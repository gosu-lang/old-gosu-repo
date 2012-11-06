/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.lang.reflect;

import gw.fs.IFile;
import gw.lang.reflect.gs.ISourceFileHandle;
import gw.lang.reflect.module.IModule;

public interface ITemporaryFileProvider {

  ISourceFileHandle getSourceFileHandle(IFile file, String strQualifiedClassName);
}
