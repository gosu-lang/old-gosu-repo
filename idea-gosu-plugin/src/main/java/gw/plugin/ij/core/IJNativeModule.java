/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.plugin.ij.core;

import com.intellij.openapi.compiler.CompilerPaths;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.impl.CompilerModuleExtensionImpl;
import com.intellij.openapi.vfs.VirtualFile;
import gw.config.CommonServices;
import gw.fs.IDirectory;
import gw.lang.reflect.module.INativeModule;
import gw.plugin.ij.util.IDEAUtil;

import java.net.MalformedURLException;
import java.net.URL;

public class IJNativeModule implements INativeModule {
  private final Module _nativeModule;

  public IJNativeModule(Module module) {
    _nativeModule = module;
  }

  @Override
  public Module getNativeModule() {
    return _nativeModule;
  }

  @Override
  public IDirectory getOutputPath() {
    final VirtualFile file = CompilerPaths.getModuleOutputDirectory(getNativeModule(), false);
    return file != null ? IDEAUtil.toIDirectory(file) : null;
  }
}
