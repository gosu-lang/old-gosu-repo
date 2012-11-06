package gw.test.servlet;

import gw.config.CommonServices;
import gw.fs.FileFactory;
import gw.fs.IDirectory;
import gw.fs.IFile;
import gw.fs.IResource;
import gw.fs.ResourcePath;
import gw.lang.UnstableAPI;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeRef;
import gw.lang.reflect.TypeSystem;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

@UnstableAPI
public class ChangedTypesRefresher {

  private Map<File, Long> _timestampMap;

  private static ChangedTypesRefresher _instance = new ChangedTypesRefresher();

  public static ChangedTypesRefresher getInstance() {
    return _instance;
  }

  public void reloadChangedTypes() {
    if (_timestampMap == null) {
      _timestampMap = initMap();
      TypeSystem.refresh(); // We have no idea what's changed since the server came up, so we have to refresh everything
    } else {
      Set<String> changedPaths = updateMap(_timestampMap);
      if (canRefreshSelectively(changedPaths)) {
        refreshSpecifiedFiles(changedPaths);
      } else {
        // No idea what's really changed, so refresh everything
        TypeSystem.refresh();
      }
    }

    // We also have to do class redefinition
    reloadChangedClasses();
  }

  private void reloadChangedClasses() {
  }

  private boolean canRefreshSelectively(Set<String> changedPaths) {
    // TODO - AHK - Need some more realistic threshold
    if (changedPaths.size() > 20) {
      return false;
    }

    for (String s : changedPaths) {
      String extension = getExtension(s);
      if (!("gs".equals(extension) || "gsx".equals(extension))) {
        System.out.println("Detected a file change to a file with extension " + extension + ", which will require a full type system refresh");
        return false;
      }
    }

    return true;
  }

  private String getExtension(String path) {
    return path.substring(path.lastIndexOf(".") + 1);
  }

  private void refreshSpecifiedFiles(Set<String> types) {
    // TODO - AHK - Bail out and refresh everything if it doesn't work out?
    for (String s : types) {
      String typeName = s.replace('\\', '.').replace('/', '.');
      typeName = typeName.substring(0, typeName.lastIndexOf("."));
      try {
        IType type = TypeSystem.getByFullName(typeName);
        TypeSystem.refresh((ITypeRef) type, false);
      } catch (Exception e) {
        System.out.println("Error refreshing " + typeName);
        e.printStackTrace();
      }

      System.out.println("Refreshing type " + typeName);
    }
  }

  private Map<File, Long> initMap() {
    long start = System.currentTimeMillis();
    Map<File, Long> result = new HashMap<File, Long>();
    List<? extends IDirectory> sourceEntries = TypeSystem.getCurrentModule().getSourcePath();
    for (IDirectory dir : sourceEntries) {
      // Ignore things inside jar files, and assume the jars aren't changing on us right now
      if (dir.isJavaFile()) {
        mapFileTimestamps(dir, result);
      }
    }

    long end = System.currentTimeMillis();
    System.out.println("initMap() took " + (end - start) + "ms");

    return result;
  }

  public void mapFileTimestamps(IResource resource, Map<File, Long> result) {
    if( !resource.isJavaFile() ) {
      // Ignore jar files right now
      return;
    }
    if (result.containsKey(resource.toJavaFile())) {
      return;
    }

    result.put(resource.toJavaFile(), resource.toJavaFile().lastModified());

    if (resource instanceof IDirectory) {
      for (IResource child : ((IDirectory) resource).listFiles()) {
        mapFileTimestamps(child, result);
      }

      for (IResource child : ((IDirectory) resource).listDirs()) {
        mapFileTimestamps(child, result);
      }
    }
  }

  /**
   * Update the timestamps as necessary, and return the relative file paths (i.e. relative to the source directory)
   * for any files that were detected as changed
   *
   * @param timestamps
   * @return
   */
  private Set<String> updateMap(Map<File, Long> timestamps) {
    long start = System.currentTimeMillis();
    HashSet<File> processedSet = new HashSet<File>();
    HashSet<String> changedFilePaths = new HashSet<String>();
    List<? extends IDirectory> sourceEntries = TypeSystem.getCurrentModule().getSourcePath();
    for (IDirectory dir : sourceEntries) {
      // Ignore jar files
      if (dir.isJavaFile()) {
        HashSet<File> changedFiles = new HashSet<File>();
        updateDirTimestamps(dir, timestamps, processedSet, changedFiles);
        for (File f : changedFiles) {
          changedFilePaths.add(dir.relativePath(CommonServices.getFileSystem().getIFile(f)));
        }
      }
    }

    for (String f : changedFilePaths) {
      System.out.println("File changed " + f);
    }

    long end = System.currentTimeMillis();
    System.out.println("updateMap() took " + (end - start) + "ms");

    return changedFilePaths;
  }

  private void updateDirTimestamps(IDirectory dir, Map<File, Long> result, Set<File> processedSet, Set<File> changedFiles) {
    File backingFile = dir.toJavaFile();
    if (!processedSet.add(backingFile)) {
      return;
    }

    Long previousTimestamp = result.get(backingFile);
    if (previousTimestamp == null) {
      long currentTimestamp = backingFile.lastModified();
      result.put(backingFile, currentTimestamp);
      FileFactory.instance().getDefaultPhysicalFileSystem().clearDirectoryCaches(ResourcePath.parse(backingFile.getAbsolutePath()));
      dir.clearCaches();
    } else {
      long currentTimestamp = backingFile.lastModified();
      if (previousTimestamp != currentTimestamp) {
        result.put(backingFile, currentTimestamp);
        FileFactory.instance().getDefaultPhysicalFileSystem().clearDirectoryCaches(ResourcePath.parse(backingFile.getAbsolutePath()));
        dir.clearCaches();        
      }
    }

    for (IFile childFile : dir.listFiles()) {
      updateFileTimestamps(childFile, result, changedFiles);
    }

    for (IDirectory childDir : dir.listDirs()) {
      updateDirTimestamps(childDir, result, processedSet, changedFiles);
    }
  }

  private void updateFileTimestamps(IFile file, Map<File, Long> result, Set<File> changedFiles) {
    // Ignore Java files
    if (file.getName().endsWith(".java")) {
      return;
    }

    File backingFile = file.toJavaFile();
    Long previousTimestamp = result.get(backingFile);
    if (previousTimestamp == null) {
      long currentTimestamp = backingFile.lastModified();
      result.put(backingFile, currentTimestamp);
      changedFiles.add(backingFile);
    } else {
      long currentTimestamp = backingFile.lastModified();
      if (previousTimestamp != currentTimestamp) {
        result.put(backingFile, currentTimestamp);
        changedFiles.add(backingFile);
      }
    }
  }

}
