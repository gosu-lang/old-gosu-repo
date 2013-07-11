/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.config.AbstractPlatformHelper;
import gw.lang.reflect.module.IModule;

public class DefaultPlatformHelper extends AbstractPlatformHelper {

  @Override
  public boolean isInIDE() {
    return false;
  }

  @Override
  public boolean shouldCacheTypeNames() {
    return false;
  }

  @Override
  public void refresh(IModule module) {
  }
}
