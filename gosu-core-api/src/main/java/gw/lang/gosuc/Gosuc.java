/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.lang.gosuc;

import gw.config.CommonServices;
import gw.config.IGlobalLoaderProvider;
import gw.fs.IDirectory;
import gw.lang.GosuShop;
import gw.lang.init.GosuInitialization;
import gw.lang.parser.IGosuParser;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.module.Dependency;
import gw.lang.reflect.module.IExecutionEnvironment;
import gw.lang.reflect.module.IJreModule;
import gw.lang.reflect.module.IModule;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 */
public class Gosuc implements IGosuc {

  private GosucProject _project;
  private IModule _globalModule;
  private List<GosucModule> _allGosucModules;

  public Gosuc( String projectFile, ICustomParser custParser ) throws FileNotFoundException {
    File file = new File( projectFile );
    if( !file.isFile() ) {
      System.err.println( "The project file does not exist: " + file );
    }
    FileInputStream is = new FileInputStream( file );
    _project = GosucProjectParser.parse( new BufferedInputStream( is ), custParser );
    _allGosucModules = _project.getModules();
  }

  public Gosuc( ICustomParser custParser, String projectFileContent ) throws FileNotFoundException {
    _project = GosucProjectParser.parse( projectFileContent, custParser );
    _allGosucModules = _project.getModules();
  }

  public void initializeGosu() {
    CommonServices.getKernel().redefineService_Privileged( IGlobalLoaderProvider.class, new GosucGlobalLoaderProvider() );
    IExecutionEnvironment execEnv = TypeSystem.getExecutionEnvironment( _project );
    List<IModule> modules = defineModules( _project );
    modules.add( _globalModule );
    GosuInitialization.instance( execEnv ).initializeMultipleModules( modules );
    updateAllModuleClasspaths( _project );
    IModule module = execEnv.getModule( IExecutionEnvironment.GLOBAL_MODULE_NAME );
    execEnv.pushModule( module );
    try {
      Object o1 = IGosuParser.NaN;
      Object o2 = JavaTypes.DOUBLE();
    }
    finally {
      execEnv.popModule( module );
    }
    _project.startDependencies();
  }

  private List<IModule> defineModules( GosucProject project ) {
    IExecutionEnvironment execEnv = TypeSystem.getExecutionEnvironment( project );
    execEnv.createJreModule( false );
    _globalModule = GosuShop.createGlobalModule(execEnv);
    _globalModule.setJavaClasspath( Collections.<URL>emptyList() );
    _globalModule.addDependency( new Dependency( execEnv.getJreModule(), true ) );

    List<IDirectory> allSourcePaths = new ArrayList<IDirectory>();
    List<IDirectory> allRoots = new ArrayList<IDirectory>();
    Map<String, IModule> modules = new HashMap<String, IModule>();
    List<IModule> allModules = new ArrayList<IModule>();
    for( GosucModule gosucModule : _allGosucModules ) {
      IModule module = defineModule( project, gosucModule );
      if( module != null ) {
        allSourcePaths.addAll( module.getSourcePath() );
        allRoots.addAll( module.getRoots() );
        modules.put( gosucModule.getName(), module );
        allModules.add( module );
      }
    }

    for( GosucModule gosucModule : _allGosucModules ) {
      IModule module = modules.get( gosucModule.getName() );
      for( GosucDependency dep : gosucModule.getDependencies() ) {
        IModule moduleDep = modules.get( dep.getModuleName() );
        if( moduleDep != null ) {
          module.addDependency( new Dependency( moduleDep, isExported( gosucModule, dep.getModuleName() ) ) );
        }
      }
    }

    addImplicitJreModuleDependency( project, allModules );
    allSourcePaths.addAll( execEnv.getJreModule().getSourcePath() );
    _globalModule.setSourcePath( allSourcePaths );
    for( IDirectory root : allRoots ) {
      _globalModule.addRoot( root );
    }
    _globalModule.update();
    return allModules;
  }

  public IModule defineModule( GosucProject project, GosucModule gosucModule ) {
    IModule gosuModule = GosuShop.createModule( TypeSystem.getExecutionEnvironment( project ),
                                                gosucModule.getName(), false );
    gosuModule.setJavaClasspath( getClassPathURLs( gosucModule ) );
    List<IDirectory> sourceFolders = getSourceFolders( gosucModule );
    gosuModule.setSourcePath( sourceFolders );
    IDirectory sourceRoot = computeCommonRoot( sourceFolders );
    if( sourceRoot != null ) {
      gosuModule.addRoot( sourceRoot );
    }
    gosuModule.setNativeModule( gosucModule );
    _globalModule.addDependency( new Dependency( gosuModule, true ) );
    return gosuModule;
  }

  private IDirectory computeCommonRoot( List<IDirectory> sourceFolders ) {
    if( sourceFolders.isEmpty() ) {
      return null;
    }
    else if( sourceFolders.size() == 1 ) {
      return sourceFolders.get( 0 ).getParent();
    }
    else {
      String[] paths = new String[sourceFolders.size()];
      int minLength = Integer.MAX_VALUE;
      for( int i = 0; i < paths.length; i++ ) {
        paths[i] = sourceFolders.get( i ).getPath().getFileSystemPathString();
        if( paths[i].length() < minLength ) {
          minLength = paths[i].length();
        }
      }
      int charIndex;
      outer:
      for( charIndex = 0; charIndex < minLength; charIndex++ ) {
        char c0 = paths[0].charAt( charIndex );
        for( int i = 1; i < paths.length; i++ ) {
          if( paths[i].charAt( charIndex ) != c0 ) {
            break outer;
          }
        }
      }
      String dirName = paths[0].substring( 0, charIndex );
      File dir = new File( dirName );
      if( !dir.exists() ) {
        return null;
      }
      else {
        return CommonServices.getFileSystem().getIDirectory( dir );
      }
    }
  }

  private List<IDirectory> getSourceFolders( GosucModule gosucModule ) {
    List<IDirectory> sourceFolders = new ArrayList<IDirectory>();
    for( String path : gosucModule.getAllSourceRoots() ) {
      sourceFolders.add( GosucUtil.getDirectoryForPath( path ) );
    }
    return sourceFolders;
  }

  public boolean isExported( GosucModule gosucModule, String childModuleName ) {
    for( GosucDependency dep : gosucModule.getDependencies() ) {
      if( dep.getModuleName().equals( childModuleName ) ) {
        return dep.isExported();
      }
      if( isExported( findGosucModule( dep.getModuleName() ), childModuleName ) ) {
        return true;
      }
    }
    return false;
  }

  private GosucModule findGosucModule( String moduleName ) {
    for( GosucModule mod : _allGosucModules ) {
      if( mod.getName().equals( moduleName ) ) {
        return mod;
      }
    }
    return null;
  }

  private void addImplicitJreModuleDependency( GosucProject project, List<IModule> modules ) {
    IJreModule jreModule = (IJreModule)TypeSystem.getExecutionEnvironment( project ).getJreModule();
    if( jreModule.getJavaClassPath() == null ) {
      updateJreModuleWithProjectSdk( project, jreModule );
    }
    for( IModule module : modules ) {
      module.addDependency( new Dependency( jreModule, true ) );
    }
    modules.add( jreModule );
  }

  public static void updateJreModuleWithProjectSdk( GosucProject project, IJreModule jreModule ) {
    GosucSdk projectSdk = project.getSdk();
    List<String> classFiles = projectSdk.getPaths();
    jreModule.setJavaClasspathFromFiles( classFiles );
    jreModule.setSourcePath( new ArrayList<IDirectory>() );
    jreModule.setNativeSDK( projectSdk );
  }

  void updateAllModuleClasspaths( GosucProject project ) {
    final List<? extends IModule> modules = TypeSystem.getExecutionEnvironment( project ).getModules();
    List<GosucModule> gosucModules = new ArrayList<GosucModule>();
    for( IModule module : modules ) {
      GosucModule gosucModule = (GosucModule)module.getNativeModule();
      if( gosucModule != null ) {
        gosucModules.add( gosucModule );
      }
    }

    Map<String, List<URL>> classpathMap = createClassPathMap( gosucModules.toArray( new GosucModule[gosucModules.size()] ) );
    for( IModule module : modules ) {
      if( module.getNativeModule() != null ) {
        module.setJavaClasspath( classpathMap.get( module.getName() ) );
      }
    }
  }

  private Map<String, List<URL>> createClassPathMap( GosucModule[] allGosucModules ) {
    Map<String, List<URL>> classpathMap = new HashMap<String, List<URL>>();

    for( GosucModule module : allGosucModules ) {
      String name = module.getName();
      List<URL> classPathURLs = getClassPathURLs( module );
      classpathMap.put( name, classPathURLs );
    }

    // simplify classpaths
    for( GosucModule module : allGosucModules ) {
      List<URL> referencedTotalClasspath = getReferencedTotalClasspath( module, classpathMap );
      if( referencedTotalClasspath != null ) {
        List<URL> claspath = classpathMap.get( module.getName() );
        for( Iterator<URL> i = claspath.iterator(); i.hasNext(); ) {
          URL url = i.next();
          if( referencedTotalClasspath.contains( url ) ) {
            i.remove();
          }
        }
      }
    }

    return classpathMap;
  }


  private List<URL> getReferencedTotalClasspath( GosucModule gosucModule, Map<String, List<URL>> classpathMap ) {
    List<URL> totalClasspath = new ArrayList<URL>();
    List<GosucModule> referencedModules = getAllRequiredModules( gosucModule );
    for( GosucModule m : referencedModules ) {
      totalClasspath.addAll( classpathMap.get( m.getName() ) );
    }
    return totalClasspath;
  }


  public List<GosucModule> getAllRequiredModules( GosucModule gosucModule ) {
    Set<GosucModule> visitedProjects = new HashSet<GosucModule>();
    List<GosucModule> modules = new ArrayList<GosucModule>();
    getAllRequiredProjects( gosucModule, modules, visitedProjects );
    return modules;
  }

  private void getAllRequiredProjects( GosucModule gosucModule, List<GosucModule> gosucModuleList, Set<GosucModule> visitedModules ) {
    visitedModules.add( gosucModule );

    for( GosucDependency dep : gosucModule.getDependencies() ) {
      GosucModule depMod = findGosucModule( dep.getModuleName() );
      if( !visitedModules.contains( depMod ) ) {
        gosucModuleList.add( depMod );
        getAllRequiredProjects( depMod, gosucModuleList, visitedModules );
      }
    }
  }

  private static List<URL> getClassPathURLs( GosucModule gosucModule ) {
    List<URL> paths = new ArrayList<URL>();
    for( String path : gosucModule.getClasspath() ) {
      final File file = new File( path );
      if( file.exists() ) {
        URL url = GosucUtil.toURL( file );
        if( url != null ) {
          paths.add( url );
        }
      }
    }
    return paths;
  }

  public List<IType> compile( String moduleName, List<String> types ) {
    IModule module = moduleName == null
                     ? TypeSystem.getGlobalModule()
                     : TypeSystem.getExecutionEnvironment().getModule( moduleName );
    return compile( module, types );
  }

  public List<IType> compile( IModule module, List<String> types ) {
    TypeSystem.pushModule( module );
    try {
      return new GosucCompiler().compile( _project, types );
    }
    finally {
      TypeSystem.popModule( module );
    }
  }

  // You can use this for testing by:
  // - From an IJ project use the 'Write Gosuc Project' command to write out the project file
  // - Then run this from IJ using 'classpath of module' setting to match the proper module for pl/pc/cc etc. (this is so the global loaders will be in the classpath)
  public static void main( String[] args ) throws FileNotFoundException {
    String error = GosucArg.parseArgs( args );
    if( error != null ) {
      System.err.println( error );
      return;
    }
    String strFile = GosucArg.PROJECT.getValue();
    Gosuc gosuc = new Gosuc( strFile, maybeGetCustomParser() );
    gosuc.initializeGosu();
    gosuc.compile( (String)null, Collections.singletonList( "-all" ) );
  }

  private static ICustomParser maybeGetCustomParser() {
    String cls = GosucArg.PARSER.getValue();
    if( cls != null ) {
      try {
        Class.forName( cls ).newInstance();
      }
      catch( Exception e ) {
        throw new RuntimeException( e );
      }
    }
    return null;
  }

}
