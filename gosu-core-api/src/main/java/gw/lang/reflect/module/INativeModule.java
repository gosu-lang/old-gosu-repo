/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.lang.reflect.module;

import gw.fs.IDirectory;

public interface INativeModule {
  boolean isGosuModule();
  Object getNativeModule();
  IDirectory getOutputPath();
}
