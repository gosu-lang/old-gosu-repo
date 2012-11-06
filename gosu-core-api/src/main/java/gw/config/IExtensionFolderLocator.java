/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.config;

import java.io.File;

public interface IExtensionFolderLocator extends IService {
  File getExtensionFolderPath();
}
