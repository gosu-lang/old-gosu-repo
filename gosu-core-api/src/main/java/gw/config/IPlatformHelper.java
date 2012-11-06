/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.config;

import gw.lang.reflect.ITemporaryFileProvider;
import gw.lang.reflect.module.IModule;

import java.io.File;
import java.net.URL;
import java.util.List;

public interface IPlatformHelper extends IService, ITemporaryFileProvider {

  boolean isInIDE();

  void refresh(IModule module, boolean clearCachedTypes);

  boolean isPathIgnored(String relativePath);

}
