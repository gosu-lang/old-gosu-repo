/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.config;

import gw.fs.IDirectory;
import gw.fs.IFile;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.module.IModule;

import java.io.File;

public abstract class AbstractPlatformHelper extends BaseService implements IPlatformHelper {

  private static final String[] IGNORE_DIRECTORY_PATTERNS = new String[]{
      "META-INF",
      "multiapp/ExtendContactEntityWithArraysConfig",
      "multiapp/MultiAppContactAutosycTestConfig",
      "test-config",
      "pcf-config",
      "rule-config",
      "modifiers-test-config",
      "shared-configs",
      "shared-modules",
      "config/resources/firetests",
      "config/web/templates",
      "config/templates",
      "exception/test/config",
      "testConfigPluginCallbackHandler/config",
      "premium-report-test/config",
      "typeloader/test-files",
      "rule-with-exception-test/config"
  };

  private String[] EXTENSIONS = new String[]{
      "pcf", "eti", "eix", "etx", "tti", "ttx", "tix"
  };

  public boolean isConfigFile(IFile file) {
    final String extension = file.getExtension();
    if (extension != null) {
      for (String ext : EXTENSIONS) {
        if (extension.equals(ext)) {
          return true;
        }
      }
    }
    return false;
  }

  public boolean isPathIgnored(String relativePath) {

    final IFile file = CommonServices.getFileSystem().getIFile(new File(relativePath));
    if (file != null && file.exists() && isConfigFile(file)) {
      final IModule module = TypeSystem.getExecutionEnvironment().getModule(file);
      if (module != null) {
        for (IDirectory dir : module.getSourcePath()) {
          if ("config".equals(dir.getName()) && file.isDescendantOf(dir)) {
            return false;
          }
        }
      }
      //System.out.println("Ignoring: " + relativePath);
      return true;
    }

    for (String pattern : IGNORE_DIRECTORY_PATTERNS) {
      if (relativePath.contains(pattern)) {
        return true;
      }
    }

    return false;
  }
}
