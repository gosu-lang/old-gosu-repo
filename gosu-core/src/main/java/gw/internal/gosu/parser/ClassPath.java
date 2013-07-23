/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.fs.IDirectory;
import gw.fs.IFile;
import gw.lang.reflect.IDefaultTypeLoader;
import gw.lang.reflect.gs.TypeName;
import gw.lang.reflect.module.IClassPath;
import gw.lang.reflect.module.IModule;
import gw.util.cache.FqnCache;
import gw.util.cache.FqnCacheNode;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ClassPath implements IClassPath
{
  private static final String CLASS_FILE_EXT = ".class";
  private IModule module;
  private ClassPathFilter filter;
  private FqnCache<IFile> cache = new FqnCache<IFile>();

  public ClassPath(IModule module, ClassPathFilter filter)
  {
    this.module = module;
    this.filter = filter;
    loadClasspathInfo();
  }

  public ArrayList<IDirectory> getPaths()
  {
    return new ArrayList<IDirectory>(module.getJavaClassPath());
  }

  public boolean contains(String fqn) {
    return cache.contains(fqn);
  }

  public IFile get( String fqn ) {
    return cache.get( fqn );
  }

  public Set<String> getFilteredClassNames() {
    return cache.getFqns();
  }

  public boolean isEmpty() {
    return cache.getRoot().isLeaf();
  }

  // ====================== PRIVATE ====================================

  private void loadClasspathInfo()
  {
    List<IDirectory> javaClassPath = module.getJavaClassPath();
    IDirectory[] paths = javaClassPath.toArray(new IDirectory[javaClassPath.size()]);
    for (int i = 0; i < paths.length; i++) {
      addClassNames(paths[i], paths[i], filter);
    }
  }

  private void addClassNames(final IDirectory root, IDirectory dir, final ClassPathFilter filter)
  {
    for (IFile file : dir.listFiles()) {
      if( isClassFileName( file.getName() ) )
      {
        String strClassName = getClassNameFromFile( root, file );
        if( isValidClassName( strClassName ) )
        {
          putClassName( file, strClassName, filter );
        }
      }
    }
    for (IDirectory subDir : dir.listDirs()) {
      addClassNames(root, subDir, filter);
    }
  }

  private void putClassName( IFile file, String strClassName, ClassPathFilter filter )
  {
    boolean bFiltered = filter != null && !filter.acceptClass( strClassName );
    if( bFiltered )
    {
      // We need to store packages so we can resolve them in the gosu parser
      strClassName = getPlaceholderClassNameForFilteredPackage( strClassName );
    }
    if( strClassName != null )
    {
      cache.add( strClassName, file );
    }
  }

  static private String getPlaceholderClassNameForFilteredPackage( String strClassName )
  {
    int iIndex = strClassName.lastIndexOf( '.' );
    if( iIndex > 0 )
    {
      return strClassName.substring( 0, iIndex+1 ) + PLACEHOLDER_FOR_PACKAGE;
    }
    return null;
  }

  private String makeClassNameFromFileName( String strFileName )
  {
    int iLength = strFileName.length() - CLASS_FILE_EXT.length();
    char[] classNameChars = new char[iLength];
    strFileName.getChars( 0, iLength, classNameChars, 0 );
    for( int i = 0; i < classNameChars.length; i++ )
    {
      char c = classNameChars[i];
      classNameChars[i] =
        c == File.separatorChar || c == '/' || c == '\\'
        ? '.' : c;
    }
    return new String( classNameChars );
  }

  private String getClassNameFromFile( IDirectory root, IFile file )
  {
    String strQualifiedClassName = root.relativePath(file);
    if( !isClassFileName( strQualifiedClassName ) )
    {
      throw new IllegalArgumentException(
        file.getPath() + " is not a legal Java class name. " +
        "It does not end with " + CLASS_FILE_EXT );
    }
    strQualifiedClassName =
      strQualifiedClassName.substring( 0, strQualifiedClassName.length() -
                                          CLASS_FILE_EXT.length() );
    return strQualifiedClassName.replace('/', '.');
  }

  private boolean isClassFileName( String strFileName )
  {
    return strFileName.toLowerCase().endsWith( ".class" );
  }

  private boolean isValidClassName( String strClassName )
  {
    if (strClassName.endsWith("package-info")) {
      return false;
    }
    // look for private or anonymous inner classes
    int index = strClassName.lastIndexOf('$');
    if (filter.isIgnoreAnonymous() &&
            index >= 0 && index < strClassName.length() - 1 &&
            Character.isDigit(strClassName.charAt(index + 1))) {
      return false;
    }
    return true;
  }

  public boolean hasNamespace(String namespace) {
    FqnCacheNode infoNode = cache.getNode(namespace);
    return infoNode != null && !infoNode.isLeaf();
  }

  @Override
  public Set<TypeName> getTypeNames(String namespace) {
    FqnCacheNode<?> node = cache.getNode(namespace);
    IDefaultTypeLoader defaultTypeLoader = module.getModuleTypeLoader().getDefaultTypeLoader();
    if (node != null) {
      Set<TypeName> names = new HashSet<TypeName>();
      for (FqnCacheNode<?> child : node.getChildren()) {
        if (child.isLeaf()) {
          names.add(new TypeName(namespace + "." + child.getName(), defaultTypeLoader, TypeName.Kind.TYPE, TypeName.Visibility.PUBLIC));
        } else {
          names.add(new TypeName(child.getName(), defaultTypeLoader, TypeName.Kind.NAMESPACE, TypeName.Visibility.PUBLIC));
        }
      }
      return names;
    } else {
      return Collections.emptySet();
    }
  }

  @Override
  public String toString() {
    return module.getName();
  }
}
