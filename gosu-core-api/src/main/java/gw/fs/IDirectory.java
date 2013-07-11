/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.fs;

import gw.lang.UnstableAPI;

import java.io.IOException;
import java.util.List;

@UnstableAPI
public interface IDirectory extends IResource {

  IDirectory dir(String relativePath);

  /**
   * Constucts a file given the path.  If the path is relative path,
   * it will be constructed based on the current directory
   *
   * @param path the path of the file
   * @return The file that is under the directory with the name
   */
  IFile file(String path);

  boolean mkdir() throws IOException;

  List<? extends IDirectory> listDirs();

  List<? extends IFile> listFiles();

  String relativePath(IResource resource);

  void clearCaches();
}
