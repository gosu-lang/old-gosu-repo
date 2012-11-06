/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.util;

import gw.config.CommonServices;
import gw.fs.IDirectory;
import gw.lang.reflect.module.IModule;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * Utility class to scan for certain manifest headers through the classpath.
 */
public final class Extensions {
  public static final String CONTAINS_SOURCES = "Contains-Sources";

  public static List<String> getExtensions(File path, String headerName) {
    List<String> extensions = new ArrayList<String>();
    getExtensions(extensions, path, headerName);
    return extensions;
  }

  public static void getExtensions(Collection<String> result, File path, String headerName) {
    if (path.getName().endsWith(".jar")) {
      getExtensionsForJar(result, path, headerName);
    } else {
      getExtensionsForDirectory(result, path, headerName);
    }
  }

  private static void getExtensionsForJar(Collection<String> result, File locationFile, String headerName) {
    if (!locationFile.exists()) {
      return;
    }
    JarFile jar = null;
    try {
      jar = new JarFile(locationFile);
      Manifest manifest = jar.getManifest();
      if (manifest != null) {
        scanManifest(result, manifest, headerName);
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      if (jar != null) {
        try {
          jar.close();
        } catch (IOException e) {
          // Ignore
        }
      }
    }
  }

  private static void getExtensionsForDirectory(Collection<String> result, File locationFile, String headerName) {
    File manifestFile = new File(locationFile, "META-INF/MANIFEST.MF");
    if (!manifestFile.exists()) {
      return;
    }
    InputStream in = null;
    try {
      in = new FileInputStream(manifestFile);
      Manifest manifest = new Manifest(in);
      scanManifest(result, manifest, headerName);
    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      if (in != null) {
        try {
          in.close();
        } catch (IOException e) {
          // Ignore
        }
      }
    }
  }

  private static void scanManifest(Collection<String> result, Manifest manifest, String headerName) {
    Attributes mainAttributes = manifest.getMainAttributes();
    String valueList = mainAttributes.getValue(headerName);
    if (valueList != null) {
      for (String val : valueList.split(",")) {
        result.add(GosuStringUtil.strip(val));
      }
    }
  }

  public static List<IDirectory> getJarsWithSources(List<String> fileExtensions, IModule module) {
    List<IDirectory> jars = new ArrayList<IDirectory>();
    for (String cpEntry : module.getJavaClassPath()) {
      try {
        IDirectory root = CommonServices.getFileSystem().getIDirectory(new File(cpEntry).toURI().toURL());
        List<String> extensions = new ArrayList<String>();
        Extensions.getExtensions(extensions, root.toJavaFile(), CONTAINS_SOURCES);
        extensions.retainAll(fileExtensions);
        if (!extensions.isEmpty()) {
          jars.add(root);
        }
      } catch (MalformedURLException e) {
        throw new RuntimeException(e);
      }
    }
    return jars;
  }

  private Extensions() {
    // No instances.
  }

}
