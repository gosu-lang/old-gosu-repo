/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.internal.gosu.module.fs;

import gw.fs.IResource;
import gw.fs.ResourcePath;
import gw.fs.IDirectory;
import gw.lang.reflect.module.IFileSystem;
import gw.config.CommonServices;

import java.io.IOException;
import java.io.File;
import java.net.URI;

public abstract class JavaResourceImpl implements IResource {

  protected File _file;
  private IFileSystem _fileSystem;

  protected JavaResourceImpl(File file) {
    _file = file.getAbsoluteFile();
    _fileSystem = CommonServices.getFileSystem();
  }

  protected IFileSystem getFileSystem() {
    return _fileSystem;
  }

  @Override
  public IDirectory getParent() {
    File parentFile = _file.getParentFile();
    if (parentFile == null) {
      return null;
    } else {
      return getFileSystem().getIDirectory(parentFile);
    }
  }

  @Override
  public String getName() {
    return _file.getName();
  }

  @Override
  public boolean delete() throws IOException {
    return _file.delete();
  }

  @Override
  public URI toURI() {
    return _file.toURI();
  }

  @Override
  public ResourcePath getPath() {
    return ResourcePath.parse(_file.getAbsolutePath());
  }

  @Override
  public boolean isChildOf(IDirectory dir) {
    return dir.equals(getParent());
  }

  @Override
  public boolean isDescendantOf( IDirectory dir ) {
    if ( ! ( dir instanceof JavaDirectoryImpl ) ) {
      return false;
    }
    File javadir = ( (JavaDirectoryImpl) dir )._file;
    File javafile = _file.getParentFile();
    while ( javafile != null ) {
      if ( javafile.equals( javadir ) ) {
        return true;
      }
      javafile = javafile.getParentFile();
    }
    return false;
  }

  @Override
  public File toJavaFile() {
    return _file;
  }

  @Override
  public boolean isJavaFile() {
    return true;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof JavaResourceImpl) {
      return _file.equals(((JavaResourceImpl) obj)._file);
    } else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    return _file.hashCode();
  }

  @Override
  public String toString() {
    return _file.toString();
  }

  @Override
  public boolean create() {
    return false;
  }
}
