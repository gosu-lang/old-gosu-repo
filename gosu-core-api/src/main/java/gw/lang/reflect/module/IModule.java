/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.lang.reflect.module;

import gw.fs.IDirectory;
import gw.fs.IFile;
import gw.fs.IResource;
import gw.lang.reflect.ITypeLoader;
import gw.lang.UnstableAPI;
import gw.lang.reflect.gs.IFileSystemGosuClassRepository;

import java.io.File;
import java.net.URL;
import java.util.List;

@UnstableAPI
public interface IModule
{
  public static final String CONFIG_RESOURCE_PREFIX = "config";
  public static final String CONFIG_RESOURCE_PREFIX_2 = "./config";

  IExecutionEnvironment getExecutionEnvironment();

  IDirectory getOutputPath();

  void update();
  
  /**
   * @return A unique name relative to all other modules in a given execution 
   *   environment.
   */
  String getName();

  void addRoot(IDirectory rootDir);

  /**
   * @return A list of dependencies for this module. The list may contain both 
   *   libraries and other modules. The dependency graph must not have cycles. 
   */
  List<Dependency> getDependencies();

  void addDependency( Dependency dependency );

  void removeDependency( Dependency d );

  void removeDependency( IModule module );

  void clearDependencies();

  ITypeLoaderStack getModuleTypeLoader();

  List<? extends IDirectory> getRoots();

  /**
   * @return The path[s] having source files that should be exposed to this 
   *   module.
   */
  List<IDirectory> getSourcePath();

  void setSourcePath( List<IDirectory> path );

  /**
   * @return the path representing all resource roots visible to this module (a superset of {@link #getSourcePath()})
   */
  List<IDirectory> getFullResourcePath();

  void addGosuApiPath( List<String> paths );

  /**
   * @param paths The class paths this module's Java class loader uses.
   */
  void setJavaClasspath( List<URL> paths );

  void setJavaClasspathFromFiles( List<String> paths );

  List<String> getJavaClassPath();

  /**
   * @return The module/project from the execution environment that corresponds
   *   with this logical module. For example, in Eclipse the native module is of 
   *   type IJavaProject.
   */
  Object getNativeModule();

  void setNativeModule( INativeModule nativeModule );

  boolean isGosuModule();

  String getClassNameForFile(File classFile);

  String getTemplateNameForFile(File templateFile);

  String getProgramNameForFile(File programFile);

  /**
   * Returns typeloaders of the given class that are local to this module as well as such
   * typeloaders from dependent modules.
   *
   * @param typeLoaderClass
   * @param <T>
   * @return
   */
  <T extends ITypeLoader> List<? extends T> getTypeLoaders(Class<T> typeLoaderClass);

  IModule[] getModuleTraversalList();

  boolean includesGosuCoreAPI();

  IFileSystemGosuClassRepository getFileRepository();

  void setDependencies(List<Dependency> newDeps);

  String pathRelativeToRoot(IResource resource);

  IResource getSourceRoot(IResource resource);
}

