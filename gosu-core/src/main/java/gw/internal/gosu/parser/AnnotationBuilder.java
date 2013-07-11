/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.config.CommonServices;
import gw.internal.gosu.annotations.AnnotationMap;
import gw.internal.gosu.parser.expressions.BeanMethodCallExpression;
import gw.internal.gosu.parser.expressions.Identifier;
import gw.internal.gosu.parser.expressions.ImplicitTypeAsExpression;
import gw.internal.gosu.parser.expressions.NewExpression;
import gw.internal.gosu.parser.expressions.NotAWordExpression;
import gw.internal.gosu.parser.expressions.StringLiteral;
import gw.internal.gosu.parser.expressions.TypeLiteral;
import gw.internal.gosu.parser.statements.BeanMethodCallStatement;
import gw.internal.gosu.parser.statements.ReturnStatement;
import gw.internal.gosu.parser.statements.StatementList;
import gw.internal.gosu.parser.statements.VarStatement;
import gw.lang.parser.StandardSymbolTable;
import gw.lang.parser.expressions.IVarStatement;
import gw.lang.reflect.IConstructorInfo;
import gw.lang.reflect.IErrorType;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IParameterInfo;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.TypeSystemShutdownListener;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;
import gw.util.concurrent.LockingLazyVar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;


public class AnnotationBuilder
{
  private static final int MAX_STMTS_PER_EVAL_METHOD = 1000;
  private static final String ANNOTATION_MAP_BUILDER_VAR_NAME = "builder";
  public static final LockingLazyVar<Symbol> BUILDER_SYMBOL = new LockingLazyVar<Symbol>() {
    protected Symbol init() {
      return new Symbol(ANNOTATION_MAP_BUILDER_VAR_NAME, annotationMapType(), null) {
        @Override
        public int getIndex() {
          return 42; // hack to make this look like a local symbol
        }
      };
    }
  };
  static {
    TypeSystem.addShutdownListener(new TypeSystemShutdownListener() {
      public void shutdown() {
        BUILDER_SYMBOL.clear();
      }
    });
  }

  public static List<StatementList> buildAnnotationInitMethodBody( ICompilableTypeInternal gosuClass, boolean bClearOnly )
  {
    if( bClearOnly && !((IGosuClass)gosuClass).isDeclarationsCompiled() )
    {
      // Don't parse unparsed class if we're clearing
      return null;
    }

    if( gosuClass.getName().startsWith( IGosuClass.PROXY_PREFIX ) )
    {
      return Collections.emptyList();
    }

    List<Statement> stmts = new ArrayList<Statement>();

    if( !bClearOnly )
    {
      stmts.add( initAnnotationMap() );
    }

    // class annotations
    List<? extends IGosuAnnotation> annotations = gosuClass.getGosuAnnotations();
    addAnnotations( annotations, GosuClass.CLASS_ANNOTATION_SLOT, stmts, bClearOnly );

    // field annotations
    for( IVarStatement varStmt : gosuClass.getMemberFields() )
    {
      annotations = ((VarStatement)varStmt).getGosuAnnotations();
      addAnnotations( annotations, varStmt.getSymbol().getName(), stmts, bClearOnly );
    }

    // static field annotations
    for( IVarStatement varStmt : gosuClass.getStaticFields() )
    {
      annotations = ((VarStatement)varStmt).getGosuAnnotations();
      addAnnotations( annotations, varStmt.getSymbol().getName(), stmts, bClearOnly );
    }

    // constructor annotations
    for( IConstructorInfo constructorInfo : gosuClass.getTypeInfo().getDeclaredConstructors() )
    {
      if( constructorInfo instanceof GosuConstructorInfo )
      {
        annotations = ((GosuConstructorInfo)constructorInfo).getGosuAnnotations();
        addAnnotations( annotations, constructorInfo.getName(), stmts, bClearOnly);
      }
    }

    // method annotations
    for( IMethodInfo methodInfo : gosuClass.getTypeInfo().getDeclaredMethods() )
    {
      if( methodInfo instanceof GosuMethodInfo )
      {
        annotations = ((GosuMethodInfo)methodInfo).getGosuAnnotations();
        addAnnotations( annotations, methodInfo.getName(), stmts, bClearOnly );
      }
    }

    // property annotations
    for( IPropertyInfo propertyInfo : gosuClass.getTypeInfo().getDeclaredProperties() )
    {
      if( propertyInfo instanceof GosuPropertyInfo )
      {
        annotations = ((GosuPropertyInfo)propertyInfo).getGosuAnnotations();
        addAnnotations( annotations, propertyInfo.getName(), stmts, bClearOnly );
      }
    }

    if( !bClearOnly )
    {
      //assign to the static class variable
      stmts.add( makeReturnStatement( gosuClass ) );
      return makeStmtList( gosuClass, stmts );
    }
    else
    {
      return null;
    }
  }

  private static Statement makeReturnStatement( ICompilableTypeInternal gosuClass )
  {
    ReturnStatement statement = initLocation( new ReturnStatement() );
    statement.setValue( invokeExpr( "getAnnotations", new Class[0], JavaTypes.MAP() ) );
    return statement;
  }

  private static VarStatement initAnnotationMap()
  {
    VarStatement varStatement = initLocation( new VarStatement() );
    varStatement.setSymbol( BUILDER_SYMBOL.get() );
    varStatement.setType( annotationMapType() );
    NewExpression newMap = initLocation( new NewExpression() );
    newMap.setConstructor( annotationMapType().getTypeInfo().getConstructor() );
    newMap.setType( annotationMapType() );
    varStatement.setAsExpression( newMap );
    return varStatement;
  }

  private static List<StatementList> makeStmtList( ICompilableTypeInternal gosuClass, List<Statement> statements )
  {
    if( statements.isEmpty() )
    {
      return Collections.emptyList();
    }
    else
    {
      ArrayList<StatementList> lst = new ArrayList<StatementList>();
      int i = 0;
      while( i < statements.size() )
      {
        StatementList stmt = new StatementList( new StandardSymbolTable() );
        int length = Math.min( MAX_STMTS_PER_EVAL_METHOD, statements.size() - i );
        List<Statement> statementList = statements.subList( i, i + length );
        lst.add( stmt );
        i+= MAX_STMTS_PER_EVAL_METHOD;
        stmt.setStatements( statementList );
      }
      return lst;
    }
  }

  private static Expression box( Expression value )
  {
    ImplicitTypeAsExpression cast = initLocation( new ImplicitTypeAsExpression() );
    cast.setLHS( value );
    cast.setCoercer( CommonServices.getCoercionManager().resolveCoercerStatically( JavaTypes.OBJECT(), value.getType() ) );
    cast.setType( TypeSystem.getBoxType( value.getType() ) );
    value = cast;
    return value;
  }

  private static LinkedHashMap<String, Expression> createArgMap( IGosuAnnotation annotation )
  {
    LinkedHashMap<String, Expression> args = new LinkedHashMap<String, Expression>();
    if (annotation.getExpression() instanceof NotAWordExpression) {
      System.out.println("XXXDEBUG: " + annotation.getName());
      System.out.println("XXXDEBUG: " + annotation.getOwnersType());
      System.out.println("XXXDEBUG: " + annotation.getType());
    }
    NewExpression newExpression = (NewExpression)annotation.getExpression();
    IParameterInfo[] parameters = newExpression.getConstructor().getParameters();
    Expression[] argArr = newExpression.getArgs();
    if( argArr != null )
    {
      for( int i = 0; i < argArr.length; i++ )
      {
        String name = parameters[i].getName();
        Expression arg = argArr[i];
        args.put( name, arg );
      }
    }
    return args;
  }

  private static String[] getNamesForJavaAnnotation( IType type )
  {
    IRelativeTypeInfo rTypeInfo = (IRelativeTypeInfo)type.getTypeInfo();
    List<IMethodInfo> methodInfos = new ArrayList<IMethodInfo>( rTypeInfo.getDeclaredMethods() );
    Collections.sort( methodInfos, new Comparator<IMethodInfo>()
    {
      @Override
      public int compare( IMethodInfo o1, IMethodInfo o2 )
      {
        return o1.getName().compareTo( o2.getName() );
      }
    } );

    String[] strings = new String[methodInfos.size()];
    for( int i = 0; i < methodInfos.size(); i++ )
    {
      IMethodInfo methodInfo = methodInfos.get( i );
      strings[i] = methodInfo.getDisplayName();
    }
    return strings;
  }

  private static <T extends ParsedElement> T initLocation( T pe )
  {
    pe.setLocation( new ParseTree( pe, 0, 0, null ) );
    return pe;
  }

  private static void debug( String s )
  {
    System.out.println( s );
  }

  private static void addAnnotations( List<? extends IGosuAnnotation> annotations, String annotationSlot, List<Statement> stmts, boolean bClearOnly )
  {
    if( annotations.isEmpty() )
    {
      return;
    }

    stmts.add( invoke( "startAnnotationInfoForFeature", new Class[]{String.class}, new StringLiteral( annotationSlot ) ) );
    for( IGosuAnnotation annotation : annotations )
    {
      //java annotation
      IType type = annotation.getType();

      // Skip this type entirely if it's an error type
      if (type instanceof IErrorType) {
        continue;
      }
      
      if( type != null && !JavaTypes.IANNOTATION().isAssignableFrom(type) )
      {
        // Java Annotations

        if( !bClearOnly )
        {
          stmts.add( invoke( "startJavaAnnotation", new Class[]{IType.class}, initLocation( new TypeLiteral( type ) ) ) );
          LinkedHashMap<String, Expression> argMap = createArgMap( annotation );
          for( String name : argMap.keySet() )
          {
            Expression value = argMap.get( name );
            if( value.getType().isPrimitive() )
            {
              value = box( value );
            }
            stmts.add( invoke( "withArg", new Class[]{String.class, Object.class}, initLocation( new StringLiteral( name ) ), value ) );
          }
          stmts.add( invoke( "finishJavaAnnotation", new Class[0] ) );
        }
      }
      else
      {
        // Gosu Annotations

        Expression expression = (Expression) annotation.getExpression();
        if( expression == null )
        {
          throw new IllegalStateException( "The expression cannot be null when generating the annotation map.\n" +
                                           "This is probably caused by a type not being compiled fully or the " +
                                           "expression was cleared prematurely." );
        }
        else
        {
          stmts.add( invoke( "addGosuAnnotation", new Class[]{Object.class}, expression ) );
        }
      }
    }
  }

  private static Statement invoke( String name, Class[] argTypes, Expression... args )
  {
    BeanMethodCallStatement bmcs = initLocation( new BeanMethodCallStatement() );
    bmcs.setBeanMethodCall( invokeExpr( name, argTypes, annotationMapType(), args ) );
    initLocation( bmcs );
    return bmcs;
  }

  private static BeanMethodCallExpression invokeExpr( String name, Class[] argTypes, IJavaType returnType, Expression... args )
  {
    BeanMethodCallExpression bmce = initLocation( new BeanMethodCallExpression() );
    bmce.setArgs( args );
    bmce.setType( returnType );
    IRelativeTypeInfo typeInfo = (IRelativeTypeInfo)((IJavaType)annotationMapType()).getTypeInfo();
    IType[] types = new IType[argTypes.length];
    for( int i = 0; i < argTypes.length; i++ )
    {
      types[i] = TypeSystem.get( argTypes[i] );
    }
    bmce.setMethodDescriptor( typeInfo.getMethod( annotationMapType(), name, types ) );

    Identifier builderSymbol = initLocation( new Identifier() );
    builderSymbol.setSymbol( BUILDER_SYMBOL.get(), new StandardSymbolTable() );
    builderSymbol.setType( annotationMapType() );

    bmce.setRootExpression( builderSymbol );
    return bmce;
  }

  private static IJavaType annotationMapType()
  {
    return JavaTypes.getGosuType( AnnotationMap.class );
  }
}