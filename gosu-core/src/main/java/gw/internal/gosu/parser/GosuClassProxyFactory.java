/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.config.CommonServices;
import gw.lang.parser.GosuParserTypes;
import gw.lang.parser.ISource;
import gw.lang.parser.Keyword;
import gw.lang.reflect.IAttributedFeatureInfo;
import gw.lang.reflect.IConstructorInfo;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IParameterInfo;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IRelativeTypeInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.ITypeRef;
import gw.lang.reflect.Modifier;
import gw.lang.reflect.TypeInfoUtil;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.ClassType;
import gw.lang.reflect.gs.GosuClassTypeLoader;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.IGosuObject;
import gw.lang.reflect.gs.StringSourceFileHandle;
import gw.lang.reflect.java.IJavaMethodInfo;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.module.IModule;

import java.util.concurrent.Callable;

/**
 */
public class GosuClassProxyFactory
{
  private static final GosuClassProxyFactory INSTANCE = new GosuClassProxyFactory();


  private GosuClassProxyFactory()
  {
  }

  public static GosuClassProxyFactory instance()
  {
    return INSTANCE;
  }

  public IGosuClassInternal create( IType type )
  {
    if( type instanceof IJavaType )
    {
      IJavaTypeInternal javaType = (IJavaTypeInternal)type;
      return (IGosuClassInternal)createJavaProxy( javaType );
    }

    throw new IllegalArgumentException( "No handler for type: " + type.getClass().getName() );
  }

  public IGosuClassInternal createImmediately( IType type )
  {
    if( type.isParameterizedType() )
    {
      type = type.getGenericType();
    }

    if( type instanceof IJavaType )
    {
      IJavaTypeInternal javaType = (IJavaTypeInternal)type;
      return (IGosuClassInternal)createJavaProxyImmediately( javaType );
    }

    throw new IllegalArgumentException( "No handler for type: " + type.getClass().getName() );
  }

  private IGosuClass createJavaProxy( IJavaTypeInternal type )
  {
    if( type.isParameterizedType() )
    {
      type = (IJavaTypeInternal)type.getGenericType();
    }

    IGosuClassInternal gsAdapterClass;

    gsAdapterClass = type.getAdapterClass();

    if( gsAdapterClass != null )
    {
      return gsAdapterClass;
    }
    return createJavaProxyImmediately( type );
  }

  private IGosuClass createJavaProxyImmediately( IJavaTypeInternal type )
  {
    IGosuClassInternal gsAdapterClass;

    if( type.getEnclosingType() != null )
    {
      // Ensure enclosing type is proxied; it contains the gosu source for the inner type
      IGosuClass outerProxy = (IGosuClass)TypeSystem.getByFullName( getProxyName( (IJavaType)TypeLord.getOuterMostEnclosingClass( type ) ) );
      outerProxy.getInnerClasses();
      if( !outerProxy.isCompilingDeclarations() )
      {
        gsAdapterClass = (IGosuClassInternal)outerProxy.getInnerClass( type.getRelativeName().substring( type.getRelativeName().indexOf( '.' ) + 1 ) );
        if( gsAdapterClass == null )
        {
          TypeSystem.refresh( (ITypeRef)outerProxy);
          gsAdapterClass = (IGosuClassInternal)outerProxy.getInnerClass( type.getRelativeName().substring( type.getRelativeName().indexOf( '.' ) + 1 ) );
        }
      }
      else
      {
        return null;
      }
    }
    else
    {
      if( type.isInterface() )
      {
        gsAdapterClass = (IGosuClassInternal)createJavaInterfaceProxy( type );
      }
      else
      {
        gsAdapterClass = (IGosuClassInternal)createJavaClassProxy( type );
      }
    }

    type.setAdapterClass( gsAdapterClass );

    gsAdapterClass.setJavaType( type );
    return gsAdapterClass;
  }

  private IGosuClass createJavaInterfaceProxy( final IJavaType type )
  {
    final IModule module = type.getTypeLoader().getModule();
    GosuClassTypeLoader loader = GosuClassTypeLoader.getDefaultClassLoader( module );
    return loader.makeNewClass(
        new LazyStringSourceFileHandle(type, new Callable<StringBuilder>() {
          public StringBuilder call() {
            TypeSystem.pushModule( module );
            try {
              return genJavaInterfaceProxy(type);
            } finally {
              TypeSystem.popModule( module );
            }
          }
        }));
  }

  private IGosuClass createJavaClassProxy( final IJavaType type )
  {
    String strProxy = IGosuClass.PROXY_PREFIX + '.' + type.getName();
    IType compilingType = GosuClassCompilingStack.getCompilingType( strProxy );
    if( compilingType != null )
    {
      return (IGosuClass)compilingType;
    }

    final IModule module = type.getTypeLoader().getModule();
    return GosuClassTypeLoader.getDefaultClassLoader( module ).makeNewClass(
      new LazyStringSourceFileHandle( type, new Callable<StringBuilder>()
      {
        public StringBuilder call()
        {
          TypeSystem.pushModule( module );
          try {
            return genJavaClassProxy( type );
          } 
          finally
          {
            TypeSystem.popModule( module );
          }
        }
      } ) );
  }

  class LazyStringSourceFileHandle extends StringSourceFileHandle
  {
    private Callable<StringBuilder> _sourceGen;

    public LazyStringSourceFileHandle( IJavaType type, Callable<StringBuilder> sourceGen )
    {
      super( getProxyName( type ), null, false, ClassType.Class );
      _sourceGen = sourceGen;
    }

    @Override
    public ISource getSource()
    {
      if( getRawSource() == null )
      {
        try
        {
          setRawSource( _sourceGen.call().toString() );
        }
        catch( Exception e )
        {
          throw new RuntimeException( e );
        }
      }
      return super.getSource();
    }
  }

  private String getProxyName( IJavaType type )
  {
    if( type.isParameterizedType() )
    {
      type = type.getGenericType();
    }
    return IGosuClass.PROXY_PREFIX + '.' + type.getName();
  }

  private StringBuilder genJavaClassProxy( IJavaType type )
  {
    if( type.isParameterizedType() )
    {
      type = type.getGenericType();
    }
    StringBuilder sb = new StringBuilder();
    sb.append( "package " ).append( IGosuClass.PROXY_PREFIX ).append('.').append( type.getNamespace() ).append( '\n' );
    genClassImpl(type, sb);
    return sb;
  }

  private void genClassImpl( IJavaType type, StringBuilder sb )
  {
    if( type.isAbstract() )
    {
      sb.append( "abstract " );
    }
    if( type.getEnclosingType() != null && Modifier.isStatic( type.getModifiers() ) )
    {
      sb.append( "static " );
    }

    sb.append( "class " ).append( getRelativeName( type ) ).append( '\n' );
    sb.append( "{\n" );

    JavaTypeInfo ti;
    if( type.getTypeInfo() instanceof JavaTypeInfo )
    {
      ti = (JavaTypeInfo)type.getTypeInfo();
    }
    else
    {
      throw new IllegalArgumentException( type + " is not a recognized Java type" );
    }

    // Constructors
    for( Object o : ti.getConstructors( type ) )
    {
      IConstructorInfo ci = (IConstructorInfo)o;
      genConstructor( sb, ci );
    }

    // Properties
    for( Object o : ti.getProperties( type ) )
    {
      IPropertyInfo pi = (IPropertyInfo)o;
      genProperty( pi, sb, type );
    }

    // Methods
    for( Object o : ti.getMethods( type ) )
    {
      IMethodInfo mi = (IMethodInfo)o;
      genMethodImpl( sb, mi, type );
    }
    // Inner classes/interfaces
    for( IJavaType innerClass : type.getInnerClasses() )
    {
      if( (Modifier.isPublic( innerClass.getModifiers() ) ||
           Modifier.isProtected( innerClass.getModifiers() )) && //## todo: maybe change to include internal for bytecode?
          !Modifier.isFinal( innerClass.getModifiers() ) )
      {
        if( innerClass.isInterface() )
        {
          genInterfaceImpl( innerClass, sb );
        }
        else if( Modifier.isStatic( innerClass.getModifiers() ) ) // must be static, otherwise can't really generate a java super class proxy
        {
          genClassImpl( innerClass, sb );
        }
      }
    }
    sb.append( "}\n" );
  }

  private String getRelativeName( IJavaType type )
  {
    String strName = TypeSystem.getGenericRelativeName( type, false );
    if( type.getEnclosingType() != null )
    {
      int iParamsIndex = strName.indexOf( '<' );
      int iIndex = iParamsIndex > 0
                   ? strName.substring( 0, iParamsIndex ).lastIndexOf( '.' )
                   : strName.lastIndexOf( '.' );
      if( iIndex > 0 )
      {
        strName = strName.substring( iIndex + 1 );
      }
    }
    return strName;
  }

  private StringBuilder genJavaInterfaceProxy( IJavaType type )
  {
    if( type.isParameterizedType() )
    {
      type = type.getGenericType();
    }
    StringBuilder sb = new StringBuilder();
    sb.append( "package " ).append( IGosuClass.PROXY_PREFIX ).append( '.' ).append( type.getNamespace() ).append('\n');
    genInterfaceImpl( type, sb );
    return sb;
  }

  private void genInterfaceImpl( IJavaType type, StringBuilder sb )
  {
    sb.append( Modifier.toModifierString(type.getModifiers()) + " interface " ).append( getRelativeName( type ) ).append('\n');
    sb.append( "{\n" );

    ITypeInfo ti = type.getTypeInfo();

    // Interface properties
    for( Object o : ti.getProperties() )
    {
      IPropertyInfo pi = (IPropertyInfo)o;
      genInterfacePropertyDecl( sb, pi, type );
    }

    // Interface methods
    for( Object o : ti.getMethods() )
    {
      IMethodInfo mi = (IMethodInfo)o;
      genInterfaceMethodDecl( sb, mi );
    }

    // Inner interfaces
    for( IJavaType iface : type.getInnerClasses() )
    {
      if( iface.isInterface() &&
          (Modifier.isPublic( iface.getModifiers() ) ||
           Modifier.isProtected( iface.getModifiers() )) && //## todo: maybe change to include internal for bytecode?
          !Modifier.isFinal( iface.getModifiers() ) )
      {
        genInterfaceImpl( iface, sb );
      }
    }
    sb.append( "}\n" );
  }

  private void genMethodImpl( StringBuilder sb, IMethodInfo mi, IJavaType type )
  {
    if( mi.isPrivate() || mi.isInternal() )
    {
      return;
    }

    if( mi.isStatic() && mi.getDisplayName().indexOf( '$' ) < 0 )
    {
      genStaticMethod( sb, mi, type );
    }
    else
    {
      genMemberMethod( sb, mi, type );
    }
  }

  private void genConstructor( StringBuilder sb, IConstructorInfo ci )
  {
    if( ci.isPrivate() || ((ci instanceof JavaConstructorInfo) && ((JavaConstructorInfo)ci).isSynthetic()) )
    {
      return;
    }

    sb.append( "  construct(" );
    IParameterInfo[] params = getGenericParameters( ci );
    for( int i = 0; i < params.length; i++ )
    {
      IParameterInfo pi = params[i];
      sb.append( ' ' ).append( "p" ).append( i ).append( " : " ).append( pi.getFeatureType().getName() )
        .append( i < params.length - 1 ? ',' : ' ' );
    }
    sb.append( ")\n" )
      .append( "{\n" )
      .append( "}\n" );
  }

  private StringBuilder appendVisibilityModifier( IAttributedFeatureInfo fi )
  {
    StringBuilder sb = new StringBuilder();
    if( fi.isProtected() )
    {
      sb.append( Keyword.KW_protected ).append( " " );
    }
    else if( fi.isInternal() )
    {
      sb.append( Keyword.KW_internal ).append( " " );
    }
    return sb;
  }

  private StringBuilder appendFieldVisibilityModifier( IAttributedFeatureInfo fi )
  {
    StringBuilder sb = new StringBuilder();
    if( fi.isProtected() )
    {
      sb.append( Keyword.KW_protected ).append( " " );
    }
    else if( fi.isInternal() )
    {
      sb.append( Keyword.KW_internal ).append( " " );
    }
    else if( fi.isPublic() )
    {
      sb.append( Keyword.KW_public ).append( " " );
    }
    return sb;
  }

  private void genMemberMethod( StringBuilder sb, IMethodInfo mi, IJavaType type )
  {
    if( !canExtendMethod( mi ) )
    {
      return;
    }

    StringBuilder sbModifiers = buildModifiers( mi );
    if( mi.getDescription() != null )
    {
      sb.append( "/** " ).append( mi.getDescription() ).append( " */" );
    }
    sb.append( "  " ).append( sbModifiers ).append( "function " ).append( mi.getDisplayName() ).append( TypeInfoUtil.getTypeVarList( mi ) ).append( "(" );
    IParameterInfo[] params = getGenericParameters( mi );
    for( int i = 0; i < params.length; i++ )
    {
      IParameterInfo pi = params[i];
      sb.append( ' ' ).append( "p" ).append( i ).append( " : " ).append( pi.getFeatureType().getName() )
        .append( i < params.length - 1 ? ',' : ' ' );
    }
    sb.append( ") : " ).append( getGenericReturnType( mi ).getName() ).append( "\n" );
    if( !mi.isAbstract() )
    {
      generateStub( sb, mi.getReturnType() );
    }
  }

  private void generateStub( StringBuilder sb, IType returnType )
  {
    sb.append( "{\n" )
      .append( (returnType == JavaTypes.pVOID()
                ? ""
                : "    return " +
                  (!returnType.isPrimitive()
                   ? "null"
                   : CommonServices.getCoercionManager().convertNullAsPrimitive( returnType, false ))) );
    sb.append( "}\n" );
  }

  private boolean canExtendMethod( IMethodInfo mi )
  {
    if( !(mi instanceof JavaMethodInfo) )
    {
      // It is possible that a methodinfo on a java type originates outside of java.
      // E.g., enhancement methods. Gosu does not support extending these.
      return false;
    }

    if( isPropertyMethod( mi ) )
    {
      // We favor properties over methods -- gotta pick one
      return false;
    }

    int iMethodModifiers = ((IJavaMethodInfo)mi).getModifiers();
    return //!java.lang.reflect.Modifier.isFinal( iMethodModifiers ) &&
      !java.lang.reflect.Modifier.isNative( iMethodModifiers ) &&
      // See GosuClassInstanceFactory.genSuperClassMembers() (we don't allow finalizers)
      !mi.getDisplayName().equals( "finalize" ) &&
      mi.getDisplayName().indexOf( '$' ) < 0;
  }

  private void genStaticMethod( StringBuilder sb, IMethodInfo mi, IJavaType type )
  {
    if( !(mi instanceof JavaMethodInfo) )
    {
      // It is possible that a methodinfo on a java type originates outside of java.
      // E.g., enhancement methods. Only static JAVA members should be reflected in
      // the proxy.
      return;
    }

    if( isPropertyMethod( mi ) )
    {
      // We favor properties over methods -- gotta pick one
      return;
    }

    StringBuilder sbModifiers = appendVisibilityModifier( mi );
    if( mi.getDescription() != null )
    {
      sb.append( "/** " ).append( mi.getDescription() ).append( " */" );
    }
    sb.append( "  " ).append( sbModifiers ).append( "static function " ).append( mi.getDisplayName() ).append( TypeInfoUtil.getTypeVarList( mi ) ).append( "(" );
    IParameterInfo[] params = getGenericParameters( mi );
    for( int i = 0; i < params.length; i++ )
    {
      IParameterInfo pi = params[i];
      sb.append( ' ' ).append( "p" ).append( i ).append( " : " ).append( pi.getFeatureType().getName() )
        .append( i < params.length - 1 ? ',' : ' ' );
    }
    sb.append( ") : " ).append( getGenericReturnType( mi ).getName() ).append( "\n" )
      .append( "{\n" )
      .append( (mi.getReturnType() == GosuParserTypes.NULL_TYPE()
                ? ""
                : "    return ") )
      .append( type.getName() ).append( '.' ).append( mi.getDisplayName() ).append( TypeInfoUtil.getTypeVarListNoBounds( mi ) ).append( "(" );
    for( int i = 0; i < params.length; i++ )
    {
      sb.append( ' ' ).append( "p" ).append( i ).append( i < params.length - 1 ? ',' : ' ' );
    }
    sb.append( ");\n" );
    sb.append( "}\n" );
  }

  private void genInterfaceMethodDecl( StringBuilder sb, IMethodInfo mi )
  {
    if( !(mi instanceof JavaMethodInfo) )
    {
      // It is possible that a methodinfo on a java type originates outside of java.
      // E.g., enhancement methods. Gosu does not support extending these.
      return;
    }
    if( isPropertyMethod( mi ) )
    {
      return;
    }
    if( mi.getDisplayName().equals( "hashCode" ) || mi.getDisplayName().equals( "equals" ) || mi.getDisplayName().equals( "toString" ) )
    {
      if( !mi.getOwnersType().getName().equals( IGosuObject.class.getName() ) )
      {
        return;
      }
    }
    if( mi.getDescription() != null )
    {
      sb.append( "/** " ).append( mi.getDescription() ).append( " */" );
    }
    sb.append( "  function " ).append( mi.getDisplayName() ).append( TypeInfoUtil.getTypeVarList( mi ) ).append( "(" );
    IParameterInfo[] params = getGenericParameters( mi );
    for( int i = 0; i < params.length; i++ )
    {
      IParameterInfo pi = params[i];
      sb.append( ' ' ).append( "p" ).append( i ).append( " : " ).append( pi.getFeatureType().getName() );
      sb.append( i < params.length - 1 ? ',' : ' ' );
    }
    sb.append( ") : " ).append( getGenericReturnType( mi ).getName() ).append( ";\n" );
  }

  private boolean isPropertyMethod( IMethodInfo mi )
  {
    return isPropertyGetter( mi ) ||
           isPropertySetter( mi );
  }

  private boolean isPropertyGetter( IMethodInfo mi )
  {
    return isPropertyGetter( mi, "get" ) ||
           isPropertyGetter( mi, "is" );
  }

  private boolean isPropertySetter( IMethodInfo mi )
  {
    String strMethod = mi.getDisplayName();
    if( strMethod.startsWith( "set" ) &&
        strMethod.length() > 3 &&
        mi.getParameters().length == 1 &&
        mi.getReturnType() == JavaTypes.pVOID() )
    {
      String strProp = strMethod.substring( "set".length() );
      if( Character.isUpperCase( strProp.charAt( 0 ) ) )
      {
        ITypeInfo ti = (ITypeInfo)mi.getContainer();
        IPropertyInfo pi = ti instanceof IRelativeTypeInfo
                           ? ((IRelativeTypeInfo)ti).getProperty( mi.getOwnersType(), strProp )
                           : ti.getProperty( strProp );
        if( pi != null && pi.isReadable() &&
            getGenericType( pi ).getName().equals( getGenericParameters( mi )[0].getFeatureType().getName() ) )
        {
          return !Keyword.isReserved( pi.getName() ) || Keyword.isReservedValue( pi.getName() );
        }
      }
    }
    return false;
  }

  private boolean isPropertyGetter( IMethodInfo mi, String strPrefix )
  {
    String strMethod = mi.getDisplayName();
    if( strMethod.startsWith( strPrefix ) &&
        mi.getParameters().length == 0 )
    {
      String strProp = strMethod.substring( strPrefix.length() );
      if( strProp.length() > 0 && Character.isUpperCase( strProp.charAt( 0 ) ) )
      {
        ITypeInfo ti = (ITypeInfo)mi.getContainer();
        IPropertyInfo pi = ti instanceof IRelativeTypeInfo
                           ? ((IRelativeTypeInfo)ti).getProperty( mi.getOwnersType(), strProp )
                           : ti.getProperty( strProp );
        if( pi != null && getGenericType( pi ).getName().equals( getGenericReturnType( mi ).getName() ) )
        {
          return !Keyword.isReserved( pi.getName() ) || Keyword.isReservedValue( pi.getName() );
        }
      }
    }
    return false;
  }

  private void genInterfacePropertyDecl( StringBuilder sb, IPropertyInfo pi, IJavaType javaType )
  {
    if( pi.isStatic() )
    {
      genStaticProperty( pi, sb, javaType );
      return;
    }
    if( !pi.isReadable() )
    {
      return;
    }
    if( !(pi instanceof JavaBaseFeatureInfo) )
    {
      // It is possible that a methodinfo on a java type originates outside of java.
      // E.g., enhancement methods. Gosu does not support extending these.
      return;
    }
    IType type = getGenericType( pi );
    if( pi.getDescription() != null )
    {
      sb.append( "/** " ).append( pi.getDescription() ).append( " */" );
    }
    sb.append( " property get " ).append( pi.getName() ).append( "() : " ).append( type.getName() ).append( "\n" );
    if( pi.isWritable( pi.getOwnersType() ) )
    {
      sb.append( " property set " ).append( pi.getName() ).append( "( _proxy_arg_value : " ).append( type.getName() ).append( " )\n" );
    }
  }

  private void genProperty( IPropertyInfo pi, StringBuilder sb, IJavaType type )
  {
    if( pi.isPrivate() || pi.isInternal() )
    {
      return;
    }

    if( pi.isStatic() )
    {
      genStaticProperty( pi, sb, type );
    }
    else
    {
      genMemberProperty( pi, sb, type );
    }
  }

  private void genMemberProperty( IPropertyInfo pi, StringBuilder sb, IJavaType type )
  {
    if( pi.isStatic() )
    {
      return;
    }

    if( !(pi instanceof JavaBaseFeatureInfo) )
    {
      // It is possible that a propertyinfo on a java type originates outside of java.
      // E.g., enhancement properties. Gosu does not support extending these.
      return;
    }

    if( Keyword.isReserved( pi.getName() ) && !Keyword.isReservedValue( pi.getName() ) )
    {
      // Sorry these won't compile
      //## todo: handle them reflectively?
      return;
    }

    if( pi instanceof JavaFieldPropertyInfo )
    {
      StringBuilder sbModifiers = appendFieldVisibilityModifier( pi );
      sb.append( "  " ).append( sbModifiers ).append( " var " ).append( pi.getName() ).append( " : " ).append( getGenericType( pi ).getName() ).append( "\n" );
    }
    else
    {
      IMethodInfo mi = getPropertyGetMethod( pi, type );
      boolean bFinal = false;
      if( mi != null )
      {
        int iMethodModifiers = ((IJavaMethodInfo)mi).getModifiers();
        bFinal = java.lang.reflect.Modifier.isFinal( iMethodModifiers );
      }

      if( mi != null && !bFinal )
      {
        if( mi.getDescription() != null )
        {
          sb.append( "/** " ).append( mi.getDescription() ).append( " */" );
        }
        StringBuilder sbModifiers = buildModifiers( mi );
        sb.append( "  " ).append( sbModifiers ).append( "property get " ).append( pi.getName() ).append( "() : " ).append( getGenericType( pi ).getName() ).append( "\n" );
        if( !mi.isAbstract() )
        {
          generateStub( sb, mi.getReturnType() );
        }
      }
      else
      {
        String strJavaClassName = type.getName();
        StringBuilder sbModifiers;
        boolean bAbstact = false;
        if( bFinal )
        {
          bAbstact = mi.isAbstract();
          sbModifiers = buildModifiers( mi );
        }
        else
        {
          sbModifiers = appendVisibilityModifier( pi );
        }
        sb.append( "  " ).append( sbModifiers ).append( "property get " ).append( pi.getName() ).append( "() : " ).append( getGenericType( pi ).getName() ).append( "\n" );
        if( !bAbstact )
        {
          generateStub( sb, getGenericType( pi ) );
        }
      }

      mi = getPropertySetMethod( pi, type );
      bFinal = false;
      if( mi != null )
      {
        int iMethodModifiers = ((IJavaMethodInfo)mi).getModifiers();
        bFinal = java.lang.reflect.Modifier.isFinal( iMethodModifiers );
      }

      if( mi != null && !bFinal )
      {
        StringBuilder sbModifiers = buildModifiers( mi );
        if( pi.isWritable( pi.getOwnersType() ) )
        {
          sb.append( "  " ).append( sbModifiers ).append( "property set " ).append( pi.getName() ).append( "( _proxy_arg_value : " ).append( getGenericType( pi ).getName() ).append( " )\n" );
          if( !mi.isAbstract() )
          {
            generateStub( sb, JavaTypes.pVOID() );
          }
        }
      }
      else
      {
        if( pi.isWritable( type.getEnclosingType() != null ? null : pi.getOwnersType() ) )
        {
          String strJavaClassName = type.getName();
          StringBuilder sbModifiers;
          boolean bAbstact = false;
          if( bFinal )
          {
            bAbstact = mi.isAbstract();
            sbModifiers = buildModifiers( mi );
          }
          else
          {
            sbModifiers = appendVisibilityModifier( pi );
          }
          sb.append( "  " ).append( sbModifiers ).append( "property set " ).append( pi.getName() ).append( "( _proxy_arg_value : " ).append( getGenericType( pi ).getName() ).append( " )\n" );
          if( !bAbstact )
          {
            generateStub( sb, JavaTypes.pVOID() );
          }
        }
      }
    }
  }

  private StringBuilder buildModifiers( IAttributedFeatureInfo fi )
  {
    StringBuilder sbModifiers = new StringBuilder();
    if( fi.isAbstract() )
    {
      sbModifiers.append( Keyword.KW_abstract ).append( " " );
    }
    else if( fi.isFinal() )
    {
      sbModifiers.append( Keyword.KW_final ).append( " " );
    }
    if( fi.isProtected() )
    {
      sbModifiers.append( Keyword.KW_protected ).append( " " );
    }
    else if( fi.isInternal() )
    {
      sbModifiers.append( Keyword.KW_internal ).append( " " );
    }

    return sbModifiers;
  }

  private IMethodInfo getPropertyGetMethod( IPropertyInfo pi, IJavaType ownerType )
  {
    JavaTypeInfo ti;
    if( !(pi.getOwnersType() instanceof IJavaType) )
    {
      return null;
    }
    if( ownerType.getTypeInfo() instanceof JavaTypeInfo )
    {
      ti = (JavaTypeInfo)ownerType.getTypeInfo();
    }
    else
    {
      throw new IllegalArgumentException( ownerType + " is not a recognized Java type" );
    }

    IType propType = pi.getFeatureType();

    String strAccessor = "get" + pi.getDisplayName();
    IMethodInfo mi = ti.getMethod( ownerType, strAccessor );
    if( mi == null || mi.getReturnType() != propType )
    {
      strAccessor = "is" + pi.getDisplayName();
      mi = ti.getMethod( ownerType, strAccessor );
    }
    if( mi != null && mi.getReturnType() == propType )
    {
      return mi;
    }

    return null;
  }

  private IMethodInfo getPropertySetMethod( IPropertyInfo pi, IJavaType ownerType )
  {
    JavaTypeInfo ti;
    if( !(pi.getOwnersType() instanceof IJavaType) )
    {
      return null;
    }
    if( ownerType.getTypeInfo() instanceof JavaTypeInfo )
    {
      ti = (JavaTypeInfo)ownerType.getTypeInfo();
    }
    else
    {
      throw new IllegalArgumentException( ownerType + " is not a recognized Java type" );
    }

    IType propType = pi.getFeatureType();

    // Check for Setter

    String strAccessor = "set" + pi.getDisplayName();
    IMethodInfo mi = ti.getMethod( ownerType, strAccessor, propType );
    if( mi != null && mi.getReturnType() == JavaTypes.pVOID() )
    {
      return mi;
    }

    return null;
  }

  private void genStaticProperty( IPropertyInfo pi, StringBuilder sb, IJavaType type )
  {
    if( !pi.isStatic() )
    {
      return;
    }

    if( !(pi instanceof JavaBaseFeatureInfo) )
    {
      // It is possible that a methodinfo on a java type originates outside of java.
      // E.g., enhancement methods. Gosu does not support extending these.
      return;
    }

    if( Keyword.isReserved( pi.getName() ) )
    {
      // Sorry these won't compile
      //## todo: handle them reflectively?
      return;
    }

    if( pi.getDescription() != null )
    {
      sb.append( "/** " ).append( pi.getDescription() ).append( " */" );
    }
    if( pi instanceof JavaFieldPropertyInfo )
    {
      StringBuilder sbModifiers = appendFieldVisibilityModifier( pi );
      sb.append( "  " ).append( sbModifiers ).append( "static var " ).append( pi.getName() ).append( " : " ).append( getGenericType( pi ).getName() ).append( "\n" );
    }
    else
    {
      StringBuilder sbModifiers = appendVisibilityModifier( pi );
      sb.append( "  " ).append( sbModifiers ).append( "static property get " ).append( pi.getName() ).append( "() : " ).append( getGenericType( pi ).getName() ).append( "\n" )
        .append( "  {\n" )
        .append( "    return " ).append( type.getName() ).append( '.' ).append( pi.getName() ).append( ";\n" )
        .append( "  }\n" );

      if( pi.isWritable( pi.getOwnersType() ) )
      {
        sb
          .append( "  static property set " ).append( pi.getName() ).append( "( _proxy_arg_value : " ).append( getGenericType( pi ).getName() ).append( " )\n" )
          .append( "  {\n" )
          .append( "  " ).append( type.getName() ).append( '.' ).append( pi.getName() ).append( " = _proxy_arg_value;\n" )
          .append( "  }\n" );
      }
    }
  }

  private IType getGenericType( IPropertyInfo pi )
  {
    return (pi instanceof JavaPropertyInfo)
           ? ((JavaPropertyInfo)pi).getGenericIntrinsicType()
           : pi.getFeatureType();
  }

  private IType getGenericReturnType( IMethodInfo mi )
  {
    return (mi instanceof JavaMethodInfo)
           ? ((JavaMethodInfo)mi).getGenericReturnType()
           : mi.getReturnType();
  }

  private IParameterInfo[] getGenericParameters( IMethodInfo mi )
  {
    return (mi instanceof JavaMethodInfo)
           ? ((JavaMethodInfo)mi).getGenericParameters()
           : mi.getParameters();
  }

  private IParameterInfo[] getGenericParameters( IConstructorInfo ci )
  {
    return (ci instanceof JavaConstructorInfo)
           ? ((JavaConstructorInfo)ci).getGenericParameters()
           : ci.getParameters();
  }

  private void appendHashCodeLine( StringBuffer buffer, String varName, IType varType )
  {
    varName = "_" + varName;
    if( "byte".equals( varType.getName() ) )
    {
      buffer.append( "    result = result + " ).append( varName ).append( " as int;\n" );
    }
    else if( "int".equals( varType.getName() ) )
    {
      buffer.append( "    result = result + " ).append( varName ).append( " as int;\n" );
    }
    else if( "short".equals( varType.getName() ) )
    {
      buffer.append( "    result = result + " ).append( varName ).append( " as int;\n" );
    }
    else if( "boolean".equals( varType.getName() ) || "java.lang.Boolean".equals( varType.getName() ) )
    {
      buffer.append( "    result = result + (" ).append( varName ).append( " == true ? 1 : 0);\n" );
    }
    else if( "double".equals( varType.getName() ) )
    {
      buffer.append( "    result = result + " ).append( varName ).append( " as int;\n" );
    }
    else if( "long".equals( varType.getName() ) )
    {
      buffer.append( "    result = result + " ).append( varName ).append( " as int;\n" );
    }
    else if( "float".equals( varType.getName() ) )
    {
      buffer.append( "    result = result + " ).append( varName ).append( " as int;\n" );
    }
    else
    {
      if( varType.isArray() )
      {
        buffer.append( "    result = result + gw.util.GosuObjectUtil.arrayHashCode(" ).append( varName ).append( ");\n" );
      }
      else
      {
        buffer.append( "    result = result + (" ).append( varName ).append( " == null ? 0 : " ).append( varName ).append( ".hashCode());\n" );
      }
    }

  }

  private String getTypeName( Class type )
  {
    return TypeSystem.get( type ).getName();
  }
}
