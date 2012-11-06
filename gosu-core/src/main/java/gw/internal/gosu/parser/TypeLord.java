/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.internal.gosu.parser.types.FunctionLiteralType;
import gw.lang.parser.*;
import gw.lang.parser.expressions.ITypeLiteralExpression;
import gw.lang.parser.expressions.ITypeVariableDefinition;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.reflect.FunctionType;
import gw.lang.reflect.IErrorType;
import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.IMetaType;
import gw.lang.reflect.INonLoadableType;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeLoader;
import gw.lang.reflect.ITypeVariableArrayType;
import gw.lang.reflect.ITypeVariableType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGenericTypeVariable;
import gw.lang.reflect.gs.IGosuProgram;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.module.IModule;
import gw.util.Pair;
import gw.util.GosuObjectUtil;
import gw.util.concurrent.Cache;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

/**
 */
public class TypeLord
{
  // LRUish cache of assignability results (recent tests indicate 99% hit rates)
  public static final TypeSystemAwareCache<Pair<IType, IType>, Boolean> ASSIGNABILITY_CACHE =
    TypeSystemAwareCache.make( "Assignability Cache", 1000,
                               new Cache.MissHandler<Pair<IType, IType>, Boolean>()
                               {
                                 public final Boolean load( Pair<IType, IType> key )
                                 {
                                   return areGenericOrParameterizedTypesAssignableInternal( key.getFirst(), key.getSecond() );
                                 }
                               } );

  public static Set<IType> getAllClassesInClassHierarchyAsIntrinsicTypes( IJavaClassInfo cls )
  {
    Set<IJavaClassInfo> classSet = new HashSet<IJavaClassInfo>();
    addAllClassesInClassHierarchy( cls, classSet );

    Set<IType> intrinsicTypeSet = new HashSet<IType>();
    intrinsicTypeSet.add(JavaTypes.OBJECT());
    for(IJavaClassInfo classInfo : classSet) {
      intrinsicTypeSet.add( classInfo.getJavaType() );
    }

    return intrinsicTypeSet;
  }

  public static Set<IType> getAllClassesInClassHierarchyAsIntrinsicTypes( IType type )
  {
    HashSet<IType> typeSet = new HashSet<IType>();
    addAllClassesInClassHierarchy( type, typeSet );
    return typeSet;
  }

  public static boolean encloses( IType type, IType inner )
  {
    return inner != null && (inner.getEnclosingType() == type || encloses( type, inner.getEnclosingType() ));
  }

  public static boolean enclosingTypeInstanceInScope( IType type, IGosuClassInternal inner )
  {
    return inner != null && !inner.isStatic() &&
           ((type != null && type.isAssignableFrom( inner.getEnclosingType() )) ||
            enclosingTypeInstanceInScope( type, (IGosuClassInternal)inner.getEnclosingType() ));
  }                                

  public static Set<IType> getArrayVersionsOfEachType( Set componentTypes )
  {
    Set<IType> allTypes = new HashSet<IType>();
    allTypes.add( JavaTypes.OBJECT() );
    Iterator it = componentTypes.iterator();
    while( it.hasNext() )
    {
      IType type = (IType)it.next();
      allTypes.add( type.getArrayType() );
    }

    return allTypes;
  }

  public static IType getActualType( Type type, TypeVarToTypeMap actualParamByVarName )
  {
    return getActualType( type, actualParamByVarName, false );
  }

  public static IType getActualType( Type type, TypeVarToTypeMap actualParamByVarName, boolean bKeepTypeVars )
  {
    if( type instanceof Class )
    {
      return TypeSystem.get( (Class)type );
    }
    if( type instanceof TypeVariable )
    {
      return actualParamByVarName.getByString( ((TypeVariable)type).getName() );
    }
    return parseType( normalizeJavaTypeName( type ), actualParamByVarName, bKeepTypeVars, null );
  }

  private static String normalizeJavaTypeName( Type type )
  {
    if( type instanceof Class )
    {
      IType itype = TypeSystem.get( (Class<?>)type );
      if ( itype == null )
      {
        throw new RuntimeException( "Class " + type + " is not loadable through the TypeSystem!" );
      }
      return itype.getName();
    }
    if( type instanceof TypeVariable )
    {
      return ((TypeVariable)type).getName();
    }
    if( type instanceof ParameterizedType )
    {
      ParameterizedType ptype = (ParameterizedType)type;
      String strName = normalizeJavaTypeName( ptype.getRawType() );
      strName += "<";
      boolean bFirst = true;
      for( Type param : ptype.getActualTypeArguments() )
      {
        if( !bFirst )
        {
          strName += ", ";
        }
        bFirst = false;
        strName += normalizeJavaTypeName( param );
      }
      strName += ">";
      return strName;
    }
    return fixSunInnerClassBug( type.toString() );
  }

  public static IType getActualType( IType type, TypeVarToTypeMap actualParamByVarName )
  {
    return getActualType( type, actualParamByVarName, false );
  }

  public static IType getActualType( IType type, TypeVarToTypeMap actualParamByVarName, boolean bKeepTypeVars )
  {
    int iArrayDims = 0;
    if( type != null && type.isArray() )
    {
      for( iArrayDims = 0; type.isArray(); iArrayDims++ )
      {
        type = type.getComponentType();
      }
    }

    if( type instanceof TypeVariableType )
    {
      TypeVariableType saveType = (TypeVariableType)type;
      type = actualParamByVarName.get( (ITypeVariableType)type );
      if( type == null )
      {
        if( bKeepTypeVars )
        {
          type = saveType;
        }
      }
      else
      {
        if( !isParameterizedWith( type, saveType ) )
        {
          type = getActualType( type, actualParamByVarName, bKeepTypeVars );
        }
        else if( !bKeepTypeVars )
        {
          type = getDefaultParameterizedTypeWithTypeVars( type );
        }
      }
    }
    else if( type instanceof FunctionType)
    {
      if( !(type instanceof ErrorTypeInfo.UniversalFunctionType) )
      {
        type = ((FunctionType)type).parameterize( (FunctionType)type, actualParamByVarName, bKeepTypeVars );
      }
    }
    else if( type != null && type.isParameterizedType() )
    {
      IType[] typeParams = type.getTypeParameters();
      IType[] actualParamTypes = new IType[typeParams.length];
      for( int i = 0; i < typeParams.length; i++ )
      {
        IType actualType = getActualType(typeParams[i], actualParamByVarName, bKeepTypeVars);
        if (actualType == null) {
          actualType = JavaTypes.OBJECT();
        }
        actualParamTypes[i] = actualType;
      }
      type = TypeLord.getPureGenericType( type ).getParameterizedType( actualParamTypes );
    }

    if( iArrayDims > 0 && type != null )
    {
      for( int j = 0; j < iArrayDims; j++ )
      {
        type = type.getArrayType();
      }
    }

    return type;
  }

  private static boolean isParameterizedWith( IType type, TypeVariableType typeVar )
  {
    type = getCoreType( type );

    if( type.equals( typeVar ) )
    {
      return true;
    }

    if( type instanceof FunctionType )
    {
      IFunctionType funType = (IFunctionType)type;
      IType[] types = funType.getParameterTypes();
      for( IType param : types )
      {
        if( isParameterizedWith( param, typeVar ) )
        {
          return true;
        }
      }
      return isParameterizedWith( funType.getReturnType(), typeVar );
    }
    else if( type.isParameterizedType() )
    {
      for( IType typeParam : type.getTypeParameters() )
      {
        if( isParameterizedWith( typeParam, typeVar ) )
        {
          return true;
        }
      }
    }

    return false;
  }

  public static IType parseType( String strParameterizedTypeName, TypeVarToTypeMap actualParamByVarName )
  {
    return parseType( strParameterizedTypeName, actualParamByVarName, null );
  }
  public static IType parseType( String strParameterizedTypeName, TypeVarToTypeMap actualParamByVarName, ITypeUsesMap typeUsesMap )
  {
    return parseType( strParameterizedTypeName, actualParamByVarName, false, typeUsesMap );
  }

  private static IType parseType( String strParameterizedTypeName, TypeVarToTypeMap actualParamByVarName, boolean bKeepTypeVars, ITypeUsesMap typeUsesMap )
  {
    try
    {
      ITypeLiteralExpression expression = parseTypeLiteral( strParameterizedTypeName, actualParamByVarName, bKeepTypeVars, typeUsesMap );
      return expression.getType().getType();
    }
    catch( ParseResultsException e )
    {
      throw new RuntimeException( "Unable to parse the type " + strParameterizedTypeName + ".  When attempting to parse this " +
                                  "as a type literal, a parse error occured.", e );
    }
  }

  public static ITypeLiteralExpression parseTypeLiteral( String strParameterizedTypeName, TypeVarToTypeMap actualParamByVarName,
                                                         boolean bKeepTypeVars, ITypeUsesMap typeUsesMap) throws ParseResultsException
  {
    StringTokenizer tokenizer = new StringTokenizer( strParameterizedTypeName, " <>[]?:(),", true );
    StringBuilder sbType = new StringBuilder();
    while( tokenizer.hasMoreTokens() )
    {
      String strToken = tokenizer.nextToken();
      IType type = actualParamByVarName.getByString( strToken );

      String resolvedTypeName;
      if( type != null )
      {
        if (type.isParameterizedType()) {
          type = resolveParameterizedType( type, actualParamByVarName, bKeepTypeVars );
        }
        if( type instanceof TypeVariableType )
        {
          type = bKeepTypeVars ? type : ((TypeVariableType)type).getBoundingType();
        }
        if (type instanceof FunctionLiteralType) {
          type = type.getSupertype();
        }
        
        resolvedTypeName = type instanceof TypeVariableType ? type.getRelativeName() : type.getName();
      }
      else
      {
        resolvedTypeName = strToken;
      }

      boolean bFirstToken = sbType.length() == 0;
      if( bFirstToken )
      {
        if( "?".equals( resolvedTypeName ) )
        {
          resolvedTypeName = JavaTypes.OBJECT().getName();
        }
      }
      sbType.append( resolvedTypeName );
    }

    String strNormalizedType = sbType.toString().replace( "$", "." );
    GosuParser parser = (GosuParser)GosuParserFactory.createParser( strNormalizedType, new StandardSymbolTable(), ScriptabilityModifiers.SCRIPTABLE );
    if (typeUsesMap != null) {
      parser.setTypeUsesMap(typeUsesMap);
    }
    parser.setIgnoreTypeDeprecation(true);
    if( bKeepTypeVars )
    {
      addTypeVars( actualParamByVarName, parser );
    }
    return parser.parseTypeLiteral( null );
  }

  private static IType resolveParameterizedType( IType parameterizedType, TypeVarToTypeMap actualParamByVarName, boolean bKeepTypeVars )
  {
    List<IType> resolvedParams = new ArrayList<IType>();
    for( IType paramType : parameterizedType.getTypeParameters() )
    {
      if( paramType instanceof TypeVariableType && actualParamByVarName.containsKey( (ITypeVariableType)paramType ) )
      {
        if( !bKeepTypeVars )
        {
          resolvedParams.add( actualParamByVarName.get( (ITypeVariableType)paramType ) );
        }
        else
        {
          resolvedParams.add( paramType );
        }
      }
      else
      {
        if( paramType.isParameterizedType() )
        {
          resolvedParams.add( resolveParameterizedType( paramType, actualParamByVarName, bKeepTypeVars ) );
        }
        else
        {
          resolvedParams.add( paramType );
        }
      }
    }
    return parameterizedType.getGenericType().getParameterizedType( resolvedParams.toArray( new IType[resolvedParams.size()] ) );
  }

  private static void addTypeVars( TypeVarToTypeMap types, IGosuParser parser )
  {
    for( Object passedInTvKey : types.keySet() )
    {
      IType type = types.getRaw( passedInTvKey );
      if( type instanceof TypeVariableType )
      {
        ITypeVariableDefinition existingTv = parser.getTypeVariables().get( type.getName() );
        if( existingTv == null || TypeVarToTypeMap.looseEquals( passedInTvKey, existingTv.getType() ) )
        {
          parser.getTypeVariables().put( type.getRelativeName(), ((TypeVariableType)type).getTypeVarDef() );
        }
      }
      else if( type.isParameterizedType() )
      {
        TypeVarToTypeMap map = new TypeVarToTypeMap();
        for( int i = 0; i < type.getTypeParameters().length; i++ )
        {
          IType t = type.getTypeParameters()[i];
          map.putRaw( String.valueOf( i ), t );
        }
        addTypeVars( map, parser );
      }
    }
  }

  public static TypeVarToTypeMap mapTypeByVarName( IType ownersType, IType declaringType )
  {
    return mapTypeByVarName( ownersType, declaringType, false );
  }

  public static TypeVarToTypeMap mapTypeByVarName( IType ownersType, IType declaringType, boolean bKeepTypeVars )
  {
    TypeVarToTypeMap actualParamByVarName;
    ownersType = findActualDeclaringType( ownersType, declaringType );
    if( ownersType != null && ownersType.isParameterizedType() )
    {
      actualParamByVarName = mapActualTypeByVarName( ownersType );
    }
    else
    {
      actualParamByVarName = mapGenericTypeByVarName( ownersType, bKeepTypeVars );
    }
    return actualParamByVarName;
  }

  // If the declaring type is generic and the owning type is parameterized, we need to
  // find the corresponding parameterized type of the declaring type e.g.
  //
  // class Base<T> {
  //   function blah() : Bar<T> {}
  // }
  // class Foo<T> extends Base<T> {}
  //
  // new Foo<String>().blah() // infer return type as Bar<String>
  //
  // The declaring class of blah() is generic class Base<T> (not a parameterized one),
  // while the owner's type is Foo<String>, thus in order to resolve the actual return
  // type for blah() we must walk the ancestry of Foo<String> until find the corresponding
  // parameterized type for Base<T>.
  private static IType findActualDeclaringType( IType ownersType, IType declaringType )
  {
    if( ownersType == null )
    {
      return null;
    }

    if( declaringType.isParameterizedType() && !declaringType.isGenericType() )
    {
      return declaringType;
    }

    if( ownersType == declaringType )
    {
      return declaringType;
    }

    if( ownersType.getGenericType() == declaringType )
    {
      return ownersType;
    }

    IType actualDeclaringType = findActualDeclaringType( ownersType.getSupertype(), declaringType );
    if( actualDeclaringType != null && actualDeclaringType != declaringType )
    {
      return actualDeclaringType;
    }

    for( IType iface : ownersType.getInterfaces() )
    {
      actualDeclaringType = findActualDeclaringType( iface, declaringType );
      if( actualDeclaringType != null && actualDeclaringType != declaringType )
      {
        return actualDeclaringType;
      }
    }

    return declaringType;
  }

  private static TypeVarToTypeMap mapActualTypeByVarName( IType ownersType )
  {
    TypeVarToTypeMap actualParamByVarName = new TypeVarToTypeMap();
    IGenericTypeVariable[] vars = ownersType.getGenericType().getGenericTypeVariables();
    if (vars != null) {
      IType[] paramArgs = ownersType.getTypeParameters();
      for( int i = 0; i < vars.length; i++ )
      {
        IGenericTypeVariable typeVar = vars[i];
        if( paramArgs.length > i )
        {
          actualParamByVarName.put( typeVar.getTypeVariableDefinition().getType(), paramArgs[i] );
        }
      }
    }
    return actualParamByVarName;
  }

  private static TypeVarToTypeMap mapGenericTypeByVarName( IType ownersType, boolean bKeepTypeVars )
  {
    TypeVarToTypeMap genericParamByVarName = TypeVarToTypeMap.EMPTY_MAP;
    IType genType = null;
    if( ownersType != null )
    {
      genType = ownersType.getGenericType();
    }
    if( genType != null )
    {
      genericParamByVarName = new TypeVarToTypeMap();
      IGenericTypeVariable[] vars = genType.getGenericTypeVariables();
      if( vars != null )
      {
        for( int i = 0; i < vars.length; i++ )
        {
          IGenericTypeVariable typeVar = vars[i];
          Object key = typeVar.getTypeVariableDefinition() == null ? typeVar.getName() : typeVar.getTypeVariableDefinition().getType();
          if( !genericParamByVarName.containsKeyRaw( key ) )
          {
            genericParamByVarName.putRaw( key, bKeepTypeVars
                                               ? new TypeVariableType( ownersType, typeVar )
                                               : typeVar.getBoundingType() );
          }
        }
      }
    }
    return genericParamByVarName;
  }

  public static String getNameWithQualifiedTypeVariables( IType type )
  {
    if( type.isParameterizedType() )
    {
      String strParams = getNameOfParams( type.getTypeParameters(), false, true );
      return getPureGenericType( type ).getName() + strParams;
    }
    else if( type instanceof TypeVariableType )
    {
      if( type.getEnclosingType() != null )
      {
        return  ((TypeVariableType)type).getNameWithEnclosingType();
      }
    }
    return type.getName();
  }

  public static String getNameOfParams( IType[] paramTypes, boolean bRelative, boolean bWithEnclosingType )
  {
    return getNameOfParams(paramTypes, bRelative, bWithEnclosingType, false);
  }
  public static String getNameOfParams( IType[] paramTypes, boolean bRelative, boolean bWithEnclosingType, boolean bIncludeModule )
  {
    StringBuilder sb = new StringBuilder( "<" );
    for( int i = 0; i < paramTypes.length; i++ )
    {
      IType paramType = paramTypes[i];
      if( bRelative )
      {
        sb.append( paramType.getRelativeName() );
      }
      else
      {
        if( bWithEnclosingType && paramType instanceof TypeVariableType )
        {
          TypeVariableType type = (TypeVariableType)paramType;
          if( type.getEnclosingType() != null )
          {
            if( bIncludeModule && !(type.getEnclosingType() instanceof INonLoadableType) )
            {
              sb.append( type.getEnclosingType().getTypeLoader().getModule().getName() + "." );
            }
            sb.append( type.getNameWithEnclosingType() );
          }
          else
          {
            sb.append( type.getName() );
          }
        }
        else if( bWithEnclosingType && paramType.isParameterizedType() )
        {
          sb.append( paramType.getGenericType().getName() );
          sb.append( getNameOfParams( paramType.getTypeParameters(), bRelative, bWithEnclosingType, bIncludeModule ) );
        }
        else
        {
          if( bIncludeModule && !(paramType instanceof INonLoadableType) )
          {
            ITypeLoader typeLoader = paramType.getTypeLoader();
            if (typeLoader != null) {
              IModule oldModule = typeLoader.getModule();
              if (oldModule != null) {
                sb.append( oldModule.getName() + "." );
              }
            }
          }
          sb.append( paramType.getName() );
        }
      }
      if( i < paramTypes.length - 1 )
      {
        sb.append( ", " );
      }
    }
    sb.append( '>' );
    return sb.toString();
  }

  /**
   * Finds a parameterized type in the ancestry of a given type. For instance,
   * given the type for ArrayList&lt;Person&gt; as the sourceType and List as
   * the rawGenericType, returns List&lt;Person&gt;.
   *
   * @param sourceType     The type to search in.
   * @param rawGenericType The raw generic type of the parameterized type to
   *                       search for e.g., List is the raw generic type of List&lt;String&gt;.
   *
   * @return A parameterization of rawGenericType corresponding with the type
   *         params of sourceType.
   */
  public static IType findParameterizedType( IType sourceType, IType rawGenericType )
  {
    if( sourceType == null )
    {
      return null;
    }

    rawGenericType = getPureGenericType( rawGenericType );

    if( //sourceType.isParameterizedType() &&
        sourceType.getGenericType() == rawGenericType )
    {
      return sourceType;
    }

    IType parameterizedType = findParameterizedType( sourceType.getSupertype(), rawGenericType );
    if( parameterizedType != null )
    {
      return parameterizedType;
    }

    IType[] interfaces = sourceType.getInterfaces();
    for (int i = 0; i < interfaces.length; i++) {
      IType iface = interfaces[i];
      parameterizedType = findParameterizedType( iface, rawGenericType );
      if( parameterizedType != null )
      {
        return parameterizedType;
      }
    }

    return null;
  }

  // Todo: the above method is nearly identical to this one. lets see about combining them
  public static IType findParameterizedTypeInHierarchy( IType sourceType, IType rawGenericType )
  {
    if( sourceType == null )
    {
      return null;
    }

    if( sourceType.isParameterizedType() && sourceType.getGenericType().equals( rawGenericType ) )
    {
      return sourceType;
    }

    IType[] list = sourceType.getInterfaces();
    for( int i = 0; i < list.length; i++ )
    {
      IType returnType = findParameterizedTypeInHierarchy( list[i], rawGenericType );
      if( returnType != null )
      {
        return returnType;
      }
    }

    return findParameterizedTypeInHierarchy( sourceType.getSupertype(), rawGenericType );
  }

  public static void addAllClassesInClassHierarchy( Class entityClass, Set<Class> set )
  {
    if( !set.add( entityClass ) )
    {
      return;
    }
    
    Class[] interfaces = entityClass.getInterfaces();
    for( int i = 0; i < interfaces.length; i++ )
    {
      addAllClassesInClassHierarchy( interfaces[i], set );
    }
    
    if( entityClass.getSuperclass() != null )
    {
      addAllClassesInClassHierarchy( entityClass.getSuperclass(), set );
    }
  }
  
  private static void addAllClassesInClassHierarchy( IJavaClassInfo entityClass, Set<IJavaClassInfo> set )
  {
    if( !set.add( entityClass ) )
    {
      return;
    }

    IJavaClassInfo[] interfaces = entityClass.getInterfaces();
    for( int i = 0; i < interfaces.length; i++ )
    {
      addAllClassesInClassHierarchy( interfaces[i], set );
    }

    if( entityClass.getSuperclass() != null )
    {
      addAllClassesInClassHierarchy( entityClass.getSuperclass(), set );
    }
  }

  public static void addAllClassesInClassHierarchy( IType type, Set<IType> set )
  {
    addAllClassesInClassHierarchy( type, set, false );
  }

  public static void addAllClassesInClassHierarchy( IType type, Set<IType> set, boolean bForce )
  {
    if( !set.add( type  ) && !bForce )
    {
      return;
    }

    for( IType iface : type.getInterfaces() )
    {
      addAllClassesInClassHierarchy( iface, set );
    }

    if( type.getSupertype() != null )
    {
      addAllClassesInClassHierarchy( type.getSupertype(), set );
    }

    if( type.isParameterizedType() )
    {
      addAllClassesInClassHierarchy( type.getGenericType(), set );
    }
  }

  public static <E extends IType> E getPureGenericType( E type )
  {
    if ( type == null ) {
      return type;
    }

    while( type.isParameterizedType() )
    {
      //noinspection unchecked
      type = (E)type.getGenericType();
    }
    return type;
  }

  public static IType makeDefaultParameterizedType( IType type )
  {
    return makeDefaultParameterizedType( type, false );
  }
  public static IType makeDefaultParameterizedType( IType type, boolean bHandleRecursiveParameterizedType )
  {
    if( type != null && !(type instanceof IGosuEnhancementInternal) &&
        type.isGenericType() &&
        (!type.isParameterizedType() ||
         (bHandleRecursiveParameterizedType && isRecursiveType( type, type.getTypeParameters() ))) )
    {
      if( type instanceof MetaType )
      {
        return MetaType.DEFAULT_TYPE_TYPE.get();
      }

      IType[] boundingTypes = new IType[type.getGenericTypeVariables().length];
      for( int i = 0; i < boundingTypes.length; i++ )
      {
        boundingTypes[i] = type.getGenericTypeVariables()[i].getBoundingType();
        if( bHandleRecursiveParameterizedType &&
            boundingTypes[i].isParameterizedType() &&
            isRecursiveType( boundingTypes[i], boundingTypes[i].getTypeParameters() ) )
        {
          boundingTypes[i] = getDefaultParameterizedType( boundingTypes[i] );
        }
      }

      if( boundingTypes.length == 0 ||
          TypeLord.isRecursiveType( type, boundingTypes ) )
      {
        return type;
      }

      type = type.getParameterizedType( boundingTypes );
    }
    return type;
  }

  public static IType replaceTypeVariableTypeParametersWithBoundingTypes( IType type )
  {
    if( type instanceof ITypeVariableType )
    {
      return replaceTypeVariableTypeParametersWithBoundingTypes( ((ITypeVariableType)type).getBoundingType() );
    }

    if( type.isArray() )
    {
      return replaceTypeVariableTypeParametersWithBoundingTypes( type.getComponentType() ).getArrayType();
    }

    if( type.isParameterizedType() )
    {
      IType[] typeParams = type.getTypeParameters();
      if( TypeLord.isRecursiveType( type, typeParams ) )
      {
        return type;
      }
      IType[] concreteParams = new IType[typeParams.length];
      for( int i = 0; i < typeParams.length; i++ )
      {
        concreteParams[i] = replaceTypeVariableTypeParametersWithBoundingTypes( typeParams[i] );
      }
      type = type.getParameterizedType( concreteParams );
    }
    else
    {
      if( type.isGenericType() )
      {
        IType[] boundingTypes = new IType[type.getGenericTypeVariables().length];
        for( int i = 0; i < boundingTypes.length; i++ )
        {
          boundingTypes[i] = type.getGenericTypeVariables()[i].getBoundingType();
        }
        if( TypeLord.isRecursiveType( type, boundingTypes ) )
        {
          return type;
        }
        for( int i = 0; i < boundingTypes.length; i++ )
        {
          boundingTypes[i] = replaceTypeVariableTypeParametersWithBoundingTypes( boundingTypes[i] );
        }
        type = type.getParameterizedType( boundingTypes );
      }
    }
    return type;
  }

  public static IType getDefaultParameterizedType( IType type )
  {
    if( !type.isGenericType() && !type.isParameterizedType() )
    {
      return type;
    }
    type = getPureGenericType( type );
    return makeDefaultParameterizedType( type );
  }

  public static IType getDefaultParameterizedTypeWithTypeVars( IType type )
  {
    if( type instanceof ITypeVariableType )
    {
      return getDefaultParameterizedTypeWithTypeVars( ((ITypeVariableType)type).getBoundingType() );
    }

    if( type instanceof ITypeVariableArrayType )
    {
      return getDefaultParameterizedTypeWithTypeVars( ((ITypeVariableType)type.getComponentType()).getBoundingType() ).getArrayType();
    }

    if( !type.isGenericType() && !type.isParameterizedType() )
    {
      return type;
    }
    type = getPureGenericType( type );
    return makeDefaultParameterizedType( type );
  }

  public static boolean isSubtype( IType subtype, IType supertype )
  {
    if( subtype == null )
    {
      return false;
    }

    // Make sure we're dealing with pure types before doing any checks
    subtype = getPureGenericType( subtype );
    supertype = getPureGenericType( supertype );
    if( supertype instanceof IGosuClassInternal )
    {
      if( ((IGosuClassInternal)supertype).isSubClass( subtype ) )
      {
        return true;
      }
    }

    IType st = getPureGenericType( subtype.getSupertype() );
    return st == supertype || isSubtype( st, supertype );
  }

  static String fixSunInnerClassBug( String type )
  {

    int i = type.indexOf( "$", 0 );

    if( i < 0 )
    {
      return type;
    }
    else
    {

      int n = 0;
      i = 0;
      StringBuilder sb = new StringBuilder( type );

      while( i >= 0 )
      {
        i = findNthPositionOfString( n, sb, "$" );
        removeDuplicateClassName( sb, i );
        n++;
      }

      return sb.toString();
    }
  }

  private static int findNthPositionOfString( int n, StringBuilder sb, String str )
  {
    int count = 0;
    int i = 0;
    while( count <= n )
    {
      i = sb.indexOf( str, i + 1 );
      count++;
    }
    return i;
  }


  static void removeDuplicateClassName( StringBuilder sb, int dollarSignPosition )
  {
    StringBuilder foundBuffer = new StringBuilder();
    boolean chopped = false;
    int start = dollarSignPosition - 1;

    while( start >= 0 )
    {
      foundBuffer.append( sb.charAt( start ) );
      if( repeatsWithDot( foundBuffer ) && !chopped )
      {
        foundBuffer.setLength( foundBuffer.length() / 2 );
        chopped = true;
        break;
      }
      start--;
    }

    if( chopped )
    {
      sb.replace( start, dollarSignPosition, foundBuffer.reverse().toString() );
    }
  }

  static boolean repeatsWithDot( StringBuilder sb )
  {
    if( sb == null || sb.length() % 2 == 0 )
    {
      return false;
    }

    int halfPoint = sb.length() / 2;

    if( sb.charAt( halfPoint ) != '.' )
    {
      return false;
    }

    for( int i = 0; i < halfPoint; i++ )
    {
      if( sb.charAt( i ) != sb.charAt( i + halfPoint + 1 ) )
      {
        return false;
      }
    }
    return true;
  }

  public static boolean areGenericOrParameterizedTypesAssignable( IType to, IType from ) {
    return ASSIGNABILITY_CACHE.get(Pair.make(to, from));
  }

  private static boolean areGenericOrParameterizedTypesAssignableInternal( IType to, IType from )
  {
    if( from instanceof CompoundType )
    {
      Set<IType> types = ((CompoundType)from).getTypes();
      for( IType type : types )
      {
        if( areGenericOrParameterizedTypesAssignable( to, type ) )
        {
          return true;
        }
      }
      return false;
    }
    else if( to.isParameterizedType() || to.isGenericType() )
    {
      IType relatedParameterizedType = findParameterizedType( from, to.getGenericType() );
      if( relatedParameterizedType == null )
      {
        if( from == to.getGenericType() )
        {
          // Handle case List<Object> assignable from List
          relatedParameterizedType = from;
        }
        else
        {
          return false;
        }
      }

      if( from.isGenericType() &&
          to.isParameterizedType() &&
          ((!from.isParameterizedType() &&
            (to == makeDefaultParameterizedType( from ) ||
             // Handle recursive type Foo where we want Foo<Foo> assignable from Foo
             isRecursiveType( getPureGenericType( to ), to.getTypeParameters() ) && getPureGenericType( to ) == from)) ||
           sameAsDefaultProxiedType( to, from )) )
      {
        //## todo: this is a hack so that we can assign a generic type to its default parameterized type e.g.,
        // var listOfObj = List<Object>; var list = List; listOfObj = list;
        //
        // Why support this? Primarily because type information coming from Java and other types
        // may have generic types e.g., a java class with method, List getMyList(), is exposed
        // with a return type of List, not List<Object>. Since an unparamaterized Gosu type
        // literal is parsed as its default parameterized type (List parses as List<Object>) we
        // would otherwise have a lot of potential unintentional errors.

        return true;
      }

      IType[] paramTypesFrom = relatedParameterizedType.getTypeParameters();
      IType[] typeParams = to.getTypeParameters();
      if( typeParams != null )
      {
        for( int i = 0; i < typeParams.length; i++ )
        {
          IType paramType = typeParams[i];

//## Leaving this commented out instead of deleting it to prevent it from coming back.
//        if( paramType instanceof TypeVariableType )
//        {
//          paramType = ((TypeVariableType)paramType).getTypeVarDef().getTypeVar().getBoundingTypes()[0];
//        }

          if( paramType != JavaTypes.OBJECT() &&
              (paramTypesFrom == null || paramTypesFrom.length <= i ||
               !paramType.isAssignableFrom( paramTypesFrom[i] )) )
          {
            return false;
          }
        }
      }
      return true;
    }
    return false;
  }

  private static boolean sameAsDefaultProxiedType( IType to, IType from )
  {
    if( to instanceof IJavaTypeInternal &&
        from instanceof IGosuClassInternal &&
        ((IGosuClassInternal)from).isProxy() )
    {
      IJavaType proxiedType = ((IGosuClassInternal)from).getJavaType();
      return proxiedType != null &&
             (TypeLord.makeDefaultParameterizedType( proxiedType ) == to ||
              TypeLord.replaceTypeVariableTypeParametersWithBoundingTypes( proxiedType ) == to);
    }
    return false;
  }

  public static Set<String> getNamespacesFromTypeNames( Set<? extends CharSequence> typeNames, Set<String> namespaces )
  {
    for( CharSequence typeName : typeNames )
    {
      String strName = typeName.toString();
      int iIndex = strName.lastIndexOf( '.' );
      if( iIndex > 0 )
      {
        String strPossibleEnclosingTypeName = strName.substring( 0, iIndex );
        if( typeNames.contains( strPossibleEnclosingTypeName ) )
        {
          // Don't add the enclosing type of an inner class as a namespace
          continue;
        }
      }
      addNamespace( namespaces, strName );
    }
    return namespaces;
  }

  public static void addNamespace( Set<String> namespaces, String strType )
  {
    int iIndex = strType.lastIndexOf( '.' );
    if( iIndex > 0 )
    {
      String strPackage = strType.substring( 0, iIndex );
      if( namespaces.add( strPackage ) )
      {
        // Add parent namespaces i.e. a namespace may not have a class
        // e.g. the java namespace has no direct classes, so we have to
        // add it here.
        addNamespace( namespaces, strPackage );
      }
    }
  }

  public static IType getRootType(IType type) {
    IType result = type;
    while (result.getSupertype() != null || result.getSupertype() != JavaTypes.OBJECT()) {
      result = result.getSupertype();
    }
    return result;
  }

  public static IType findLeastUpperBound( List<? extends IType> types )
  {
    return findLeastUpperBoundImpl( types, new HashSet<IType>() );
  }

  private static IType findLeastUpperBoundImpl( List<? extends IType> types, Set<IType> resolvingTypes )
  {
    // Optimization 1: if there are no types, return void
    if( types.size() == 0 )
    {
      return JavaTypes.pVOID();
    }
    // Optimization 2: if there is one type, return that type
    else if( types.size() == 1 )
    {
      return types.get( 0 );
    }

    // Optimization 3: if there is only one type in the list, return that type
    IType type = null;
    boolean disJointTypes = false;
    boolean foundOnlyNullTypes = true;
    for( Iterator<? extends IType> it = types.iterator(); it.hasNext(); )
    {
      IType iIntrinsicType = it.next();
      //nuke null types, which don't contribute to the type
      if( iIntrinsicType.equals( GosuParserTypes.NULL_TYPE() ) )
      {
        continue;
      }
      foundOnlyNullTypes = false;
      if( type == null )
      {
        type = iIntrinsicType;
      }
      if( !type.equals( iIntrinsicType ) &&
          !BeanAccess.isBoxedTypeFor( type, iIntrinsicType ) &&
          !BeanAccess.isBoxedTypeFor( iIntrinsicType, type ) )
      {
        disJointTypes = true;
        break;
      }
    }
    if( foundOnlyNullTypes )
    {
      return GosuParserTypes.NULL_TYPE();
    }
    if( !disJointTypes )
    {
      return type;
    }

    // Short circuit recursive LUBs
    for( IType iType : types )
    {
      if( resolvingTypes.contains( iType ) )
      {
        return JavaTypes.OBJECT();
      }
    }
    resolvingTypes.addAll( types );

    // OK, we have disjoint types, so we need to do the full-monty LUB analysis
    IType seedType = types.get( 0 );

    Set<IType> lubSet = new HashSet<IType>( seedType.getAllTypesInHierarchy() );
    for( int i = 1; i < types.size(); i++ )
    {
      IType iIntrinsicType = types.get( i );
      lubSet.retainAll( iIntrinsicType.getAllTypesInHierarchy() );
    }

    pruneNonLUBs( lubSet );

    if( lubSet.size() == 0 )
    {
      /* If there is no common types, return Object */
      return JavaTypes.OBJECT();
    }

    lubSet = findParameterizationLUBS( types, lubSet, resolvingTypes );

    if( lubSet.size() == 1 )
    {
      /* If there is a single, unabiguous LUB type, return that */
      return lubSet.iterator().next();
    }
    else
    {
      return CompoundType.get( lubSet );
    }
  }

  private static Set<IType> findParameterizationLUBS( List<? extends IType> currentTypes, Set<IType> lubSet, Set<IType> resolvingTypes )
  {
    Set<IType> returnSet = new HashSet<IType>();
    for( IType lub : lubSet )
    {
      if( lub.isGenericType() && !lub.isParameterizedType() && currentTypes.size() > 1 )
      {
        ArrayList<List<IType>> typeParametersByPosition = new ArrayList<List<IType>>( lub.getGenericTypeVariables().length );
        for( int i = 0; i < lub.getGenericTypeVariables().length; i++ )
        {
          typeParametersByPosition.add( new ArrayList<IType>() );
        }
        for( IType initialType : currentTypes )
        {
          IType parameterizedType = findParameterizedType( initialType, lub );
          if (parameterizedType == null) {
            System.out.println("*** ERROR: parameteredType is null");
            System.out.println("*** initialType is " + initialType);
            System.out.println("*** lub is " + lub);
            for (IType t : currentTypes) {
              System.out.println("*** currentTypes contains " + t);
            }
            for (IType t : lubSet) {
              System.out.println("*** lubSet contains " + t);
            }
          }
          if( !parameterizedType.isParameterizedType() )
          {
            parameterizedType = getDefaultParameterizedType( initialType );
          }
          if( parameterizedType.isParameterizedType() )
          {
            for( int i = 0; i < parameterizedType.getTypeParameters().length; i++ )
            {
              IType parameter = parameterizedType.getTypeParameters()[i];
              typeParametersByPosition.get( i ).add( parameter );
            }
          }
        }
        ArrayList<IType> paramLubs = new ArrayList<IType>();
        for( List<IType> paramterTypesAtPositionI : typeParametersByPosition )
        {
          IType leastUpperBound = findLeastUpperBoundImpl( paramterTypesAtPositionI, resolvingTypes );
          paramLubs.add( leastUpperBound );
        }
        IType finalType = lub.getParameterizedType( paramLubs.toArray( new IType[paramLubs.size()] ) );
        returnSet.add( finalType );
      }
      else
      {
        returnSet.add( lub );
      }
    }
    return returnSet;
  }

  private static void pruneNonLUBs( Set<IType> typeSet )
  {
    for( Iterator<IType> outerIterator = typeSet.iterator(); outerIterator.hasNext(); )
    {
      IType typeToPossiblyRemove = outerIterator.next();
      for( IType otherType : typeSet )
      {
        //noinspection SuspiciousMethodCalls
        if( otherType.getAllTypesInHierarchy().contains( typeToPossiblyRemove ) &&
            !typeToPossiblyRemove.getAllTypesInHierarchy().contains( otherType ) )
        {
          outerIterator.remove();
          break;
        }
      }
    }
  }

  public static boolean isRecursiveType( IJavaType javaType )
  {
    return getDefaultParameterizedType( javaType ).isGenericType();
  }

  public static boolean isRecursiveType( IType genericType, IType[] paramTypes )
  {
    for( IType paramType : paramTypes )
    {
      if( getPureGenericType( paramType ).equals( getPureGenericType( genericType ) ) )
      {
        // Short-circuit recursive type parameterization e.g., class Foo<T extends Foo<T>>
        return true;
      }
      else if( paramType.isParameterizedType() )
      {
        if( isRecursiveType( genericType, paramType.getTypeParameters() ) )
        {
          return true;
        }
      }
      else if( paramType instanceof TypeVariableType )
      {
        if( genericType.isGenericType() )
        {
          if( isRecursiveType( paramType, new IType[] {((TypeVariableType)paramType).getBoundingType()} ) )
          {
            return true;
          }
        }
        if( isRecursiveType( genericType, new IType[] {((TypeVariableType)paramType).getBoundingType()} ) )
        {
          return true;
        }
      }
      else if( paramType.isArray() )
      {
        if( isRecursiveType( genericType, new IType[] {paramType.getComponentType()} ) )
        {
          return true;
        }
      }
    }
    return false;
  }

  public static IJavaClassInfo getOuterMostEnclosingClass(IJavaClassInfo innerClass) {
    IJavaClassInfo outerMost = innerClass;
    while( outerMost.getEnclosingType() != null )
    {
      outerMost = outerMost.getEnclosingClass();
    }
    return outerMost;
  }

  public static IType getOuterMostEnclosingClass( IType innerClass )
  {
    IType outerMost = innerClass;
    while( outerMost.getEnclosingType() != null && !isEvalProgram( outerMost ) )
    {
      outerMost = outerMost.getEnclosingType();
      if (TypeSystem.isDeleted(outerMost)) {
        return null;
      }
    }
    return outerMost;
  }

  public static boolean isEvalProgram( IType type )
  {
    return (type instanceof IGosuProgram) && ((IGosuProgram)type).isAnonymous();
  }

  public static void addReferencedTypeVarsThatAreNotInMap( IType type, TypeVarToTypeMap map, boolean bKeepTypeVars )
  {
    if( type instanceof TypeVariableType )
    {
      IType existingType = map.get( (TypeVariableType)type );
      if( existingType == null )
      {
        if( bKeepTypeVars )
        {
          map.put( (ITypeVariableType)type, type );
        }
        else
        {
          map.put( (ITypeVariableType)type, ((TypeVariableType)type).getBoundingType() );
        }
      }
    }
    else if( type.isParameterizedType() )
    {
      for( IType typeParam : type.getTypeParameters() )
      {
        addReferencedTypeVarsThatAreNotInMap( typeParam, map, bKeepTypeVars );
      }
    }
    else if( type.isArray() )
    {
      addReferencedTypeVarsThatAreNotInMap( type.getComponentType(), map, bKeepTypeVars );
    }
    else if( type instanceof IFunctionType )
    {
      IFunctionType funType = (IFunctionType)type;
      IType[] types = funType.getParameterTypes();
      for( IType iType : types )
      {
        addReferencedTypeVarsThatAreNotInMap( iType, map, bKeepTypeVars );
      }
      addReferencedTypeVarsThatAreNotInMap( funType.getReturnType(), map, bKeepTypeVars );
    }
  }

  public static boolean hasTypeVariable( IType type )
  {
    if( type == null )
    {
      return false;
    }
    else if( type instanceof TypeVariableType )
    {
      return true;
    }
    else if( type instanceof TypeVariableArrayType )
    {
      return true;
    }
    else if( type.isParameterizedType() )
    {
      IType[] compileTimeTypeParams = type.getTypeParameters();
      for( int i = 0; i < compileTimeTypeParams.length; i++ )
      {
        IType ctTypeParam = compileTimeTypeParams[i];
        if( hasTypeVariable( ctTypeParam ) )
        {
          return true;
        }
      }
    }
    else if( type instanceof FunctionType )
    {
      IFunctionType funType = (IFunctionType)type;
      IType[] types = funType.getParameterTypes();
      for( IType param : types )
      {
        if( hasTypeVariable( param ) )
        {
          return true;
        }
      }
      if( hasTypeVariable( funType.getReturnType() ) )
      {
        return true;
      }
    }

    return false;
  }

  public static boolean isExpandable( IType type )
  {
    return getExpandableComponentType( type ) != null;
  }

  public static IType getExpandableComponentType( IType type )
  {
    IType retType = null;
    if( type.isArray() )
    {
      retType = type.getComponentType();
    }
    else
    {
      IType parameterized = TypeLord.findParameterizedType( type, JavaTypes.ITERABLE() );
      if( parameterized != null && parameterized.isParameterizedType() )
      {
        retType = parameterized.getTypeParameters()[0];
      }
      else
      {
        parameterized = TypeLord.findParameterizedType( type, JavaTypes.ITERATOR() );
        if( parameterized != null && parameterized.isParameterizedType() )
        {
          retType = parameterized.getTypeParameters()[0];
        }
      }
    }

    return retType;
  }

  /**
   */
  public static void inferTypeVariableTypesFromGenParamTypeAndConcreteType( IType genParamType, IType argType, TypeVarToTypeMap inferenceMap )
  {
    if( argType == GosuParserTypes.NULL_TYPE() ||
        argType instanceof IErrorType ||
        (argType instanceof IMetaType && ((IMetaType)argType).getType() instanceof IErrorType) )
    {
      return;
    }

    if( argType.isPrimitive() )
    {
      argType = getBoxedTypeFromPrimitiveType( argType );
    }

    if( genParamType.isArray() )
    {
      if ( !argType.isArray()) {
        return;
      }
      //## todo: DON'T allow a null component type here; we do it now as a hack that enables gosu arrays to be compatible with java arrays
      //## todo: same as JavaMethodInfo.inferTypeVariableTypesFromGenParamTypeAndConcreteType()
      if( argType.getComponentType() == null || !argType.getComponentType().isPrimitive() )
      {
        inferTypeVariableTypesFromGenParamTypeAndConcreteType( genParamType.getComponentType(), argType.getComponentType(), inferenceMap );
      }
    }
    else if( genParamType.isParameterizedType() )
    {
      IType argTypeInTermsOfParamType = findParameterizedType( argType, genParamType.getGenericType() );
      if( argTypeInTermsOfParamType == null )
      {
        return;
      }
      IType[] concreteTypeParams = argTypeInTermsOfParamType.getTypeParameters();
      if( concreteTypeParams != null && concreteTypeParams.length > 0 )
      {
        int i = 0;
        for( IType typeArg : genParamType.getTypeParameters() )
        {
          inferTypeVariableTypesFromGenParamTypeAndConcreteType( typeArg, concreteTypeParams[i++], inferenceMap );
        }
      }
    }
    else if( genParamType instanceof ITypeVariableType &&
             argType != GosuParserTypes.NULL_TYPE() )
    {
      ITypeVariableType tvType = ((ITypeVariableType)genParamType).getTypeVarDef().getType();
      IType type = inferenceMap.get( tvType );
      if( type == null || type instanceof ITypeVariableType )
      {
        // Infer the type
        inferenceMap.put( tvType, argType );
      }
      IType boundingType = ((ITypeVariableType)genParamType).getBoundingType();
      if( !isRecursiveType( genParamType, new IType[]{boundingType} ) )
      {
        inferTypeVariableTypesFromGenParamTypeAndConcreteType( boundingType, argType, inferenceMap );
      }
    }
    else if( genParamType instanceof FunctionType )
    {
      FunctionType genBlockType = (FunctionType)genParamType;
      if( !(argType instanceof FunctionType) )
      {
        // argType may not be symetric with getParamType if the enclosing expr is errant
        return;
      }
      inferTypeVariableTypesFromGenParamTypeAndConcreteType(
        genBlockType.getReturnType(), ((FunctionType)argType).getReturnType(), inferenceMap );

      IType[] genBlockParamTypes = genBlockType.getParameterTypes();
      if( genBlockParamTypes != null )
      {
        IType[] argTypeParamTypes = ((FunctionType)argType).getParameterTypes();
        if( argTypeParamTypes.length == genBlockParamTypes.length )
        {
          for( int i = 0; i < genBlockParamTypes.length; i++ )
          {
            inferTypeVariableTypesFromGenParamTypeAndConcreteType(
              genBlockParamTypes[i], ((FunctionType)argType).getParameterTypes()[i], inferenceMap );
          }
        }
      }
    }
  }

  public static IType getConcreteType( IType type )
  {
    if( type.isGenericType() && !type.isParameterizedType() )
    {
      IGenericTypeVariable[] genTypeVars = type.getGenericTypeVariables();
      IType[] typeVarTypes = new IType[genTypeVars.length];
      for( int i = 0; i < typeVarTypes.length; i++ )
      {
        typeVarTypes[i] = genTypeVars[i].getTypeVariableDefinition().getType();
      }
      type = type.getParameterizedType( typeVarTypes );
    }
    return type;
  }

  public static IType getCoreType( IType type )
  {
    if (TypeSystem.isDeleted(type)) {
      return null;
    }
    if( type.isArray() )
    {
      return getCoreType( type.getComponentType() );
    }
    return type;
  }

  public static IType getBoxedTypeFromPrimitiveType( IType primitiveType )
  {
    IType boxedType;

    if( primitiveType == JavaTypes.pBOOLEAN() )
    {
      boxedType = JavaTypes.BOOLEAN();
    }
    else if( primitiveType == JavaTypes.pBYTE() )
    {
      boxedType = JavaTypes.BYTE();
    }
    else if( primitiveType == JavaTypes.pCHAR() )
    {
      boxedType = JavaTypes.CHARACTER();
    }
    else if( primitiveType == JavaTypes.pSHORT() )
    {
      boxedType = JavaTypes.SHORT();
    }
    else if( primitiveType == JavaTypes.pINT() )
    {
      boxedType = JavaTypes.INTEGER();
    }
    else if( primitiveType == JavaTypes.pLONG() )
    {
      boxedType = JavaTypes.LONG();
    }
    else if( primitiveType == JavaTypes.pFLOAT() )
    {
      boxedType = JavaTypes.FLOAT();
    }
    else if( primitiveType == JavaTypes.pDOUBLE() )
    {
      boxedType = JavaTypes.DOUBLE();
    }
    else if( primitiveType == JavaTypes.pVOID() )
    {
      boxedType = JavaTypes.VOID();
    }
    else
    {
      throw new IllegalArgumentException( "Unhandled type " + primitiveType );
    }
    return boxedType;
  }

  public static IType boundTypes( IType type, List<IType> typesToBound )
  {
    if( type == null )
    {
      return null;
    }
    else if( type instanceof ITypeVariableType && inferringType(type, typesToBound) )
    {
      return boundTypes( ((ITypeVariableType)type).getBoundingType(), typesToBound );
    }
    else if( type instanceof ITypeVariableArrayType )
    {
      IType componentType = type.getComponentType();
      return boundTypes( componentType, typesToBound ).getArrayType();
    }
    else if( type.isParameterizedType() )
    {
      IType[] parameters = type.getTypeParameters().clone();
      for( int i = 0; i < parameters.length; i++ )
      {
        parameters[i] = boundTypes( parameters[i], typesToBound );
      }
      return type.getGenericType().getParameterizedType( parameters );
    }
    else if( type instanceof IFunctionType )
    {
      IFunctionType funType = (IFunctionType)type;
      IType[] paramTypes = funType.getParameterTypes().clone();
      for( int i = 0; i < paramTypes.length; i++ )
      {
        paramTypes[i] = boundTypes( paramTypes[i], typesToBound );
      }
      IType returnType = boundTypes( funType.getReturnType(), typesToBound );
      return funType.newInstance( paramTypes, returnType );
    }
    else
    {
      return type;
    }
  }

  private static boolean inferringType( IType type, List<IType> currentlyInferringTypes )
  {
    if( type instanceof TypeVariableType )
    {
      for( IType currentlyInferringType : currentlyInferringTypes )
      {
        TypeVariableType typeVarType = (TypeVariableType)type;
        TypeVariableType inferringTypeVarType = (TypeVariableType)currentlyInferringType;
        if( areTypeVariablesEquivalent( typeVarType, inferringTypeVarType ) )
        {
          return true;
        }
      }
    }
    return false;
  }

  private static boolean areTypeVariablesEquivalent( TypeVariableType possible, TypeVariableType inferred )
  {
    boolean match = false;
    if( GosuObjectUtil.equals( possible.getName(), inferred.getName() ) )
    {
      IType enclosingType1 = possible.getEnclosingType();
      IType enclosingType2 = inferred.getEnclosingType();
      if( enclosingType1 instanceof IFunctionType && enclosingType2 instanceof IFunctionType )
      {
        IFunctionType funType1 = (IFunctionType)enclosingType1;
        IFunctionType funType2 = (IFunctionType)enclosingType2;
        IScriptPartId id1 = funType1.getScriptPart();
        IScriptPartId id2 = funType2.getScriptPart();
        String typeName1 = id1 == null ? null : id1.getContainingTypeName();
        String typeName2 = id2 == null ? null : id2.getContainingTypeName();
        if( GosuObjectUtil.equals( typeName1, typeName2 ) &&
            GosuObjectUtil.equals( funType1.getParamSignature(), funType2.getParamSignature() ) )
        {
          match = true;
        }
      }
    }
    return match;
  }

  public static IType getTopLevelType(IType type) {
    IType topType = getCoreType(type);
    topType = getPureGenericType(topType);
    topType = getOuterMostEnclosingClass(topType);
    return topType;
  }
}
