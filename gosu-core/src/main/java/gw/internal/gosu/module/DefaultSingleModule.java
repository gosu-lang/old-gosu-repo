/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.internal.gosu.module;

import gw.config.CommonServices;
import gw.fs.IDirectory;
import gw.internal.gosu.parser.ExecutionEnvironment;
import gw.internal.gosu.properties.PropertiesTypeLoader;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.GosuClassTypeLoader;
import gw.lang.reflect.module.IExecutionEnvironment;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DefaultSingleModule extends GlobalModule
{
  private static final List<String> SPECIAL_FILES = Arrays.asList("servlet-api.jar");

  public DefaultSingleModule( ExecutionEnvironment execEnv )
  {
    super(execEnv, IExecutionEnvironment.DEFAULT_SINGLE_MODULE_NAME);
  }

  public DefaultSingleModule reset()
  {
    reset(getName(), includesGosuCoreAPI());
    return this;
  }

  @Override
  public void reset( String strName, boolean includesGosuCoreAPI )
  {
    super.reset(strName, includesGosuCoreAPI);
    setJavaClasspathFromFiles(getRawJavaClasspath());
  }

  @Override
  protected void createStandardTypeLoaders() {
    TypeSystem.pushTypeLoader(this, new GosuClassTypeLoader(this, getFileRepository()));
    TypeSystem.pushTypeLoader( this, new PropertiesTypeLoader( this ) );
    createGlobalTypeloaders();
  }

  @Override
  public IDirectory getOutputPath()
  {
    return null;
  }

  /**
   * This method is a hack to resolve "special" system-like classes that may be provided in certain
   * execution enviornments.  Currently, we only add the servlet jar and ant jars if we find them
   * in parent class loaders of the current class loader.  This means that people can code against
   * these APIs in environments where these classes are available without explicitly including
   * these jars in thier projects.  Ugly.
   */
  private void addSpecialJars( ClassLoader loader, ArrayList<String> vals )
  {
    // Don't include the default type loader, which should be part of the java.class.path above
    if( loader == null || loader.getParent() == null )
    {
      return;
    }
    if( loader instanceof URLClassLoader)
    {
      URLClassLoader urlLoader = (URLClassLoader) loader;
      URL[] urls = urlLoader.getURLs();
      if (urls != null) {
        StringBuilder sb = new StringBuilder();
        for (URL url : urls)
        {
          try
          {
            URI uri = url.toURI();
            String scheme = uri.getScheme();
            if( scheme != null && scheme.equalsIgnoreCase( "file" ) )
            {
              File file = new File( uri );
              if( SPECIAL_FILES.contains( file.getName() ) )
              {
                String path = file.getAbsolutePath();
                sb.append(path).append(File.pathSeparator);
              }
            }
          }
          catch( URISyntaxException e )
          {
            //ignore
          }
        }
        if( sb.length() > 0 )
        {
          vals.add(sb.toString());
        }
      }
    }
    addSpecialJars( loader.getParent(), vals );
  }

  public ArrayList<String> getRawJavaClasspath() {
    ArrayList<String> vals = new ArrayList<String>();
    vals.add(System.getProperty("java.class.path", "") + File.pathSeparator + CommonServices.getEntityAccess().getWebServerPaths());
    ClassLoader thisClassLoader = getClass().getClassLoader();
    addSpecialJars(thisClassLoader, vals);
    vals.add(System.getProperty("sun.boot.class.path", ""));
    vals.add(System.getProperty("java.ext.dirs", ""));
    vals.add(CommonServices.getEntityAccess().getPluginRepositories().toString());
    if (_includesGosuCoreAPI) {
      addGosuApiPath( vals );
    }
    return vals;
  }

  @Override
  protected void createExtensionTypeLoaders() {
    createExtenxioTypeloadersImpl();
  }
}
