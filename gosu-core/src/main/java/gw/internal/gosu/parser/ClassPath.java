/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.config.CommonServices;
import gw.fs.IDirectory;
import gw.fs.IFile;
import gw.internal.gosu.module.DefaultSingleModule;
import gw.lang.reflect.IDefaultTypeLoader;
import gw.lang.reflect.gs.TypeName;
import gw.lang.reflect.module.IClassPath;
import gw.lang.reflect.module.IModule;
import gw.util.cache.FqnCache;
import gw.util.cache.FqnCacheNode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ClassPath implements IClassPath
{
  private static final String CLASS_FILE_EXT = ".class";
  private IModule module;
  private ClassPathFilter filter;
  private FqnCache<Object> cache = new FqnCache<Object>();

  public ClassPath(IModule module, ClassPathFilter filter)
  {
    this.module = module;
    this.filter = filter;
    loadClasspathInfo();
  }

  public ClassPath(IModule module)
  {
    this(module, ClassPath.ALLOW_ALL_FILTER);
  }

  public ArrayList<String> getPaths()
  {
    return new ArrayList<String>(module.getJavaClassPath());
  }

  public boolean contains(String fqn) {
    return cache.contains(fqn);
  }

  public Set<String> getFilteredClassNames() {
    return cache.getFqns();
  }

  // ====================== PRIVATE ====================================

  private void loadClasspathInfo()
  {
      if (module instanceof DefaultSingleModule) {
        loadTheOldWay();
      } else {
        loadTheNewWay();
      }
  }

  private void loadTheOldWay() {
    ArrayList<String> rawJavaClasspath = ((DefaultSingleModule)module).getRawJavaClasspath();
    for( int i = 0; i < rawJavaClasspath.size(); i++ )
    {
      String strPaths = rawJavaClasspath.get(i);
      ClassPathFilter filter = i == 0 ? ONLY_API_CLASSES : ALLOW_ALL_FILTER;
      StringTokenizer tokenizer = new StringTokenizer( strPaths, File.pathSeparator );
      while( tokenizer.hasMoreTokens() )
      {
        String strPath = tokenizer.nextToken();
        addClassNames( strPath, filter );
      }
    }
  }

  private void loadTheNewWay() {
    List<String> javaClassPath = module.getJavaClassPath();
    String[] paths = javaClassPath.toArray(new String[javaClassPath.size()]);
    for (int i = 0; i < paths.length; i++) {
      addClassNames(paths[i], filter);
    }
  }

  private void addClassNames(String strPath, ClassPathFilter filter)
  {
    File path = new File( strPath );
    if( path.isFile() )
    {
      addClassNamesFromZip( path, filter );
    }
    else if( path.isDirectory() )
    {
      IDirectory dir = CommonServices.getFileSystem().getIDirectory(path);
      addClassNamesFromDir( dir, dir, filter );
    }
  }

  private void addClassNamesFromDir(final IDirectory root, IDirectory dir, final ClassPathFilter filter)
  {
    for (IFile file : dir.listFiles()) {
      if( isClassFileName( file.getName() ) )
      {
        String strClassName = getClassNameFromFile( root, file );
        if( isValidClassName( strClassName ) )
        {
          putClassName( strClassName, filter );
        }
      }
    }
    for (IDirectory subDir : dir.listDirs()) {
      addClassNamesFromDir(root, subDir, filter);
    }
  }

  private void addClassNamesFromZip(File zipFile, ClassPathFilter filter)
  {
    ZipFile zip;
    try
    {
      zip = new ZipFile( zipFile );
    }
    catch( IOException e )
    {
      return;
    }
    for( Enumeration iter = zip.entries(); iter.hasMoreElements(); )
    {
      ZipEntry entry = (ZipEntry)iter.nextElement();
      String entryName = entry.getName();
      if( entryName.endsWith( CLASS_FILE_EXT ) )
      {
        entryName = makeClassNameFromFileName( entryName );
        if( isValidClassName( entryName ) )
        {
          putClassName( entryName, filter );
        }
      }
    }
    return;
  }

  private void putClassName(String strClassName, ClassPathFilter filter)
  {
    boolean bFiltered = filter != null && !filter.acceptClass( strClassName );
    if( bFiltered )
    {
      // We need to store packages so we can resolve them in the gosu parser
      strClassName = getPlaceholderClassNameForFilteredPackage( strClassName );
    }
    if( strClassName != null )
    {
      cache.add(strClassName);
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
    String strQualifiedClassName = root.getPath().relativePath(file.getPath());
    if( !isClassFileName( strQualifiedClassName ) )
    {
      throw new IllegalArgumentException(
        file.getPath() + " is not a legal Java class name. " +
        "It does not end with " + CLASS_FILE_EXT );
    }
    strQualifiedClassName =
      strQualifiedClassName.substring( 0, strQualifiedClassName.length() -
                                          CLASS_FILE_EXT.length() );
    return strQualifiedClassName.replace(File.separatorChar, '.');
  }

  private boolean isClassFileName( String strFileName )
  {
    return strFileName.toLowerCase().endsWith( ".class" );
  }

  private boolean isValidClassName( String strClassName )
  {
    return !strClassName.endsWith("package-info");
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
