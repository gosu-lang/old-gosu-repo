/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.lang.init;

import gw.lang.UnstableAPI;
import gw.fs.IDirectory;
import gw.fs.IFile;
import gw.xml.simple.SimpleXmlNode;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@UnstableAPI
public class ModuleFileUtil {

  /**
   * Reads a module.xml file into a GosuPathEntry object
   *
   * @param moduleFile the module.xml file to convert to GosuPathEntry
   * @return an ordered list of GosuPathEntries created based on the algorithm described above
   */
  public static GosuPathEntry createPathEntryForModuleFile(IFile moduleFile) {
    try {
      InputStream is = moduleFile.openInputStream();
      try {
        SimpleXmlNode moduleNode = SimpleXmlNode.parse(is);
        IDirectory rootDir = moduleFile.getParent();

        List<IDirectory> sourceDirs = new ArrayList<IDirectory>();
        List<String> typeloaderNames = new ArrayList<String>();

        for (SimpleXmlNode child : moduleNode.getChildren()) {
          if (child.getName().equals("src")) {
            sourceDirs.add(rootDir.dir(child.getText()));
          } else if (child.getName().equals("typeloaders")) {
            for (SimpleXmlNode typeloader : child.getChildren()) {
              if (typeloader.getName().equals("typeloader")) {
                typeloaderNames.add(typeloader.getAttributes().get("class"));
              }
            }
          }
        }

        return new GosuPathEntry(rootDir, sourceDirs, typeloaderNames);
      } finally {
        is.close();
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
