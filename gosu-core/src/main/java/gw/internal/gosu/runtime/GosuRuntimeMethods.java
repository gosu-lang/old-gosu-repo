/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.internal.gosu.runtime;

import gw.internal.gosu.ir.transform.AbstractElementTransformer;
import gw.internal.gosu.parser.TypeLord;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.ReflectUtil;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.java.IJavaType;
import gw.util.GosuExceptionUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class GosuRuntimeMethods {

  public static Object getProperty( Object rootObject, IType type, String propertyName )
  {
    IPropertyInfo propertyInfo = getPropertyInfo( rootObject, type, propertyName );
    return propertyInfo.getAccessor().getValue( rootObject );
  }

  public static Object getPropertyDynamically(Object rootObject, String propertyName) {
    if (rootObject == null) {
      throw new NullPointerException();
    }
    return getProperty(rootObject, TypeSystem.getFromObject(rootObject), propertyName);
  }

  public static void setProperty( Object rootObject, IType type, String propertyName, Object value )
  {
    IPropertyInfo propertyInfo = getPropertyInfo( rootObject, type, propertyName );
    propertyInfo.getAccessor().setValue( rootObject, value );
  }

  public static void setPropertyDynamically(Object rootObject, String propertyName, Object value) {
    if (rootObject == null) {
      throw new NullPointerException();
    }
    setProperty(rootObject, TypeSystem.getFromObject(rootObject), propertyName, value);
  }

  private static IPropertyInfo getPropertyInfo( Object rootObject, IType type, String propertyName )
  {
    IPropertyInfo propertyInfo = ReflectUtil.findProperty( type, propertyName );
    if( propertyInfo == null )
    {
      propertyInfo = ReflectUtil.findProperty( TypeSystem.getFromObject( rootObject ), propertyName );
      if( propertyInfo == null )
      {
        throw new IllegalArgumentException( "No property named " + propertyName + " found on type " + type.getName() );
      }
    }
    return propertyInfo;
  }

  public static Object initMultiArray( IType componentType, Object instance, int iDimension, int[] sizes )
  {
    if( sizes.length <= iDimension-1 )
    {
      return instance;
    }

    int iLength = componentType.getArrayLength( instance );
    componentType = componentType.getComponentType();
    for( int i = 0; i < iLength; i++ )
    {
      Object component = componentType.makeArrayInstance( sizes[iDimension-1] );
      initMultiArray( componentType, component, iDimension + 1, sizes );
      componentType.setArrayComponent( instance, i, component );
    }
    return instance;
  }

  public static IType getType( Object obj )
  {
    return TypeSystem.get( obj.getClass() );
  }

  public static Object invokeMethod( Class c, String methodName, Class[] argTypes, Object root, Object[] args )
  {
    Method declaredMethod = AbstractElementTransformer.getDeclaredMethod( c, methodName, argTypes );
    try
    {
      return declaredMethod.invoke( root, args );
    }
    catch( IllegalAccessException e )
    {
      throw GosuExceptionUtil.forceThrow( e );
    }
    catch( InvocationTargetException e )
    {
      throw GosuExceptionUtil.forceThrow( e.getTargetException() );
    }
  }

  public static Object invokeMethodInfo( IType type, String methodName, IType[] parameterTypes, Object root, Object[] args ) {
    ITypeInfo typeInfo = type.getTypeInfo();
    IMethodInfo method;
    if (typeInfo instanceof IRelativeTypeInfo) {
      method = ((IRelativeTypeInfo) typeInfo).getMethod( type, methodName, parameterTypes );
    } else {
      method = typeInfo.getMethod( methodName, parameterTypes );
    }

    if( method == null )
    {
      throw new IllegalStateException( "Could not find method for " + methodName + " on " + type.getName() + " with specified param types" );
    }
    return method.getCallHandler().handleCall( root, args );
  }

  public static Class lookUpClass( String className ) {
    if (className.startsWith("L") && className.endsWith(";")) {
      className = className.substring(1, className.length() -1 );
    }
    className = className.replaceAll("/", ".");

    try
    {
      return Class.forName(className);
    }
    catch( ClassNotFoundException e )
    {
      throw GosuExceptionUtil.forceThrow( e );
    }
  }

  public static void invokeLockMethod( Object o )
  {
    if( o != null )
    {
      IMethodInfo iMethodInfo = TypeSystem.getFromObject( o ).getTypeInfo().getMethod( "lock" );
      if( iMethodInfo != null )
      {
        iMethodInfo.getCallHandler().handleCall( o );
      }
    }
  }

  public static IType typeof( Object o )
  {
    IType type = TypeSystem.getFromObject( o );
    if( type instanceof IJavaType && type.isGenericType() )
    {
      // Never return a generic type resulting from Java's generic type erasure.
      // Instead return the "erased" or default type.
      type = TypeLord.getDefaultParameterizedType( type );
    }
    return type;
  }

  public static void invokeUnlockOrDisposeOrCloseMethod( Object o )
  {
    if( o != null )
    {
      ITypeInfo ti = TypeSystem.getFromObject( o ).getTypeInfo();
      IMethodInfo mi = ti.getMethod( "unlock" );
      if( mi != null )
      {
        mi.getCallHandler().handleCall( o );
      }
      else
      {
        mi = ti.getMethod( "dispose" );
        if( mi != null )
        {
          mi.getCallHandler().handleCall( o );
        }
        else
        {
          mi = ti.getMethod( "close" );
          if( mi != null )
          {
            mi.getCallHandler().handleCall( o );
          }
          else
          {

          }
        }
      }
    }
  }

  /*//
    // rootType
    // .getTypeInfo()
    // .getProperty( propertyName ) or getProperty( type, propertyName )
    // .getAccessor()
    // .getValue( ctx )
    //

    MethodVisitor mv = _cc().getMethodVisitor();
    int iRootExprIndex = -1;
    if( _expr().getRootExpression() != null && !(pi instanceof ITypeInfoPropertyInfo) )
    {
      // Must get root type from root expression when getting property dynamically
      // i.e., the property may not exist on the static root type
      iRootExprIndex = _cc().getStatementCompiler().makeAndIndexTempSymbol( rootType );
      mv.visitVarInsn( Opcodes.ASTORE, iRootExprIndex );
      mv.visitVarInsn( Opcodes.ALOAD, iRootExprIndex );
      callStaticMethod( TypeSystem.class, "getFromObject", Object.class );
    }
    else if (pi instanceof ITypeInfoPropertyInfo) {
      // We want to get the MetaType . . . but calling TypeSystem.getFromObject will return the literal version
      // of the MetaType, which doesn't have all the properties we want . . . sooooooo
      // there's this gigantic hack in here to directly invoke MetaType.get
      iRootExprIndex = _cc().getStatementCompiler().makeAndIndexTempSymbol( rootType );
      mv.visitVarInsn( Opcodes.ASTORE, iRootExprIndex );
      mv.visitVarInsn( Opcodes.ALOAD, iRootExprIndex );
      callStaticMethod( MetaType.class, "get", IType.class );
    }
    else
    {
      pushType( rootType );
    }
    callInstanceMethod( IType.class, "getTypeInfo" );

    boolean relativeTypeInfo = pi != null && pi.getOwnersType().getTypeInfo() instanceof IRelativeTypeInfo;
    if( relativeTypeInfo )
    {
      checkCast( IRelativeTypeInfo.class );
      pushType( rootType );
      pushPropertyName( pi );
      callInstanceMethod( IRelativeTypeInfo.class, "getProperty", IType.class, CharSequence.class );
    }
    else
    {
      pushPropertyName( pi );
      callInstanceMethod( ITypeInfo.class, "getProperty", CharSequence.class );
    }

    callInstanceMethod( IPropertyInfo.class, "getAccessor" );

    if( iRootExprIndex >= 0 )
    {
      mv.visitVarInsn( Opcodes.ALOAD, iRootExprIndex );
    }
    else
    {
      pushRootExpression( rootType, rootExpr, pi );
    }
    callInstanceMethod( IPropertyAccessor.class, "getValue", Object.class );*/
}
