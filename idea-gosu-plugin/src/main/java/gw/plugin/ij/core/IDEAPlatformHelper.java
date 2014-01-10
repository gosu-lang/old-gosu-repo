/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.plugin.ij.core;

import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.fileEditor.FileEditorManagerEvent;
import com.intellij.openapi.fileEditor.FileEditorManagerListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.PathUtil;
import gw.config.AbstractPlatformHelper;
import gw.lang.reflect.module.IModule;
import gw.plugin.ij.lang.psi.impl.GosuClassParseDataCache;

import org.jetbrains.annotations.NotNull;

import java.io.File;

public class IDEAPlatformHelper extends AbstractPlatformHelper implements FileEditorManagerListener {
  private Project _project;

  public IDEAPlatformHelper(Project project) {
    _project = project;
  }

  @Override
  public boolean isInIDE() {
    return true;
  }

  @Override
  public boolean shouldCacheTypeNames() {
    return true;
  }

  @Override
  public void refresh(IModule module) {
    GosuClassParseDataCache.clear();
  }

  // FileEditorManagerListener
  public void fileOpened(FileEditorManager source, final VirtualFile file) {
  }

  public void fileClosed(FileEditorManager source, VirtualFile file) {
  }

  public void selectionChanged(@NotNull FileEditorManagerEvent event) {
  }

  @Override
  public File getIndexFile(String id) {
    final File indexPath = PathManager.getIndexRoot();
    File dir = new File(indexPath, "gosutypenames");
    if (!dir.exists()) {
      dir.mkdir();
    }
    String projectID = _project.getName() + "$" + _project.getLocationHash();
    return new File(dir, projectID + "$" + id + "$index.txt");
  }


  public File getIDEACachesDirFile() {
    return new File(getIDEACachesDir());
  }

  public String getIDEACachesDir() {
    String dir = System.getProperty("caches_dir");
    return dir == null ? PathManager.getSystemPath() + "/caches/" : dir;
  }

  public File getIDEACorruptionMarkerFile() {
    return new File(getIDEACachesDirFile(), "corruption.marker");
  }

}
