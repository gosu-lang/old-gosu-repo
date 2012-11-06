/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.lang.reflect.module;

import gw.fs.IResource;
import gw.internal.gosu.parser.TypeSystemState;
import gw.lang.UnstableAPI;

import java.util.List;
import java.net.URL;

@UnstableAPI
public interface IExecutionEnvironment
{
  public final static String GLOBAL_MODULE_NAME = "_globalModule";
  public final static String DEFAULT_SINGLE_MODULE_NAME = "_default_";

  IProject getProject();

  List<? extends IModule> getModules();
  void initializeMultipleModules(List<? extends IModule> modules);
  void addModule(IModule module);
  void removeModule(IModule module);
  void pushModule( IModule module );
  void popModule( IModule module );
  IModule getCurrentModule();
  IModule getModule( String strModuleName );
  IModule getModule( IResource file );
  IModule getModule( URL baseURL );
  IModule getGlobalModule();

  IModule getJreModule();

  /**
   * @return Whether or not this is the default single module environment.
   */
  boolean isSingleModuleMode();


//  void setLog(Object log);
//  void logI(String fqn, String message);
//  void logI(String fqn, String message, Throwable t);
  void createJreModule(boolean includesGosuCoreAPI);

  void updateAllModules();
  TypeSystemState getState();

  void renameModule(IModule gosuModule, String name);

  String makeGosucProjectFile( String gosucProjectClassName );
}
