/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.lang.reflect.gs;

import gw.lang.Gosu;
import gw.lang.reflect.TypeSystem;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

/**
 */
public class GosuClassPathThing {
  private static void addGosuClassProtocolToClasspath() {
    try {
      URLClassLoaderWrapper urlLoader = findUrlLoader();
      URL url = makeURL(urlLoader);
      if (!urlLoader.getURLs().contains(url)) {
        urlLoader.addURL(url);
      }
    }
    catch( Exception e ) {
      throw new RuntimeException( e );
    }
  }

  private static URL makeURL(URLClassLoaderWrapper urlLoader) throws NoSuchFieldException, NoSuchMethodException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, MalformedURLException {
    String protocol =  isOSGi(urlLoader.getWrappedClassLoader()) ? "gosuclassosgi" : "gosuclass";
    URL url;
    try {
      url = new URL( null, protocol + "://honeybadger/" );
    }
    catch( Exception e ) {
      // If our Handler class is not in the system loader and not accessible within the Caller's
      // classloader from the URL constructor (3 activation records deep), then our Handler class
      // is not loadable by the URL class, but the honey badget really doesn't give a shit; it gets
      // what it wants.
      eatTheCobraUpInATree();
      url = new URL( null, protocol + "://honeybadger/" );
    }
    return url;
  }

  private static boolean isOSGi(ClassLoader loader) {
/*
    while (loader != null) {
      if (loader.getClass().getName().equals("org.eclipse.osgi.internal.baseadaptor.DefaultClassLoader") ) {
        return true;
      }
      loader = loader.getParent();
    }
*/
    return false;
  }

  private static void eatTheCobraUpInATree() throws NoSuchFieldException, NoSuchMethodException, ClassNotFoundException, IllegalAccessException, InvocationTargetException {
    Field field = URL.class.getDeclaredField( "handlers" );
    field.setAccessible( true );
    Method put = Hashtable.class.getMethod( "put", Object.class, Object.class );
    Field instanceField = Class.forName( "gw.internal.gosu.compiler.protocols.gosuclass.Handler" ).getField( "INSTANCE" );
    Object handler = instanceField.get( null );
    put.invoke( field.get( null ), "gosuclass", handler );
  }

  private static boolean addOurProtocolPackage() {
    // XXX: Do not add protocol package since OSGi implementation of URLStreamFactory
    // first delegates to those and only then calls service from Service Registry
    String strProtocolProp = "java.protocol.handler.pkgs";
    String ours = "gw.internal.gosu.compiler.protocols";
    if( System.getProperty( strProtocolProp ) != null ) {
      String oldProp = System.getProperty( strProtocolProp );
      if( oldProp.contains( ours ) ) {
        return false;
      }
      ours += "|" + oldProp;
    }
    System.setProperty( strProtocolProp, ours );
    return true;
  }

  private static URLClassLoaderWrapper findUrlLoader() {
    ClassLoader loader = TypeSystem.getGosuClassLoader().getActualLoader();
    if (loader instanceof URLClassLoader) {
      return new SunURLClassLoaderWrapper((URLClassLoader) loader);
    }
    else {
      Class<?> ijUrlClassLoaderClass = findSuperClass(loader.getClass(), IJUrlClassLoaderWrapper.CLASS_NAME);
      if (ijUrlClassLoaderClass != null) {
        return new IJUrlClassLoaderWrapper(loader, ijUrlClassLoaderClass);
      }
    }
    throw new IllegalStateException("class loader not identified as a URL-based loader: " + loader.getClass().getName());
  }

  private static Class<?> findSuperClass(Class<?> loaderClass, String possibleSuperClassName) {
    if (loaderClass == null) {
      return null;
    }
    if (loaderClass.getName().equals(possibleSuperClassName)) {
      return loaderClass;
    }
    return findSuperClass(loaderClass.getSuperclass(), possibleSuperClassName);
  }

  public synchronized static boolean init() {
    if( addOurProtocolPackage() ) {
      if( Gosu.bootstrapGosuWhenInitiatedViaClassfile() ) {
        // Assuming we are in runtime, we push the root module in the case where the process was started with java.exe and not gosu.cmd
        // In other words a Gosu class can be loaded directly from classfile in a bare bones Java program where only the Gosu runtime is
        // on the classpath and no module was pushed prior to loading.
        TypeSystem.pushModule( TypeSystem.getGlobalModule() );
      }
    }
    addGosuClassProtocolToClasspath();
    return true;
  }

  private static abstract class URLClassLoaderWrapper {
    final ClassLoader _loader;
    final Class _classLoaderClass;

    URLClassLoaderWrapper(ClassLoader loader, Class classLoaderClass) {
      _loader = loader;
      _classLoaderClass = classLoaderClass;
    }

    ClassLoader getWrappedClassLoader() {
      return _loader;
    }

    abstract void addURL(URL url);
    abstract List<URL> getURLs();

    Object invokeMethod(String methodName, Class<?>[] paramTypes, Object[] params) {
      try {
        Method method = _classLoaderClass.getDeclaredMethod( methodName, paramTypes );
        method.setAccessible(true);
        return method.invoke(_loader, params);
      } catch (NoSuchMethodException e) {
        throw new RuntimeException( e );
      } catch (IllegalAccessException e) {
        throw new RuntimeException( e );
      } catch (InvocationTargetException e) {
        throw new RuntimeException( e );
      }
    }
  }

  private static class SunURLClassLoaderWrapper extends URLClassLoaderWrapper {
    SunURLClassLoaderWrapper(URLClassLoader loader) {
      super(loader, URLClassLoader.class);
    }

    @Override
    void addURL(URL url) {
      invokeMethod("addURL", new Class[] { URL.class }, new Object[] { url });
    }

    @Override
    List<URL> getURLs() {
      return Arrays.asList(((URLClassLoader) _loader).getURLs());
    }
  }

  private static class IJUrlClassLoaderWrapper extends URLClassLoaderWrapper {
    static final String CLASS_NAME = "com.intellij.util.lang.UrlClassLoader";

    IJUrlClassLoaderWrapper(ClassLoader loader, Class<?> classLoaderClass) {
      super(loader, classLoaderClass);
    }

    @Override
    void addURL(URL url) {
      invokeMethod("addURL", new Class<?>[] { URL.class }, new Object[] { url });
    }

    @Override
    List<URL> getURLs() {
      //noinspection unchecked
      return (List<URL>) invokeMethod("getUrls", new Class<?>[0], new Object[0]);
    }
  }
}
