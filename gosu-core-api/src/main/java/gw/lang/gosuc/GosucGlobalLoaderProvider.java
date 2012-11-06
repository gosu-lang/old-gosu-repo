/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.lang.gosuc;

import gw.config.BaseService;
import gw.config.IGlobalLoaderProvider;
import gw.lang.init.GosuPathEntry;
import gw.lang.reflect.ITypeLoader;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 */
public class GosucGlobalLoaderProvider extends BaseService implements IGlobalLoaderProvider {
  private static ArrayList<Class<? extends ITypeLoader>> LOADER_CLASSES;

  @Override
  public List<Class<? extends ITypeLoader>> getGlobalLoaderTypes() {
    return LOADER_CLASSES;
  }

  @Override
  public List<GosuPathEntry> getGlobalPathEntries() {
    if( LOADER_CLASSES.size() > 0 ) {
      try {
        // hack: The presence of global loaders indicates we're executing with PL classes in classpath,
        // so it's ok to call reflectively.
        Class<?> GosuModuleUtil_Class = Class.forName( "com.guidewire.util.gosu.GosuModuleUtil" );
        Method createDefaultPathEntries_Method = GosuModuleUtil_Class.getMethod( "createDefaultPathEntries" );
        return (List<GosuPathEntry>)createDefaultPathEntries_Method.invoke( null );
      }
      catch( Exception e ) {
        throw new RuntimeException( e );
      }
    }
    return Collections.emptyList();
  }

  public static void setLoaderClass( List<String> classNames ) {
    ArrayList<Class<? extends ITypeLoader>> loaderClasses = new ArrayList<Class<? extends ITypeLoader>>();
    for( String name: classNames ) {
      try {
        loaderClasses.add( (Class<? extends ITypeLoader>)Class.forName( name ) );
      }
      catch( Exception e ) {
        throw new RuntimeException( e );
      }
      LOADER_CLASSES = loaderClasses;
    }
  }
}
