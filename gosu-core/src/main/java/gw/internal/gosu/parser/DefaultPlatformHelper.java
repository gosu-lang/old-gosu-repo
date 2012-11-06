/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.config.AbstractPlatformHelper;
import gw.config.BaseService;
import gw.config.IPlatformHelper;
import gw.fs.IFile;
import gw.lang.reflect.gs.ISourceFileHandle;
import gw.lang.reflect.module.IModule;

import java.util.List;

public class DefaultPlatformHelper extends AbstractPlatformHelper {

  @Override
  public boolean isInIDE() {
    return false;
  }

  @Override
  public void refresh(IModule module, boolean clearCachedTypes) {
  }

  @Override
  public ISourceFileHandle getSourceFileHandle(IFile file, String strQualifiedClassName) {
    return null; // no editors or anything here
  }
}
