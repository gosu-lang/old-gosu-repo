/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.config;

import java.util.List;

import gw.lang.init.GosuPathEntry;
import gw.lang.reflect.ITypeLoader;

public interface IGlobalLoaderProvider extends IService {

  List<Class<? extends ITypeLoader>> getGlobalLoaderTypes();

  List<GosuPathEntry> getGlobalPathEntries();

}
