/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.config;

import gw.lang.reflect.ITypeLoader;

public interface IGosuInitializationHooks extends IService {
  void beforeTypeLoaderCreation(Class typeLoaderClass);
  void afterTypeLoaderCreation();
}