/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.internal.gosu.parser.java.classinfo.CompileTimeExpressionParser;
import gw.internal.gosu.parser.java.classinfo.JavaSourceDefaultValue;
import gw.lang.parser.GosuParserFactory;
import gw.lang.parser.ICompilationState;
import gw.lang.parser.IExpression;
import gw.lang.parser.IGosuParser;
import gw.lang.parser.IParseTree;
import gw.lang.parser.IReducedDynamicFunctionSymbol;
import gw.lang.parser.ISymbol;
import gw.lang.parser.ITypeUsesMap;
import gw.lang.parser.ParserOptions;
import gw.lang.parser.StandardSymbolTable;
import gw.lang.parser.SymbolType;
import gw.lang.parser.TypelessScriptPartId;
import gw.lang.parser.exceptions.ErrantGosuClassException;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.parser.expressions.IIdentifierExpression;
import gw.lang.parser.expressions.INewExpression;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IAttributedFeatureInfo;
import gw.lang.reflect.IConstructorInfo;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IParameterInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.IGosuClassTypeInfo;
import gw.lang.reflect.gs.IGosuConstructorInfo;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.java.IJavaClassMethod;
import gw.lang.reflect.java.IJavaType;
import gw.util.GosuClassUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GosuAnnotationInfo implements IAnnotationInfo
{
  private static final Object NOT_FOUND = new Object() { public String toString() {return "NOT FOUND";} };

  private int _index;
  private volatile Object _value;
  private IFeatureInfo  _container;
  private IGosuClassInternal  _owner;
  private String _newExpressionAsString;
  private INewExpression _expr;
  private IGosuAnnotation _rawAnnotation;
  private IType _type;

  public GosuAnnotationInfo( IGosuAnnotation rawAnnotation, IFeatureInfo container, IGosuClassInternal owner, int index )
  {
    _rawAnnotation = rawAnnotation;
    _container = container;
    _index = index;
    _value = null;
    _owner = owner;
    _newExpressionAsString = rawAnnotation.getNewExpressionAsString();
    _type = rawAnnotation.getType();
    IExpression e = rawAnnotation.getExpression();
    _expr = e instanceof INewExpression ? (INewExpression)e : null;
  }

  public String getName()
  {
    return _type.getName();
  }

  public IFeatureInfo getContainer()
  {
    return _container;
  }

  public IGosuClassInternal getOwnersType()
  {
    return _owner;
  }

  public String getDisplayName()
  {
    return getName();
  }

  public String getDescription()
  {
    return getName();
  }

  public Object getInstance()
  {
    if( _value == null )
    {
      TypeSystem.lock();
      ICompilationState state = _owner.getCompilationState();
      if( state.isCompilingHeader() || state.isCompilingDeclarations() || state.isCompilingDefinitions() )
      {
        throw new IllegalStateException( "You cannot request Annotation values during compilation phase." );
      }
      try
      {
        if( !_owner.isValid() )
        {
          throw new ErrantGosuClassException( _owner );
        }
        if( _value == null )
        {
          Map<String, List> featureMap = _owner.getRuntimeFeatureAnnotationMap();
          List annotations = featureMap.get( getFeatureName() );
          if( annotations == null )
          {
            throw new IllegalStateException( "Could not resolve feature map for " + getFeatureName() );
          }
          if( _index >= annotations.size() )
          {
            throw new IllegalStateException( "Index " + _index + " is not a valid index into " + annotations );
          }
          Object val = annotations.get( _index );
          _value = val;
        }
      }
      finally
      {
        TypeSystem.unlock();
      }
    }
    return _value;
  }

//  @Override
//  public Object _getFieldValue(String field) {
//    Object instance = getInstance();
//    try {
//      Method method = instance.getClass().getMethod(field);
//      Object value = method.invoke(instance);
//      value = CompileTimeExpressionParser.convertValueToInfoFriendlyValue( value, getOwnersType().getTypeInfo() );
//      return value;
//    } catch (Exception e) {
//      throw new RuntimeException(e);
//    }
//  }

  @Override
  public Object getFieldValue( String field )
  {
    Object value;
    if( _type instanceof IJavaType )
    {
      value = getJavaFieldValue( (IJavaType)_type, field );
    }
    else
    {
      value = getGosuFieldValue( (IGosuClassInternal)_type, field );
    }
    return CompileTimeExpressionParser.convertValueToInfoFriendlyValue( value, _container );
  }

  private Object getGosuFieldValue( IGosuClassInternal type, String field )
  {
    Object value = getValueFromCallSite( type, field );
    if( value == NOT_FOUND )
    {
      value = getValueFromDeclaredDefaultValueAtDeclSite( type, field );
      if( value == NOT_FOUND )
      {
        throw new RuntimeException( "Annotation field, " + field + ", not found in " + type.getName() );
      }
    }
    return value;
  }

  private Object getValueFromDeclaredDefaultValueAtDeclSite( IGosuClassInternal type, String field )
  {
    List<? extends IConstructorInfo> ctors = type.getTypeInfo().getConstructors( type );
    for( IConstructorInfo ctor: ctors )
    {
      if( ctor instanceof IGosuConstructorInfo )
      {
        String[] paramNames = ((IGosuConstructorInfo)ctor).getParameterNames();
        for( int i = 0; i < paramNames.length; i++ )
        {
          String paramName = paramNames[i];
          if( paramName != null && paramName.equals( field ) )
          {
            return ((IGosuConstructorInfo)ctor).getDefaultValueExpressions()[i].evaluate();
          }
        }
      }
    }
    return NOT_FOUND;
  }

  private Object getJavaFieldValue( IJavaType type, String field )
  {
    Object value = getValueFromCallSite( type, field );
    if( value == NOT_FOUND )
    {
      value = getValueFromDeclaredDefaultValueAtDeclSite( type, field );
      if( value == NOT_FOUND )
      {
        throw new RuntimeException( "Annotation field, " + field + ", not found in " + type.getName() );
      }
    }
    return value;
  }

  private Object getValueFromDeclaredDefaultValueAtDeclSite( IJavaType type, String field )
  {
    IJavaClassInfo classInfo = type.getBackingClassInfo();
    try
    {
      IJavaClassMethod m = classInfo.getDeclaredMethod( field );
      Object value = m.getDefaultValue();
      if( value instanceof JavaSourceDefaultValue )
      {
        value = ((JavaSourceDefaultValue) value).evaluate();
      }
      return value;
    }
    catch( NoSuchMethodException e )
    {
      return NOT_FOUND;
    }
  }

  private Object getValueFromCallSite( IType type, String field )
  {
    List<IIdentifierExpression> ids = new ArrayList<IIdentifierExpression>();
    getExpr().getContainedParsedElementsByType( IIdentifierExpression.class, ids );
    boolean bTypedSymFound = false;
    if( !ids.isEmpty() )
    {
      for( IIdentifierExpression id: ids )
      {
        ISymbol sym = id.getSymbol();
        bTypedSymFound = bTypedSymFound || sym instanceof TypedSymbol;
        if( sym instanceof TypedSymbol &&
            ((TypedSymbol)sym).getSymbolType() == SymbolType.NAMED_PARAMETER &&
            sym.getName().equals( field ) )
        {
          IParseTree nextSibling = id.getLocation().getNextSibling();
          if( nextSibling != null )
          {
            Expression expr = (Expression)nextSibling.getParsedElement();
            return expr.evaluate();
          }
          return NOT_FOUND;
        }
      }
    }

    if( !bTypedSymFound )
    {
      // Assume old-style ctor invocation where params are not named, so
      // we must also assume the ctor-defined param ordering and get the
      // field names and ordering from the params.
      IExpression[] args = getExpr().getArgs();
      if( args != null && args.length > 0 )
      {
        IParameterInfo[] parameters = getExpr().getConstructor().getParameters();
        for( int i = 0; i < parameters.length; i++ )
        {
          IParameterInfo param = parameters[i];
          if( field.equalsIgnoreCase( param.getDisplayName() ) )
          {
            return args[i].evaluate();
          }
        }
        if( getType() instanceof IJavaType ) {
          IJavaClassInfo classInfo = ((IJavaType)getType()).getBackingClassInfo();
          if( classInfo instanceof ClassJavaClassInfo ) {
            Field[] fields = classInfo.getBackingClass().getDeclaredFields();
            for( int i = 0; i < fields.length; i++ )
            {
              Field f = fields[i];
              if( field.equalsIgnoreCase( f.getName() ) )
              {
                return args[i].evaluate();
              }
            }
          }
        }
      }
    }

    if( "value".equalsIgnoreCase( field ) )
    {
      IExpression[] args = getExpr().getArgs();
      if( args != null && args.length == 1 )
      {
        return args[0].evaluate();
      }
    }
    return NOT_FOUND;
  }

  private INewExpression getExpr()
  {
    if( _expr == null )
    {
      _expr = parseNewExpression();
    }
    return _expr;
  }

  private INewExpression parseNewExpression()
  {
    IGosuClassInternal ownersType = (IGosuClassInternal)_container.getOwnersType();
    ITypeUsesMap usesMap;
    IType outerMostEnclosingType = TypeLord.getOuterMostEnclosingClass( ownersType );
    if( outerMostEnclosingType instanceof IGosuClass )
    {
      usesMap = ((IGosuClass)outerMostEnclosingType).getTypeUsesMap();
    }
    else
    {
      usesMap = ownersType.getTypeUsesMap();
    }
    if( usesMap != null )
    {
      usesMap = usesMap.copy();
      usesMap.addToDefaultTypeUses( "gw.lang." );
    }
    else
    {
      usesMap = new TypeUsesMap();
    }
    addEnclosingPackages( usesMap, ownersType );
    ParserOptions options = new ParserOptions().withTypeUsesMap( usesMap );
    IGosuParser parser = GosuParserFactory.createParser( _newExpressionAsString );
    options.setParserOptions( parser );

    StandardSymbolTable symTable = new StandardSymbolTable( true );
    TypeSystem.pushSymTableCtx( symTable );
    try
    {
      parser.setSymbolTable( TypeSystem.getCompiledGosuClassSymbolTable() ); // Set up the symbol table
      return (INewExpression)parser.parseExp( new TypelessScriptPartId( toString() ), options.getExpectedType(), options.getFileContext(), false );
    }
    catch( ParseResultsException e )
    {
      if( e.hasOnlyParseWarnings() )
      {
        return (INewExpression)e.getParsedElement();
      }
      else
      {
        throw new RuntimeException( e );
      }
    }
    finally
    {
      TypeSystem.popSymTableCtx();
    }
  }

  private static void addEnclosingPackages( ITypeUsesMap map, IType type )
  {
    type = TypeLord.getPureGenericType( type );
    type = TypeLord.getOuterMostEnclosingClass( type );
    map.addToDefaultTypeUses( GosuClassUtil.getPackage( type.getName() ) + "." );
  }

  private String getFeatureName()
  {
    return _container instanceof IGosuClassTypeInfo ? GosuClass.CLASS_ANNOTATION_SLOT : resolveFeatureName();
  }

  private String resolveFeatureName()
  {
    if( _container instanceof AbstractGenericMethodInfo )
    {
      AbstractGenericMethodInfo gmi = (AbstractGenericMethodInfo)_container;

      IReducedDynamicFunctionSymbol fs = gmi.getDfs();
      IAttributedFeatureInfo actualFeatureInfo = gmi;
      while( fs instanceof ReducedParameterizedDynamicFunctionSymbol )
      {
        fs = ((ReducedParameterizedDynamicFunctionSymbol) fs).getBackingDfs();
      }
      //IAttributedFeatureInfo actualFeatureInfo = fs.getMethodOrConstructorInfo();
      return actualFeatureInfo.getName();
    }
    else
    {
      return _container.getName();
    }
  }

  public IType getType()
  {
    if (TypeSystem.isDeleted(_type)) {
      return TypeSystem.getErrorType(_type.getName());
    } else {
      return TypeLord.getPureGenericType( _type );
    }
  }

  public String toString()
  {
    return getName();
  }

  public String getNewExpressionAsString() {
    return _newExpressionAsString;
  }

  public IGosuAnnotation getRawAnnotation()
  {
    return _rawAnnotation;
  }
}
