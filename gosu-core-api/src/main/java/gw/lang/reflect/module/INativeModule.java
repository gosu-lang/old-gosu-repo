/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.lang.reflect.module;

import gw.fs.IDirectory;

public interface INativeModule {
  Object getNativeModule();
  IDirectory getOutputPath();
}
