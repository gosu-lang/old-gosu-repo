/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.config;

import gw.fs.IResource;

import java.io.File;
import java.net.URL;

public class ResourceFileResolver {

  public File resolveToFile(String fileName) {
    return resolveToFile(fileName, getClass().getClassLoader());
  }

  public File resolveToFile(String fileName, ClassLoader classLoader) {
    URL url = classLoader.getResource(fileName);
    return resolveURLToFile(fileName, url);
  }

  public File resolveURLToFile(String fileName, URL url) {
    if (url == null) {
      return null;
    }
    IResource file = CommonServices.getFileSystem().getIFile(url);
    if (file != null) {
      // NOTE pdalbora 3-Nov-2011 -- This method is actually supposed to return the parent of the given
      // resource path, so walk back up the path that many path elements
      int numPathElements = fileName.split("/").length;
      for (int i = 0; i < numPathElements; i++) {
        file = file.getParent();
      }
    }
    return file != null && file.isJavaFile() ? file.toJavaFile() : null;
  }

}
