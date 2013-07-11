/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.reflect.IInjectableClassLoader;

/**
 * The methods ClassLoaders need to implement in order to work with the
 */
public interface IOldGosuClassLoader extends IInjectableClassLoader
{
  Package getPackage( String name );
  void definePackage( String name );
}
