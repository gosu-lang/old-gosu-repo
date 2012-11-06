/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.internal.gosu.module;

import gw.config.CommonServices;
import gw.fs.IDirectory;
import gw.fs.IFile;
import gw.fs.IResource;
import gw.internal.gosu.parser.DefaultTypeLoader;
import gw.internal.gosu.parser.FileSystemGosuClassRepository;
import gw.internal.gosu.parser.ModuleTypeLoader;
import gw.internal.gosu.properties.PropertiesTypeLoader;
import gw.lang.GosuShop;
import gw.lang.reflect.ITypeLoader;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.GosuClassTypeLoader;
import gw.lang.reflect.gs.IFileSystemGosuClassRepository;
import gw.lang.reflect.gs.IGosuClassRepository;
import gw.lang.reflect.module.Dependency;
import gw.lang.reflect.module.IExecutionEnvironment;
import gw.lang.reflect.module.IModule;
import gw.lang.reflect.module.INativeModule;
import gw.util.Extensions;
import gw.util.GosuExceptionUtil;

import java.io.File;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Module implements IModule
{
  private IExecutionEnvironment _execEnv;

  private String _strName;
  private List<Dependency> _dependencies;
  private volatile IModule[] _traversalList;
  private ModuleTypeLoader _modTypeLoader;

  private List<IDirectory> _roots;
  protected List<IDirectory> _explicitSourceRoots;
  protected ResourceRoots _allResourceRoots;
  protected Set<String> _allSourceExtensions;
  protected List<String> _classpath;
  private INativeModule _nativeModule;

  protected boolean _includesGosuCoreAPI;
  private IFileSystemGosuClassRepository _fileRepository;
  private URLClassLoader _extensionsClassLoader;
  private List<ITypeLoader> _extensionTypeLoaders; //## todo: curiously, we don't seem to be using these here

  public Module( IExecutionEnvironment execEnv, String strName, boolean includesGosuCoreAPI )
  {
    _execEnv = execEnv;
    reset( strName, includesGosuCoreAPI );
  }

  public void reset( String strName, boolean includesGosuCoreAPI )
  {
    _strName = strName;
    _dependencies = new ArrayList<Dependency>();
    _includesGosuCoreAPI = includesGosuCoreAPI;
    _roots = new ArrayList<IDirectory>();
    _allResourceRoots = null;
    _explicitSourceRoots = new ArrayList<IDirectory>();
    _extensionTypeLoaders = new ArrayList<ITypeLoader>();
  }

  public final IExecutionEnvironment getExecutionEnvironment()
  {
    return _execEnv;
  }

  public boolean includesGosuCoreAPI() {
    return _includesGosuCoreAPI;
  }

  @Override
  public IFileSystemGosuClassRepository getFileRepository() {
    return _fileRepository;
  }

  @Override
  public void setDependencies(List<Dependency> newDeps) {
    _dependencies = new ArrayList<Dependency>(newDeps);
    update();
  }

  @Override
  public void update() {
    // create default traversal list
    List<IModule> traversalList = new ArrayList<IModule>();
    buildTraversalList(this, traversalList);
    // make sure that the jre module is last
    IModule jreModule = getExecutionEnvironment().getJreModule();
    if (traversalList.remove(jreModule)) {
      traversalList.add(jreModule);
    }
    IModule globalModule = getExecutionEnvironment().getGlobalModule();
    if (this != globalModule) {
      traversalList.add(0, globalModule);
    }
    _traversalList = traversalList.toArray(new IModule[traversalList.size()]);

    // create prefix traversal lists
//    _traversalListsByPrefix = createTraversalPrefixMap();
  }

  @Override
  public String getName()
  {
    return _strName;
  }

  public void addRoot(IDirectory rootDir) {
    _roots.add(rootDir);
  }

  @Override
  public List<Dependency> getDependencies()
  {
    return _dependencies;
  }

  @Override
  public void addDependency( Dependency d )
  {
    _dependencies.add(d);
  }

  public void removeDependency( Dependency d )
  {
    _dependencies.remove( d );
  }

  public void removeDependency( IModule module ) {
    int size = _dependencies.size();
    for(int i = 0; i < size; ++i) {
      Dependency d = _dependencies.get(i);
      if(d.getModule() == module) {
        _dependencies.remove(i);
        break;
      }
    }
  }

  @Override
  public void clearDependencies()
  {
    _dependencies.clear();
  }

  @Override
  public List<IDirectory> getSourcePath()
  {
    maybeComputeSourcePathAndExtensions();
    return _allResourceRoots.getAllSourceRoots();
  }

  @Override
  public List<IDirectory> getFullResourcePath()
  {
    maybeComputeSourcePathAndExtensions();
    return _allResourceRoots.getAllResourceRoots();
  }

  private void maybeComputeSourcePathAndExtensions() {
    if (_allResourceRoots == null) {
      _allResourceRoots = new ResourceRoots();
      _allResourceRoots.addAllAsSourceRoots(_explicitSourceRoots);
      _allSourceExtensions = new HashSet<String>();
      Collections.addAll(_allSourceExtensions, GosuClassTypeLoader.ALL_EXTS);
      _allSourceExtensions.add(".java");

      // add the extension typeloader source paths and extensions
      populateExtensionSourcePathsAndExtensions();
    }
  }

  private void populateExtensionSourcePathsAndExtensions() {
    List<String> javaClassPath = getJavaClassPath();
    for (String path : javaClassPath) {
      try {
        IDirectory root = CommonServices.getFileSystem().getIDirectory(new File(path).toURI().toURL());
        List<String> sourceFileExtensions = Extensions.getExtensions(new File(path), Extensions.CONTAINS_SOURCES);
        for (String ext : sourceFileExtensions) {
          _allSourceExtensions.add('.' + ext);
        }
        boolean hasExtensions = sourceFileExtensions.size() > 0;
        _allResourceRoots.add(root, hasExtensions);
      } catch (MalformedURLException e) {
        throw new RuntimeException(e);
      }
    }
  }

  @Override
  public void setSourcePath( List<IDirectory> path )
  {
    _explicitSourceRoots = path;
    _allResourceRoots = null;
    if (_fileRepository != null) { // if it was already created
      List<IDirectory> sourcePath = getSourcePath();
      _fileRepository.setSourcePath(sourcePath.toArray(new IDirectory[sourcePath.size()]));
    }
  }

  @Override
  public IDirectory getOutputPath()
  {
    return _nativeModule.getOutputPath();
  }

  public ModuleTypeLoader getModuleTypeLoader()
  {
    return _modTypeLoader;
  }

  public void setModuleTypeLoader( ModuleTypeLoader modTypeLoader )
  {
    _modTypeLoader = modTypeLoader;
  }

  @Override
  public void setJavaClasspath( List<URL> urls )
  {
    setJavaClasspathFromFiles(GosuShop.urls2paths(urls));
  }

  public void addGosuApiPath( List<String> paths )
  {
    File file = TypeSystem.getResourceFileResolver().resolveToFile(FileSystemGosuClassRepository.RESOURCE_LOCATED_W_CLASSES);
    paths.add(file.getAbsolutePath());
  }

  @Override
  public void setJavaClasspathFromFiles( List<String> paths )
  {
    _classpath = maybeExpand(paths);
    _allResourceRoots = null; // need to recalculate the source roots now
    if (_fileRepository != null) { // need to update the repository paths
      List<IDirectory> src = getSourcePath();
      _fileRepository.setSourcePath(src.toArray(new IDirectory[src.size()]));
    }
  }

  @Override
  public List<String> getJavaClassPath()
  {
    return _classpath;
  }

  @Override
  public String toString()
  {
    return _strName;
  }

  @Override
  public Object getNativeModule()
  {
    return _nativeModule != null ? _nativeModule.getNativeModule() : null;
  }

  @Override
  public void setNativeModule( INativeModule nativeModule )
  {
    _nativeModule = nativeModule;
  }

  @Override
  public String getClassNameForFile( File classFile )
  {
    GosuClassTypeLoader typeLoader = getModuleTypeLoader().getTypeLoader(GosuClassTypeLoader.class);
    IFileSystemGosuClassRepository repo = (IFileSystemGosuClassRepository)typeLoader.getRepository();
    IFile classIFile = CommonServices.getFileSystem().getIFile( classFile );
    List<IDirectory> classPath = repo.getClassPath();
    IDirectory root = null;
    for (IDirectory folder : classPath) {
      if (classIFile.isDescendantOf(folder) ) {
        root = folder;
        break;
      }
    }
    if (root == null) {
      return null;
    }
    return repo.getClassNameFromFile( root, classIFile, repo.getExtensions() );
  }

  public String getTemplateNameForFile( File templateFile )
  {
    IFile programIFile = CommonServices.getFileSystem().getIFile(templateFile);
    GosuClassTypeLoader typeLoader = getModuleTypeLoader().getTypeLoader( GosuClassTypeLoader.class );
    IFileSystemGosuClassRepository repo = (IFileSystemGosuClassRepository)typeLoader.getRepository();
    List<IDirectory> classPath = repo.getClassPath();
    IDirectory root = null;
    for (IDirectory folder : classPath) {
      if (programIFile.isDescendantOf(folder) ) {
        root = folder;
        break;
      }
    }
    if (root == null) {
      return null;
    }
    return repo.getClassNameFromFile( root, programIFile, new String[] {GosuClassTypeLoader.GOSU_TEMPLATE_FILE_EXT} );
  }

  public String getProgramNameForFile( File programFile )
  {
    IFile programIFile = CommonServices.getFileSystem().getIFile(programFile);
    GosuClassTypeLoader typeLoader = getModuleTypeLoader().getTypeLoader(GosuClassTypeLoader.class);
    IFileSystemGosuClassRepository repo = (IFileSystemGosuClassRepository)typeLoader.getRepository();
    List<IDirectory> classPath = repo.getClassPath();
    IDirectory root = null;
    for (IDirectory folder : classPath) {
      if (programIFile.isDescendantOf(folder) ) {
        root = folder;
        break;
      }
    }
    if (root == null) {
      return null;
    }
    return repo.getClassNameFromFile( root, programIFile, new String[] {GosuClassTypeLoader.GOSU_PROGRAM_FILE_EXT} );
  }

  public void initializeTypeLoaders() {
    List<IDirectory> src = getSourcePath();
    Set<String> extensions = getSourceExtensions();
    _fileRepository = new FileSystemGosuClassRepository(this, src.toArray(new IDirectory[src.size()]), extensions.toArray(new String[extensions.size()]), _includesGosuCoreAPI);

    maybeCreateModuleTypeLoader();
    createStandardTypeLoaders();
    if( CommonServices.getEntityAccess().getLanguageLevel().isStandard() ) {
      createExtensionTypeLoaders();
    }

    // initialize all loaders
    List<ITypeLoader> loaders = getModuleTypeLoader().getTypeLoaders();
    for (int i = loaders.size() - 1; i >= 0; i--) {
      loaders.get(i).init();
    }
  }

  protected void createExtensionTypeLoaders() {
    createExtenxioTypeloadersImpl();
  }

  protected void createExtenxioTypeloadersImpl() {
    for( String additionalTypeLoader : getExtensionTypeLoaderNames())
    {
      try {
        ITypeLoader typeLoader = createAndPushTypeLoader(_fileRepository, additionalTypeLoader, getExtensionClassLoader());
        _extensionTypeLoaders.add(typeLoader);
      } catch (Throwable e) {
        System.err.println("==> WARNING: Cannot create extension typeloader " + additionalTypeLoader + ". " + e.getMessage());
//        e.printStackTrace(System.err);
        System.err.println("==> END WARNING.");
      }
    }
  }

  private Set<String> getExtensionTypeLoaderNames() {
    Set<String> set = new HashSet<String>();
    for (URL url : getExtensionURLs()) {
      Extensions.getExtensions(set, new File(url.getFile()), "Gosu-Typeloaders");
    }
    return set;
  }

  private ClassLoader getExtensionClassLoader() {
    if (_extensionsClassLoader == null) {
      _extensionsClassLoader = new ExtensionClassLoader(Module.this.getExtensionURLs(), Module.this.getClass().getClassLoader());
    }
    return _extensionsClassLoader;
  }

  private URL[] getExtensionURLs() {
    List<URL> urls = new ArrayList<URL>();
    for (String path : _classpath) {
      try {
        urls.add(new File(path).toURI().toURL());
      } catch (MalformedURLException e) {
        //ignore
      }
    }
    return urls.toArray(new URL[urls.size()]);
  }

  protected void createStandardTypeLoaders()
  {
    TypeSystem.pushTypeLoader( this, new GosuClassTypeLoader( this, _fileRepository ) );
    TypeSystem.pushTypeLoader( this, new PropertiesTypeLoader( this ) );
  }

  protected void maybeCreateModuleTypeLoader() {
    if (getModuleTypeLoader() == null) {
      ModuleTypeLoader tla = new ModuleTypeLoader( this, new DefaultTypeLoader(this) );
      setModuleTypeLoader(tla);
    }
  }

  public final IModule[] getModuleTraversalList() {
    return _traversalList;
  }

  private void buildTraversalList(final IModule theModule, List<IModule> traversalList) {
    traversalList.add(theModule);
    for (Dependency dependency : theModule.getDependencies()) {
      IModule dependencyModule = dependency.getModule();

      // traverse all direct dependency and indirect exported dependencies
      if (!traversalList.contains(dependencyModule) &&
              (dependency.isExported() || theModule == this)) {
        buildTraversalList(dependencyModule, traversalList);
      }
    }
  }

  @Override
  public <T extends ITypeLoader> List<? extends T> getTypeLoaders(Class<T> typeLoaderClass) {
    List<T> results = new ArrayList<T>();
    for (ITypeLoader loader : getModuleTypeLoader().getTypeLoaderStack()) {
      if (typeLoaderClass.isInstance(loader)) {
        results.add(typeLoaderClass.cast(loader));
      }
    }
    return results;
  }

  @Override
  public boolean isGosuModule() {
    return _nativeModule == null || _nativeModule.isGosuModule();
  }

  private ITypeLoader createAndPushTypeLoader(IFileSystemGosuClassRepository classRepository, String className, ClassLoader classLoader)
  {
    ITypeLoader typeLoader = null;
    try
    {
      if (classLoader == null) {
        boolean standard = CommonServices.getEntityAccess().getLanguageLevel().isStandard();
        classLoader = standard ? TypeSystem.getGosuClassLoader().getActualLoader() : null;
      }
      Class loaderClass = Class.forName( className, true, classLoader );
      CommonServices.getGosuInitializationHooks().beforeTypeLoaderCreation( loaderClass );

      Constructor constructor = getConstructor( loaderClass, IModule.class );
      if( constructor != null )
      {
        typeLoader = (ITypeLoader) constructor.newInstance( this );
      }
      else
      {
        constructor = getConstructor( loaderClass, IModule.class );
        if( constructor != null )
        {
          typeLoader = (ITypeLoader) constructor.newInstance( this );
        }
        else
        {
          if( constructor != null )
          {
            typeLoader = (ITypeLoader) constructor.newInstance( this );
          }
          else
          {
            constructor = getConstructor( loaderClass, IGosuClassRepository.class );
            if( constructor != null )
            {
              typeLoader = (ITypeLoader) constructor.newInstance( classRepository );
            }
            else
            {
              constructor = getConstructor( loaderClass );
              if( constructor != null )
              {
                typeLoader = (ITypeLoader) constructor.newInstance();
              }
            }
          }
        }
      }
    }
    catch( Exception e )
    {
      throw GosuExceptionUtil.forceThrow( e );
    }
    if( typeLoader != null )
    {
      TypeSystem.pushTypeLoader( this, typeLoader );
      CommonServices.getGosuInitializationHooks().afterTypeLoaderCreation();
    }
    else
    {
      throw new IllegalStateException(
        "TypeLoader class " + className + " must have one of the following constructor signatures:\n" +
        "  <init>()\n" +
        "  <init>(gw.lang.reflect.module.IModule)\n" +
        "  <init>(gw.lang.reflect.gs.IGosuClassRepository)\n" );
    }
    return typeLoader;
  }

  private Constructor getConstructor( Class loaderClass, Class... argTypes )
  {
    try
    {
      return loaderClass.getConstructor( argTypes );
    }
    catch( NoSuchMethodException e )
    {
      return null;
    }
  }

  private static List<String> maybeExpand( List<String> paths )
  {
    LinkedHashSet<String> expanded = new LinkedHashSet<String>();
    for( String path : paths )
    {
      for( String pathElement : path.split( File.pathSeparator ) )
      {
        if( pathElement.length() > 0 )
        {
          IResource resource = CommonServices.getFileSystem().getIFile(new File(pathElement));
          if (resource == null) {
            resource = CommonServices.getFileSystem().getIDirectory(new File(pathElement));
          }
          expanded.add(resource.getPath().getPathString());
        }
      }
    }
    return new ArrayList<String>( expanded );
  }

  public boolean equals(Object o) {
    if (!(o instanceof IModule)) {
      return false;
    }
    IModule m = (IModule) o;
    return this.getName().equals(m.getName());
  }

  public int hashCode() {
    return _strName.hashCode();
  }

  public void setName(String name) {
    _strName = name;
  }

  public Set<String> getSourceExtensions() {
    maybeComputeSourcePathAndExtensions();
    return _allSourceExtensions;
  }

  private static class ExtensionClassLoader extends URLClassLoader {

    public ExtensionClassLoader(URL[] extensionURLs, ClassLoader parent) {
      super(extensionURLs, parent);
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
      try {
        // delegate first
        return super.loadClass(name, resolve);
      } catch (ClassNotFoundException e) {
        // if we missed, let's try to load locally
        synchronized (this) {
          Class c = this.findLoadedClass(name);
          if (c == null) {
            try {
              c = this.findClass(name);
            } catch (ClassNotFoundException e2) {
              //ignore
            }
          }
          if (c != null) {
            if (resolve) {
              this.resolveClass(c);
            }
            return c;
          }
        }
        throw e;
      }
    }
  }

  @Override
  public List<? extends IDirectory> getRoots() {
    return _roots;
  }

  public String pathRelativeToRoot(IResource resource) {
    for (IDirectory root : getSourcePath()) {
      if (resource.isDescendantOf(root)) {
        return root.relativePath(resource);
      }
    }
    for (IDirectory root : getRoots()) {
      if (resource.isDescendantOf(root)) {
        return root.relativePath(resource);
      }
    }
    return null;
  }

  @Override
  public IResource getSourceRoot(IResource resource) {
    for (IDirectory root : _explicitSourceRoots) {
      if (resource.isDescendantOf(root)) {
        return root;
      }
    }
    return null;
  }

  protected static class ResourceRoots {
    private List<IDirectory> _allSourceRoots = new ArrayList<IDirectory>();
    private List<IDirectory> _allResourceRoots = new ArrayList<IDirectory>();

    protected void addAllAsSourceRoots(List<IDirectory> dirs) {
      _allSourceRoots.addAll(dirs);
      _allResourceRoots.addAll(dirs);
    }

    protected void add(IDirectory dir, boolean sourceRootOnly) {
      if (sourceRootOnly) {
        _allSourceRoots.add(dir);
      }
      _allResourceRoots.add(dir);
    }

    public List<IDirectory> getAllSourceRoots() {
      return _allSourceRoots;
    }

    public List<IDirectory> getAllResourceRoots() {
      return _allResourceRoots;
    }
  }

}
