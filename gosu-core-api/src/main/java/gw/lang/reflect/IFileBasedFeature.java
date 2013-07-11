/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import gw.fs.IFile;

public interface IFileBasedFeature extends IFeatureInfo {
  IFile getSourceFile();
}
