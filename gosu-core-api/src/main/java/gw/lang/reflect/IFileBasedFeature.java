/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.lang.reflect;

import gw.fs.IFile;

public interface IFileBasedFeature extends IFeatureInfo {
  IFile getSourceFile();
}
