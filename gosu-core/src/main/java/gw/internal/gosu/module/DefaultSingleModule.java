/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.internal.gosu.module;

import gw.config.CommonServices;
import gw.fs.IDirectory;
import gw.internal.gosu.parser.ExecutionEnvironment;
import gw.internal.gosu.parser.FileSystemGosuClassRepository;
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
  public DefaultSingleModule( ExecutionEnvironment execEnv )
  {
    super(execEnv, IExecutionEnvironment.DEFAULT_SINGLE_MODULE_NAME);
  }

  protected List<IDirectory> getAdditionalSourceRoots() {
    return CommonServices.getEntityAccess().getAdditionalSourceRoots();
  }

  @Override
  protected void createStandardTypeLoaders() {
    CommonServices.getTypeSystem().pushTypeLoader( this, new GosuClassTypeLoader( this, getFileRepository( ) ) );
    CommonServices.getTypeSystem().pushTypeLoader( this, new PropertiesTypeLoader( this ) );
    createGlobalTypeloaders( );
  }

  @Override
  public IDirectory getOutputPath()
  {
    return null;
  }

  @Override
  protected void createExtensionTypeLoaders() {
    createExtenxioTypeloadersImpl( );
  }
}
