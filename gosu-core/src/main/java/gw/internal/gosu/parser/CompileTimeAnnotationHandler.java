/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.config.CommonServices;
import gw.internal.gosu.parser.expressions.BeanMethodCallExpression;
import gw.internal.gosu.parser.expressions.Identifier;
import gw.internal.gosu.parser.expressions.MemberAccess;
import gw.internal.gosu.parser.expressions.MethodCallExpression;
import gw.internal.gosu.parser.expressions.NewExpression;
import gw.internal.gosu.parser.expressions.TypeLiteral;
import gw.internal.gosu.parser.statements.ClassStatement;
import gw.internal.gosu.parser.statements.FunctionStatement;
import gw.internal.gosu.parser.statements.PropertyStatement;
import gw.lang.parser.IDeclarationSiteValidator;
import gw.lang.parser.IParseTree;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.ISymbol;
import gw.lang.parser.ITypeUsesMap;
import gw.lang.parser.IUsageSiteValidator;
import gw.lang.parser.IUsageSiteValidatorReference;
import gw.lang.parser.ParserOptions;
import gw.lang.parser.StandardSymbolTable;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.parser.resources.Res;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IAttributedFeatureInfo;
import gw.lang.reflect.IConstructorInfo;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IMethodInfoDelegate;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.IGosuFragment;
import gw.lang.reflect.java.JavaTypes;
import gw.util.GosuClassUtil;
import gw.util.GosuExceptionUtil;

import java.util.List;

public class CompileTimeAnnotationHandler
{
  public static void postDefinitionVerification( IParsedElement elt )
  {
    postDefinitionVerification( elt, true );
  }

  public static Object eval( IAnnotationInfo ai )
  {
    if( ai instanceof GosuAnnotationInfo )
    {
      GosuAnnotationInfo info = (GosuAnnotationInfo) ai;
      return evalGosuAnnotation(info.getNewExpressionAsString(), info.getOwnersType());
    }
    else
    {
      return ai.getInstance();
    }
  }

  private static Object evalGosuAnnotation( String newExpressionString, IGosuClass ownersType )
  {
    if( newExpressionString != null )
    {
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
      ParserOptions parserOptions = new ParserOptions().withTypeUsesMap( usesMap );
      IGosuFragment fragment;
      try
      {
        fragment = GosuFragmentParser.getInstance().parseExpressionOnly( newExpressionString, new StandardSymbolTable( true ), parserOptions );
      }
      catch( ParseResultsException e )
      {
        throw GosuExceptionUtil.forceThrow( e );
      }
      return fragment.evaluate( null );
    }
    else
    {
      return null;
    }
  }

  private static void postDefinitionVerification( IParsedElement elt, boolean recurseOnClassStmt )
  {
    // Usage Sites
    if( elt instanceof BeanMethodCallExpression )
    {
      verifyBeanMethodCallExpression( (BeanMethodCallExpression) elt );
    }
    else if( elt instanceof MethodCallExpression )
    {
      verifyMethodCallExpression( (MethodCallExpression) elt );
    }
    else if( elt instanceof MemberAccess )
    {
      IType rootType = ((MemberAccess)elt).getRootType();
      if( rootType != null && rootType.getTypeInfo() != null && ((MemberAccess)elt).getMemberName() != null )
      {
        IPropertyInfo pi = BeanAccess.getProperty( rootType.getTypeInfo(), rootType, ((MemberAccess)elt).getMemberName() );
        if( pi != null )
        {
          verifyUsage( elt, pi.getAnnotations() );
        }
      }
    }
    else if( elt instanceof Identifier )
    {
      ISymbol sym = ((Identifier)elt).getSymbol();
      if( sym instanceof DynamicPropertySymbol )
      {
        DynamicFunctionSymbol getterDfs = ((DynamicPropertySymbol)sym).getGetterDfs();
        if( getterDfs != null && getterDfs.getMethodOrConstructorInfo() != null)
        {
           verifyUsage( elt, getterDfs.getMethodOrConstructorInfo().getAnnotations() );
        }
        DynamicFunctionSymbol setterDfs = ((DynamicPropertySymbol)sym).getSetterDfs();
        if( setterDfs != null && setterDfs.getMethodOrConstructorInfo() != null )
        {
          verifyUsage( elt, setterDfs.getMethodOrConstructorInfo().getAnnotations() );
        }
      }
    }
    else if( elt instanceof NewExpression )
    {
      IConstructorInfo ctor = ((NewExpression)elt).getConstructor();
      if( ctor != null )
      {
        verifyUsage( elt, ctor.getAnnotations() );
      }
    }
    else if( elt instanceof TypeLiteral )
    {
      IType type = ((TypeLiteral)elt).evaluate();
      if(type != null &&  type.getTypeInfo() != null )
      {
        verifyUsage( elt, ((TypeLiteral)elt).evaluate().getTypeInfo().getAnnotations() );
      }
    }

    // Declaration Sites
    if( elt instanceof ClassStatement )
    {
      IGosuClassInternal clazz = ((ClassStatement)elt).getGosuClass();
      if( clazz != null )
      {
        verifyDeclarationSite( elt, clazz.getTypeInfo().getAnnotations() );
      }
      if( !recurseOnClassStmt )
      {
        return;
      }
    }
    else if( elt instanceof FunctionStatement )
    {
      DynamicFunctionSymbol dfs = ((FunctionStatement)elt).getDynamicFunctionSymbol();
      if( dfs != null )
      {
        IAttributedFeatureInfo fi = dfs.getMethodOrConstructorInfo();
        if( fi != null )
        {
          verifyDeclarationSite( elt, fi.getAnnotations() );
        }
      }
    }
    else if( elt instanceof PropertyStatement )
    {
      FunctionStatement fs = ((PropertyStatement)elt).getPropertyGetterOrSetter();
      if( fs != null )
      {
        DynamicFunctionSymbol dfs = fs.getDynamicFunctionSymbol();
        if( dfs != null )
        {
          IAttributedFeatureInfo fi = dfs.getMethodOrConstructorInfo();
          if( fi != null )
          {
            verifyDeclarationSite( elt, fi.getAnnotations() );
          }
        }
      }
    }

    IParseTree location = elt == null ? null : elt.getLocation();
    if( location != null )
    {
      for( IParseTree parseTree : location.getChildren() )
      {
        postDefinitionVerification( parseTree.getParsedElement(), false );
      }
    }
  }

  private static void verifyBeanMethodCallExpression( BeanMethodCallExpression expr )
  {
    verifyMethodInvocation( expr, expr.getMethodDescriptor() );
  }

  private static void verifyMethodCallExpression( MethodCallExpression expr )
  {
    if( expr.getFunctionType() != null && expr.getFunctionType().getMethodInfo() != null )
    {
      verifyMethodInvocation( expr, expr.getFunctionType().getMethodInfo() );
    }
  }

  private static void verifyMethodInvocation( IParsedElement expr, IMethodInfo md )
  {
    if( md != null )
    {
      while( md instanceof IMethodInfoDelegate )
      {
        md = ((IMethodInfoDelegate)md).getSource();
      }
      verifyUsage( expr, md.getAnnotations() );
    }
  }

  private static void verifyUsage( IParsedElement expr, List<IAnnotationInfo> featureAnnotations )
  {
    for( IAnnotationInfo ai : featureAnnotations )
    {
      IUsageSiteValidator mcv = null;
      if(JavaTypes.getGosuType( IUsageSiteValidator.class ).isAssignableFrom( ai.getType() ) )
      {
        mcv = (IUsageSiteValidator)evalAndHandleError( ai, expr );
      }
      else if( JavaTypes.getGosuType( IUsageSiteValidatorReference.class ).isAssignableFrom( ai.getType() ) )
      {
        IUsageSiteValidatorReference ref = (IUsageSiteValidatorReference)evalAndHandleError( ai, expr );
        if(ref != null) {
          IType validator = TypeSystem.get( ref.value() );
          IConstructorInfo ctor = ((IRelativeTypeInfo)validator.getTypeInfo()).getConstructor( validator, null );
          if( ctor != null )
          {
            mcv = (IUsageSiteValidator)ctor.getConstructor().newInstance();
          }
          else
          {
            CommonServices.getEntityAccess().getLogger().warn( "Unable to instantiate IUsageSiteValidator of type " + validator.getName() );
          }
        }
      }
      if( mcv != null )
      {
        mcv.validate( expr );
      }
    }
  }

  private static void verifyDeclarationSite( IParsedElement feature, List<IAnnotationInfo> annotations )
  {
    if( annotations != null )
    {
      for( IAnnotationInfo annotationInfo : annotations )
      {
        if( JavaTypes.getGosuType( IDeclarationSiteValidator.class ).isAssignableFrom( annotationInfo.getType() ) )
        {
          IDeclarationSiteValidator o = (IDeclarationSiteValidator)evalAndHandleError( annotationInfo, feature );
          if( o != null )
          {
            o.validate( feature );
          }
        }
      }
    }
  }

  private static Object evalAndHandleError( IAnnotationInfo ai, IParsedElement elt )
  {
    try
    {
      IType gsClass = ai.getType();
      if( gsClass != null && gsClass instanceof IGosuClass )
      {
        // Force bytecode compilation ahead of unholy annotation evaluation
        ((IGosuClass)gsClass).getBackingClass();
      }
      return eval( ai );
    }
    catch( Exception e )
    {
      elt.addParseException( Res.MSG_COMPILE_TIME_ANNOTATION_FAILED_TO_EXECUTE, e.getMessage() );
      return null;
    }
  }

  private static void addEnclosingPackages( ITypeUsesMap map, IType type )
  {
    type = TypeLord.getPureGenericType( type );
    type = TypeLord.getOuterMostEnclosingClass( type );
    map.addToDefaultTypeUses( GosuClassUtil.getPackage( type.getName() ) + "." );
  }
}
