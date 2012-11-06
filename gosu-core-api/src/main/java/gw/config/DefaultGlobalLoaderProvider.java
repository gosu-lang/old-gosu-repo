/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.config;

import gw.lang.init.GosuPathEntry;
import gw.lang.reflect.ITypeLoader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DefaultGlobalLoaderProvider extends BaseService implements IGlobalLoaderProvider {

  public List<Class<? extends ITypeLoader>> getGlobalLoaderTypes() {
    return Collections.EMPTY_LIST;
  }

  @Override
  public List<GosuPathEntry> getGlobalPathEntries() {
    return new ArrayList<GosuPathEntry>(0);
  }

}
