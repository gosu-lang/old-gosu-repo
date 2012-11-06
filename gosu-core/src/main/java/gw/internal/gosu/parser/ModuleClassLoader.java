/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.config.CommonServices;
import gw.lang.reflect.module.IExecutionEnvironment;
import gw.lang.reflect.module.IJreModule;
import gw.lang.reflect.module.IModule;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

public class ModuleClassLoader extends URLClassLoader {
  private IModule _module;
  private boolean _bDeferToParent;

  private ModuleClassLoader(URL[] urls, IModule module) {
    super(urls);
    _module = module;
  }

  private ModuleClassLoader(URL[] urls, ClassLoader parent, IModule module) {
    super(urls, parent);
    _bDeferToParent = urls.length == 0;
    _module = module;
  }

  public boolean isDeferToParent() {
    return _bDeferToParent;
  }

  protected Class<?> loadClassLocally(String name, boolean resolve) throws ClassNotFoundException {
    if( _bDeferToParent || getParent() != getSystemClassLoader() || isJreModuleInPluginTest() ) {
      return super.loadClass(name, false);
    } else {
      // First, check if the class has already been loaded
      Class c = findLoadedClass(name);
      if (c == null) {
        // If still not found, then invoke findClass in order to find the class.
        try {
          c = findClass(name);
        } catch (Exception e) {}
      }
      if (c == null) {
        return super.loadClass(name, false);
      }
      if (resolve) {
        resolveClass(c);
      }
      return c;
    }
  }

  private boolean isJreModuleInPluginTest() {
    return _module instanceof IJreModule && _module.getExecutionEnvironment().getProject().isHeadless();
  }

  @Override
  protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
    if ( _module == null) {
      throw new RuntimeException("This loader has been disposed and cannot be used for class loading purposes.");
    }
    if (name.equalsIgnoreCase("con") || name.toLowerCase().endsWith(".con")) {
      return null;
    }
    try {
      Class<?> aClass = loadClassLocally(name, resolve);
      return aClass;
    } catch (Throwable e1) {
      for (IModule m : _module.getModuleTraversalList()) {
        if (m != _module ) {
          DefaultTypeLoader typeLoader = m.getTypeLoaders(DefaultTypeLoader.class).get(0);
          ClassLoader moduleClassLoader = typeLoader.getModuleClassLoader();
          Class aClass;
          try {
            if (moduleClassLoader instanceof ModuleClassLoader) {
              aClass = ((ModuleClassLoader) moduleClassLoader).loadClassLocally(name, resolve);
            } else {
              aClass = moduleClassLoader.loadClass(name);
            }
            return aClass;
          } catch (ClassNotFoundException e2) {
          }
        }
      }
      throw new ClassNotFoundException("Class not found at all: " + name, e1);
    }
  }

  public static ModuleClassLoader create(IModule module) {
    List<String> javaClassPath = module.getJavaClassPath();
    List<URL> urls = new ArrayList<URL>(javaClassPath.size());
    for (String entry : javaClassPath) {
      try {
        urls.add(new File(entry).toURI().toURL());
      } catch (MalformedURLException e) {
        throw new RuntimeException(e);
      }
    }

    IExecutionEnvironment environment = module.getExecutionEnvironment();
    if (module == environment.getJreModule()) {
      ClassLoader parent = CommonServices.getEntityAccess().getPluginClassLoader();
      if (environment.isSingleModuleMode()) {
        urls.clear();
      }
      return new ModuleClassLoader(urls.toArray(new URL[urls.size()]), parent, module);
    } else {
      return new ModuleClassLoader(urls.toArray(new URL[urls.size()]), module);
    }
  }

  public void dispose() {
    _module = null;
  }
}
