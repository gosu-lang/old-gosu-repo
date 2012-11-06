/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.fs.IFile;
import gw.internal.ext.org.antlr.runtime.*;
import gw.internal.ext.org.antlr.runtime.Token;
import gw.internal.gosu.parser.java.JavaLexer;
import gw.lang.parser.Keyword;
import gw.lang.reflect.RefreshRequest;
import gw.lang.reflect.RefreshKind;
import gw.lang.reflect.gs.ClassType;
import gw.lang.reflect.gs.IGosuEnhancement;
import gw.lang.reflect.gs.GosuClassTypeLoader;
import gw.lang.reflect.gs.IEnhancementIndex;
import gw.lang.reflect.IErrorType;
import gw.lang.reflect.gs.IGosuClassRepository;
import gw.lang.reflect.gs.IGenericTypeVariable;
import gw.lang.reflect.gs.ISourceFileHandle;
import gw.lang.parser.CaseInsensitiveCharSequence;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IParameterInfo;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.util.CaseInsensitiveHashMap;
import gw.util.GosuObjectUtil;
import gw.config.CommonServices;
import gw.lang.parser.TypeVarToTypeMap;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 */
public class EnhancementIndex implements IEnhancementIndex
{
  // Controls some local debugging messages that can be used to troubleshoot this here index
  private static final boolean LOCAL_DEBUG = false;

  private GosuClassTypeLoader _loader;
  private Map<String, ArrayList<String>> _typeToEnhancementsMap;
  private ArrayList<String> _arrayEnhancements;

  private boolean _loadingIndex;
  private String _currentEnhName;
  private Set<IType> _currentlyEnhancing;

  EnhancementIndex( GosuClassTypeLoader loader )
  {
    _currentlyEnhancing = new HashSet<IType>();
    _loader = loader;
  }

  public void addEnhancementMethods( IType typeToEnhance, Collection<IMethodInfo> methodsToAddTo )
  {
    maybeLoadEnhancementIndex();
    checkNotIndexing(typeToEnhance);

    if( !(typeToEnhance instanceof IGosuEnhancementInternal) )
    {
      TypeSystem.lock();
      try
      {
        checkAndPushEnhancing( typeToEnhance );
        try
        {
          EnhancementManager enhancementManager = new EnhancementManager(methodsToAddTo );
          enhancementManager.addAllEnhancementMethodsForType(typeToEnhance);

          IType[] interfaces = typeToEnhance.getInterfaces();
          for( IType interfaceType : interfaces )
          {
            enhancementManager.addAllEnhancementMethodsForType(interfaceType);
          }
        }
        finally
        {
          popEnhancing( typeToEnhance );
        }
      }
      finally
      {
        TypeSystem.unlock();
      }
    }
  }

  public void addEnhancementProperties(IType typeToEnhance, Map<CharSequence, IPropertyInfo> propertyInfosToAddTo, boolean caseSensitive)
  {
    maybeLoadEnhancementIndex();
    checkNotIndexing(typeToEnhance);

    if( !(typeToEnhance instanceof IGosuEnhancementInternal) )
    {
      TypeSystem.lock();
      try
      {
        checkAndPushEnhancing( typeToEnhance );
        try {
          EnhancementManager enhancementManager = new EnhancementManager(propertyInfosToAddTo );
          enhancementManager.addAllEnhancementPropsForType(typeToEnhance, caseSensitive);

          IType[] interfaces = typeToEnhance.getInterfaces();
          for( IType interfaceType : interfaces )
          {
            enhancementManager.addAllEnhancementPropsForType(interfaceType, caseSensitive);
          }
        } finally {
          popEnhancing( typeToEnhance );
        }
      }
      finally
      {
        TypeSystem.unlock();
      }
    }
  }

  private void popEnhancing( IType typeToEnhance )
  {
    _currentlyEnhancing.remove(typeToEnhance);
  }

  private void checkAndPushEnhancing( IType typeToEnhance )
  {
    if( _currentlyEnhancing.contains( typeToEnhance ) )
    {
      throw new IllegalStateException( "A recursive loop was found in enhancement definition.  The type " + typeToEnhance +
                                       " attempted to load its enhancements again while loading its enhancments initially.  " +
                                       "This usually indicates a logical error in the Gosu runtime." );
    }
    else
    {
      _currentlyEnhancing.add( typeToEnhance );
    }
  }

  private void checkNotIndexing( IType typeToEnhance )
  {
    if( _loadingIndex )
    {
      throw new IllegalStateException( "There is a circular type reference.  When loading the enhancement defined in " +
                                       _currentEnhName + ", enhancements for " + typeToEnhance.getName() + " were requested.  This" +
                                       " usual occurs when the typeloader of the type enhanced in " + _currentEnhName + " compiles " +
                                       " code.  We do not support enhancements on types that have this behavior." );
    }
  }

  public void maybeLoadEnhancementIndex()
  {
    if( _typeToEnhancementsMap != null )
    {
      return;
    }

    try
    {
      _loadingIndex = true;
      _typeToEnhancementsMap = new CaseInsensitiveHashMap<String, ArrayList<String>>();
      _arrayEnhancements = new ArrayList<String>();

      Set<String> allEnhancements = _loader.getRepository().getAllTypeNames(new String[]{GosuClassTypeLoader.GOSU_ENHANCEMENT_FILE_EXT});

      indexEnhancements(allEnhancements.toArray(new String[allEnhancements.size()]));
    }
    finally
    {
      _loadingIndex = false;
      _currentEnhName = null;
    }
  }

  private void indexEnhancements(String[] enhancementNames) {
    IGosuClassRepository repository = _loader.getRepository();
    for( String enhancementName : enhancementNames )
    {
      _currentEnhName = enhancementName;
      ISourceFileHandle sfh = repository.findClass(enhancementName, GosuClassTypeLoader.ALL_EXTS);
      if (sfh != null && sfh.getClassType() == ClassType.Enhancement && sfh.getParentType() == null) {
        String enhancedTypeName = parseEnhancedTypeName(sfh);
        if( enhancedTypeName != null && !IErrorType.NAME.equals( enhancedTypeName ) )
        {
          ArrayList<String> enhancements = getEnhancementIndexForType( enhancedTypeName );
          if (!enhancements.contains(enhancementName)) {
            enhancements.add(enhancementName);
          }
        }
      } else {
        // remove the enhancement
      }
    }
  }

  public static String parseEnhancedTypeName(ISourceFileHandle src) {
//    long t1 = System.nanoTime();
    try {
      CharStream cs;
      if (src.getFilePath() != null && new File(src.getFilePath()).exists()) {
        File file = new File(src.getFilePath());
        IFile iFile = CommonServices.getFileSystem().getIFile(file);
        InputStream inputStream = iFile.openInputStream();
        cs = new ANTLRInputStream(new BufferedInputStream(inputStream, 1024));
      } else {
        cs = new ANTLRStringStream(src.getSource().getSource());
      }
      JavaLexer lexer = new JavaLexer(cs);

      Token token = lexer.nextToken();
      while (not(token, Keyword.KW_enhancement.getName())) {
        token = lexer.nextToken();
      }
      while (not(token, src.getRelativeName())) {
        token = lexer.nextToken();
      }
      while (not(token, ":")) {
        token = lexer.nextToken();
      }

      StringBuilder name = new StringBuilder();
      token = lexer.nextToken();
      do {
        name.append(token.getText());
        token = lexer.nextToken();
      } while (not(token, "{") && not(token, "<"));

      return clean(name);
    } catch (IOException e) {
      return IErrorType.NAME;
    } finally {
//      System.out.println("  parseEnhancedTypeName " + src + " - " + (System.nanoTime() - t1)*1e-6);
    }
  }

  private static String clean(StringBuilder name) {
    for (int i = 0; i < name.length(); ) {
      if (Character.isWhitespace(name.charAt(i))) {
        name.delete(i, i + 1);
      } else {
        i++;
      }
    }
    return name.toString();
  }

  private static boolean not(Token token, String text) throws IOException {
    if (token.getType() == JavaLexer.EOF) {
      throw new IOException("EOF");
    }
    return !token.getText().equals(text);
  }

  private ArrayList<String> getEnhancementIndexForType( String strEnhancedTypeName )
  {
    maybeLoadEnhancementIndex();

    if( strEnhancedTypeName.contains( "[" ) )
    {
      return _arrayEnhancements;
    }
    else
    {
      ArrayList<String> enhancements = _typeToEnhancementsMap.get( strEnhancedTypeName );
      if( enhancements == null )
      {
        enhancements = new ArrayList<String>();
        _typeToEnhancementsMap.put( strEnhancedTypeName, enhancements );
      }
      return enhancements;
    }
  }

  public List<IGosuEnhancementInternal> getEnhancementsForType( IType typeToEnhance ) {
    maybeLoadEnhancementIndex();

    IType genericEnhancedType = TypeLord.getPureGenericType(typeToEnhance);
    Set<String> possibleEnhancementNames = getPossibleEnhancementsForTypeFromIndex( genericEnhancedType );
    ArrayList<IGosuEnhancementInternal> enhancements = new ArrayList<IGosuEnhancementInternal>();
    for( String possibleEnhancementTypeName : possibleEnhancementNames )
    {
      try
      {
        IType enhancementType = TypeLoaderAccess.instance().getIntrinsicTypeByFullName( possibleEnhancementTypeName );
        if( enhancementType instanceof IGosuEnhancementInternal )
        {
          IGosuEnhancementInternal possibleEnhancement = (IGosuEnhancementInternal)enhancementType;
          if (!possibleEnhancement.isCompilingHeader()) {
            IType typeEnhanced = possibleEnhancement.getEnhancedType();
            TypeVarToTypeMap actualParamByVarName = TypeLord.mapTypeByVarName( possibleEnhancement, possibleEnhancement );
            typeEnhanced = typeEnhanced == null ? null : TypeLord.getActualType( typeEnhanced, actualParamByVarName, false );
            if( enhancementApplies( typeEnhanced, typeToEnhance, true) )
            {
              enhancements.add( possibleEnhancement );
            }
          } else {
            throw new RuntimeException("Theoretically, should not be able to get here....so much for theory ");
          }
        }
      }
      catch( ClassNotFoundException e )
      {
        // CommonServices.getEntityAccess().getLogger().warn( "Unable to add enhancement " + possibleEnhancementTypeName + " because the corresponding class was not found." );
      }
    }

    return enhancements;
  }

  @Override
  public void refreshedTypes(RefreshRequest request) {
    if (request.kind == RefreshKind.CREATION) {
      indexEnhancements(request.types);
    } else if (request.kind == RefreshKind.DELETION) {
      for (String enhancementName : request.types) {
        removeEnhancement(enhancementName);
      }
    } else if (request.kind == RefreshKind.MODIFICATION) {
      indexEnhancements(request.types);
    }
  }

  public String getOrphanedEnhancement(String typeName) {
    String name = typeName.substring(typeName.lastIndexOf('.') + 1);
    ArrayList<String> enhancementNames = getEnhancementIndexForType(name);
    for (String enhancement : enhancementNames) {
      return enhancement;
    }
    return null;
  }

  private boolean enhancementApplies( IType typeEnhanced, IType typeToEnhance, boolean exact )
  {
    if( typeEnhanced == null )
    {
      return false;
    }
    else if( typeEnhanced instanceof IGosuEnhancementInternal )
    {
      //Do not enhance enhancements
      return false;
    }
    else if( hasErrorTypeComponent( typeEnhanced ) )
    {
      //Do not enhance if there is an error component to the enhancement type
      return false;
    }
    else if( typeEnhanced.isArray() && typeToEnhance.isArray() )
    {
      IType enhancementComponent = typeEnhanced.getComponentType();
      IType enhancedComponent = typeToEnhance.getComponentType();
      return enhancedComponent.isPrimitive() == enhancementComponent.isPrimitive() &&
             enhancementApplies( enhancementComponent, enhancedComponent, exact );
    }
    else
    {
      if (exact) {
      return typeEnhanced.isAssignableFrom( typeToEnhance );
      } else {
        return TypeLord.getPureGenericType(typeEnhanced).isAssignableFrom( TypeLord.getPureGenericType(typeToEnhance) );
      }
    }
  }

  private boolean hasErrorTypeComponent(IType enhancedType) {
    if (enhancedType instanceof IErrorType) {
      return true;
    }
    if (enhancedType.isParameterizedType()) {
      IType[] typeParam = enhancedType.getTypeParameters();
      for (IType parameter : typeParam) {
        if (hasErrorTypeComponent(parameter)) {
          return true;
        }
      }
    }
    if( enhancedType.isArray() )
    {
      return hasErrorTypeComponent( enhancedType.getComponentType() );
    }
    return false;
  }


  Set<String> getPossibleEnhancementsForTypeFromIndex( IType typeToGetEnhancementsFor )
  {
    String typeToEnhanceName = typeToGetEnhancementsFor.getName();
    StringBuilder possibleName = new StringBuilder();
    Set<String> enhancements = new TreeSet<String>();
    int currPos = typeToEnhanceName.length();
    do {
      int nextDot = typeToEnhanceName.lastIndexOf('.', currPos-1);
      String string = typeToEnhanceName.substring(nextDot == -1 ? 0 : nextDot+1, currPos);
      currPos = nextDot;
      possibleName.insert( 0, string );

      ArrayList<String> enhancementTypes;
      if( typeToGetEnhancementsFor.isArray() )
      {
        enhancementTypes = _arrayEnhancements;
      }
      else
      {
        enhancementTypes = _typeToEnhancementsMap.get( possibleName.toString() );
      }

      if( enhancementTypes != null )
      {
        enhancements.addAll( enhancementTypes );
      }
      possibleName.insert( 0, "." );
    } while (currPos != -1);
    return enhancements;
  }

  public void removeEntry( IGosuEnhancement enhancement )
  {
    removeEnhancement(enhancement.getName());
  }

  public void removeEnhancement(String enhancementName)
  {
    if (_typeToEnhancementsMap != null) {
      for( Map.Entry<String, ArrayList<String>> entry : _typeToEnhancementsMap.entrySet())
      {
        ArrayList<String> value = entry.getValue();
        if( value.remove( enhancementName ) )
        {
          break;
        }
      }
    }
  }

  public void addEntry( IType enhancedType, IGosuEnhancement enhancement )
  {
    getEnhancementIndexForType( enhancedType.getName() ).add( enhancement.getName() );
  }

  /**
   * A helper class that holds some data structures that we build up to manage method addition/replacement
   */
  private class EnhancementManager
  {

    private Collection<IMethodInfo> _methodsToAddTo;
    private Map<CharSequence, IPropertyInfo> _propertyInfosToAddTo;
    private Map<String, List<IMethodInfo>> _methodNamesToMethods;

    public EnhancementManager(Collection<IMethodInfo> methodsToAddTo)
    {
      _methodsToAddTo = methodsToAddTo;
    }

    public EnhancementManager(Map<CharSequence, IPropertyInfo> propertyInfosToAddTo)
    {
      _propertyInfosToAddTo = propertyInfosToAddTo;
    }

    public void addAllEnhancementMethodsForType( IType typeToGetEnhancementsFor )
    {
      List<IGosuEnhancementInternal> enhancements = getEnhancementsForType( typeToGetEnhancementsFor );

      for( IGosuEnhancementInternal type : enhancements )
      {
        GosuClassTypeInfo typeInfo = getTypeInfoForType( typeToGetEnhancementsFor, type );
        List extensionMethods = typeInfo.getDeclaredMethods();
        for( Object extensionMethod : extensionMethods )
        {
          GosuMethodInfo extensionMethodInfo = (GosuMethodInfo)extensionMethod;
          if( shouldAddMethod( extensionMethodInfo, type, typeToGetEnhancementsFor ) )
          {
            _methodsToAddTo.add( extensionMethodInfo );
          }
        }
      }
    }

    public void addAllEnhancementPropsForType(IType typeToGetEnhancementsFor, boolean caseSensitive)
    {
      List<IGosuEnhancementInternal> enhancements = getEnhancementsForType( typeToGetEnhancementsFor );

      for( IGosuEnhancementInternal type : enhancements )
      {

        GosuClassTypeInfo typeInfo = getTypeInfoForType( typeToGetEnhancementsFor, type );
        List<? extends IPropertyInfo> props = typeInfo.getDeclaredProperties();
        for( IPropertyInfo enhancementPropertyInfo : props )
        {
          if( typeToGetEnhancementsFor.isArray() ||
              (!enhancementPropertyInfo.isPrivate() &&
               (!enhancementPropertyInfo.isInternal() || type.getNamespace().equals( typeToGetEnhancementsFor.getNamespace() ))) )
          {
            IPropertyInfo existingPropInfo = _propertyInfosToAddTo.get(convertCharSequenceToCorrectSensitivity(enhancementPropertyInfo.getName(), caseSensitive) );

            //if a property exists that is not associated with this
            if( existingPropInfo != null &&
                !GosuObjectUtil.equals( existingPropInfo.getContainer(), enhancementPropertyInfo.getContainer() ) &&
                notAnInternalOrPrivateField( existingPropInfo ) )
            {

            }
            else
            {
              _propertyInfosToAddTo.put( convertCharSequenceToCorrectSensitivity(enhancementPropertyInfo.getName(), caseSensitive), enhancementPropertyInfo );
            }
          }
        }
      }
    }

    private boolean notAnInternalOrPrivateField( IPropertyInfo existingPropInfo )
    {
      // Allow shadowing of protected or private fields on Java classes
      return !(existingPropInfo instanceof JavaFieldPropertyInfo) || (existingPropInfo.isPublic() || existingPropInfo.isProtected());
    }

    private GosuClassTypeInfo getTypeInfoForType( IType typeToGetEnhancementsFor, IGosuEnhancementInternal enhancementType )
    {
      if( enhancementType.isGenericType() && typeToGetEnhancementsFor.isParameterizedType()
          || typeToGetEnhancementsFor.isArray() )
      {
        enhancementType = parameterizeEnhancement( enhancementType, typeToGetEnhancementsFor );
      }
      else if (enhancementType.isGenericType() && typeToGetEnhancementsFor.isArray() )
      {
        //## todo: I think we can delete the else-if because it is subsumed by the previous if
        enhancementType = (IGosuEnhancementInternal) enhancementType.getParameterizedType( typeToGetEnhancementsFor.getComponentType() );
      }
      return (GosuClassTypeInfo) enhancementType.getTypeInfo();
    }

    private IGosuEnhancementInternal parameterizeEnhancement( IGosuEnhancementInternal enhancementType, IType typeToGetEnhancementsFor )
    {
      if( typeToGetEnhancementsFor.isArray() )
      {
        typeToGetEnhancementsFor = typeToGetEnhancementsFor.getComponentType().isPrimitive()
                                   ? TypeLord.getBoxedTypeFromPrimitiveType( typeToGetEnhancementsFor.getComponentType() ).getArrayType()
                                   : typeToGetEnhancementsFor;
        IType genericEnhancedType = TypeLord.getPureGenericType( enhancementType.getEnhancedType() );
        TypeVarToTypeMap typeVars = new TypeVarToTypeMap();
        TypeLord.inferTypeVariableTypesFromGenParamTypeAndConcreteType( genericEnhancedType, typeToGetEnhancementsFor, typeVars );
        if( typeVars.size() > 0 )
        {
          return (IGosuEnhancementInternal)enhancementType.getParameterizedType( (IType[])typeVars.values().toArray( new IType[typeVars.values().size()] ) );
        }
        else
        {
          return enhancementType;
        }
      }
      else
      {
        IType genericEnhancedType = TypeLord.getPureGenericType( enhancementType.getEnhancedType() );
        IType parameterizedEnhancedType = TypeLord.findParameterizedType( typeToGetEnhancementsFor, genericEnhancedType );
        TypeVarToTypeMap map = new TypeVarToTypeMap();
        for( IGenericTypeVariable gtv : enhancementType.getGenericTypeVariables() )
        {
          map.put( gtv.getTypeVariableDefinition().getType(), null );
        }
        IGenericTypeVariable[] gtvs = enhancementType.getEnhancedType().getGenericTypeVariables();
        if( gtvs != null )
        {
          for( int i = 0; i < gtvs.length; i++ )
          {
            IGenericTypeVariable gtv = gtvs[i];
            TypeLord.inferTypeVariableTypesFromGenParamTypeAndConcreteType( gtv.getTypeVariableDefinition().getType(),
                                                                                parameterizedEnhancedType.getTypeParameters()[i],
                                                                                map );
          }
        }
        // Bound any types that cannot be inferred via the relationship with the enhanced type
        for( Object key : new ArrayList<Object>( map.keySet() ) )
        {
          if( map.getRaw( key ) == null )
          {
            for( IGenericTypeVariable gtv : enhancementType.getGenericTypeVariables() )
            {
              if( TypeVarToTypeMap.looseEquals( gtv.getTypeVariableDefinition().getType(), key ) )
              {
                map.put( gtv.getTypeVariableDefinition().getType(), gtv.getBoundingType() );
                break;
              }
            }
          }
        }

        if( map.size() > 0 )
        {
          List<IType> typeParamList = makeOrderedTypeParams( map, enhancementType );
          return (IGosuEnhancementInternal)enhancementType.getParameterizedType( typeParamList.toArray( new IType[typeParamList.size()] ) );
        }

        return enhancementType;
      }
    }

    private List<IType> makeOrderedTypeParams( TypeVarToTypeMap map, IGosuEnhancementInternal enhancementType )
    {
      List<IType> typeParams = new ArrayList<IType>();
      for( IGenericTypeVariable gtv : enhancementType.getGenericTypeVariables() )
      {
        typeParams.add( map.getRaw( gtv.getTypeVariableDefinition().getType() ) );
      }
      return typeParams;
    }

    //builds up a map of method names to method info collections
    private Map<String, List<IMethodInfo>> createMethodMap( Collection<IMethodInfo> methodsToAddTo )
    {
      Map<String, List<IMethodInfo>> returnMap = new HashMap<String, List<IMethodInfo>>();
      for( IMethodInfo methodInfo : methodsToAddTo )
      {
        List<IMethodInfo> list = returnMap.get( methodInfo.getDisplayName() );
        if( list == null )
        {
          list = new ArrayList<IMethodInfo>();
          returnMap.put( methodInfo.getDisplayName(), list );
        }
        list.add( methodInfo );
      }
      return returnMap;
    }

    private boolean shouldAddMethod(IMethodInfo enhancementMethodInfo, IGosuEnhancement enhancementType, IType typeToGetEnhancementsFor )
    {
      if (_methodNamesToMethods == null) {
        _methodNamesToMethods = createMethodMap( _methodsToAddTo );
      }
      IParameterInfo[] extensionParams = enhancementMethodInfo.getParameters();
      List<IMethodInfo> matchingMethods = _methodNamesToMethods.get( enhancementMethodInfo.getDisplayName() );

      if( !typeToGetEnhancementsFor.isArray() &&
          (enhancementMethodInfo.isPrivate() ||
           (enhancementMethodInfo.isInternal() && !enhancementType.getNamespace().equals( typeToGetEnhancementsFor.getNamespace() ))) )
      {
        return false;
      }

      if( matchingMethods != null )
      {
        //for each method with a matching name
        for( IMethodInfo methodInfo : matchingMethods )
        {
          IParameterInfo[] methodParams = methodInfo.getParameters();

          if( methodParams.length == extensionParams.length )
          {
            if( paramTypesEqual( methodParams, extensionParams ) )
            {
              //do not add the method because either it is a bad extension that conflicts with an existing method or
              //it has already been added by a superclass
              return false;
            }
          }
        }
      }

      //Good to go, add the method
      return true;
    }

    private boolean paramTypesEqual( IParameterInfo[] methodParams, IParameterInfo[] extensionParams )
    {
      for( int i = 0; i < methodParams.length; i++ )
      {
        if( !GosuObjectUtil.equals( methodParams[i].getFeatureType(), extensionParams[i].getFeatureType() ) )
        {
          return false;
        }
      }
      return true;
    }

  }

  private static CharSequence convertCharSequenceToCorrectSensitivity(CharSequence cs, boolean caseSensitive) {
    return caseSensitive ? cs.toString() : CaseInsensitiveCharSequence.get(cs);
  }

  @Override
  public String toString() {
    return "Enhancements Index for " + _loader.toString();
  }
}
