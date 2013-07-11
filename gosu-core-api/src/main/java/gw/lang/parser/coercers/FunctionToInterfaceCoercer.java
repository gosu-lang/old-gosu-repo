/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.lang.parser.coercers;

import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.MethodList;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.java.IJavaMethodInfo;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IParameterInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeVariableType;
import gw.lang.reflect.gs.IGenericTypeVariable;
import gw.lang.reflect.gs.IGosuEnhancement;
import gw.lang.GosuShop;
import gw.lang.function.IBlock;
import gw.lang.parser.ICoercer;
import gw.lang.parser.IResolvingCoercer;
import gw.config.CommonServices;
import gw.lang.reflect.java.JavaTypes;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FunctionToInterfaceCoercer extends BaseCoercer implements IResolvingCoercer
{
  private static FunctionToInterfaceCoercer _instance = new FunctionToInterfaceCoercer();

  public static FunctionToInterfaceCoercer instance()
  {
    return _instance;
  }

  private FunctionToInterfaceCoercer()
  {
  }

  public Object coerceValue( IType typeToCoerceTo, Object value )
  {
    IJavaType javaType = (IJavaType)typeToCoerceTo;
    if( value instanceof IBlock )
    {
      Class proxyClass = GosuShop.getBlockToInterfaceConversionClass( javaType );

      final IBlock blk = (IBlock)value;
      IJavaMethodInfo javaMethodInfo = getSingleMethod( javaType );
      final IType methodReturnType = javaMethodInfo.getReturnType();
      try {
        Field field = blk.getClass().getField( "_returnType" );
        ICoercer coercer = getCoercer( (IType)field.get( blk ), methodReturnType );
        return proxyClass.getConstructor(IBlock.class, ICoercer.class, IType.class).newInstance(blk, coercer, methodReturnType );
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
    else
    {
      throw new IllegalStateException();
    }
  }

  private ICoercer getCoercer( IType type, IType returnType )
  {
    if( !returnType.isAssignableFrom( type ) )
    {
      final ICoercer coercer = CommonServices.getCoercionManager().findCoercer( returnType, type, true );
      if( coercer == null )
      {
        if( JavaTypes.pVOID().equals(returnType) )
        {
          return IdentityCoercer.instance();
        }
        throw new IllegalStateException( "Unable to coerce return value " + type + " of block to " + returnType );
      }
      return coercer;
    }
    return null;
  }

  public static IFunctionType getRepresentativeFunctionType( IType interfaceType )
  {
    IJavaMethodInfo javaMethodInfo = getSingleMethod( interfaceType );
    if( javaMethodInfo != null )
    {
      return GosuShop.createFunctionType( javaMethodInfo );
    }
    else
    {
      return null;
    }
  }

  public static IJavaMethodInfo getSingleMethod( IType interfaceType )
  {
    if( interfaceType.isInterface() && interfaceType instanceof IJavaType )
    {
      IJavaType javaIntrinsicType = (IJavaType)interfaceType;
      List<IMethodInfo> list = new ArrayList<IMethodInfo>( javaIntrinsicType.getTypeInfo().getMethods() );

      //extract all object methods since they are guaranteed to be implemented
      for( Iterator<? extends IMethodInfo> it = list.iterator(); it.hasNext(); )
      {
        IMethodInfo methodInfo = it.next();
        IParameterInfo[] parameterInfos = methodInfo.getParameters();
        IType[] paramTypes = new IType[parameterInfos.length];
        for( int i = 0; i < parameterInfos.length; i++ )
        {
          paramTypes[i] = parameterInfos[i].getFeatureType();
        }
        if( JavaTypes.OBJECT().getTypeInfo().getMethod( methodInfo.getDisplayName(), paramTypes ) != null ||
            methodInfo.getOwnersType() instanceof IGosuEnhancement )
        {
          it.remove();
        }
      }

      if( list.size() == 1 && list.get( 0 ) instanceof IJavaMethodInfo )
      {
        return (IJavaMethodInfo)list.get( 0 );
      }
    }
    return null;
  }

  public boolean isExplicitCoercion()
  {
    return false;
  }

  public boolean handlesNull()
  {
    return false;
  }

  public int getPriority( IType to, IType from )
  {
    return 0;
  }

  public IType resolveType( IType target, IType source )
  {
    IFunctionType sourceFun = (IFunctionType)source;
    IType returnType = sourceFun.getReturnType();
    IType methodReturnType = extractReturnTypeFromInterface( target );
    if( methodReturnType instanceof ITypeVariableType )
    {
      IGenericTypeVariable[] typeVariables = target.getGenericTypeVariables();
      IType[] parameterizationTypes = new IType[typeVariables.length];
      for( int i = 0; i < typeVariables.length; i++ )
      {
        IGenericTypeVariable typeVariable = typeVariables[i];
        if( typeVariable.getName().equals( methodReturnType.getName() ) )
        {
          parameterizationTypes[i] = returnType;
        }
        else
        {
          parameterizationTypes[i] = target.getTypeParameters()[i];
        }
      }
      return target.getParameterizedType( parameterizationTypes );
    }
    else
    {
      return target;
    }
  }

  private IType extractReturnTypeFromInterface( IType target )
  {
    for( IMethodInfo methodInfo : target.getTypeInfo().getMethods() )
    {
      if( methodInfo.getOwnersType().equals( target ) )
      {
        return methodInfo.getReturnType();
      }
    }
    return null;
  }

}
