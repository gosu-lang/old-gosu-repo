/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.lang.build;

import gw.fs.IDirectory;
import gw.fs.IDirectoryUtil;
import gw.fs.IFile;
import gw.fs.IResource;
import gw.fs.jar.JarFileDirectoryImpl;
import gw.lang.Gosu;
import gw.lang.GosuVersion;
import gw.test.util.ITCaseUtils;
import gw.test.util.ManifestVerifyUtil;
import org.fest.assertions.Assertions;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Brian Chang
 */
public class JarContentITCase extends Assert {

  private static File _gosuCoreApiSourcesJar;
  private static DistAssemblyUtil _assembly;

  @BeforeClass
  public static void beforeTestClass() throws Exception {
    _assembly = DistAssemblyUtil.getInstance();
    _gosuCoreApiSourcesJar = new File(_assembly.getPom().getParentFile().getParent(), "gosu-core-api/target/gosu-core-api-" + _assembly.getGosuVersion() + "-sources.jar");
  }

  @Test
  public void testGosuCoreApiJar() {
    IDirectory dir = getGosuCoreApiJar();
    Assertions.assertThat(toNamesSorted(dir.listDirs())).containsExactly("META-INF", "gw");
    assertGosuCoreApiShades(dir, true);
    assertGosuCoreApiFiles(dir, true);
    ManifestVerifyUtil manifestUtil = new ManifestVerifyUtil(dir, getVersion());
    manifestUtil.assertManifestImplementationEntries();
    manifestUtil.assertManifestContainsSourcesEntry(dir, "gs,gsx");
  }

  private IDirectory getGosuCoreApiJar() {
    return getJar("gosu-core-api");
  }

  @Test
  public void testGosuCoreJar() {
    IDirectory dir = getGosuCoreJar();
    Assertions.assertThat(toNamesSorted(dir.listDirs())).containsExactly("META-INF", "gw");
    assertGosuCoreApiShades(dir, false);
    assertGosuCoreShades(dir, true);
    assertGosuCoreApiFiles(dir, false);
    assertGosuCoreFiles(dir, true);
    ManifestVerifyUtil manifestUtil = new ManifestVerifyUtil(dir, getVersion());
    manifestUtil.assertManifestImplementationEntries();
    manifestUtil.assertManifestContainsSourcesEntry(dir, null);
  }

  private IDirectory getGosuCoreJar() {
    return getJar("gosu-core");
  }

  @Test
  public void testNoOverlapsAmongShadedJars() {
    IDirectory[] shadedJars = new IDirectory[] {
            getGosuCoreApiJar(),
            getGosuCoreJar()
    };
    TreeMap<String, List<String>> collectedResources = new TreeMap<String, List<String>>();
    for (IDirectory shadedJar : shadedJars) {
      collectResources(shadedJar, shadedJar, collectedResources);
    }
    boolean fail = false;
    for (Map.Entry<String, List<String>> resourceEntry : collectedResources.entrySet()) {
      if (resourceEntry.getValue().size() != 1
              && !resourceEntry.getKey().startsWith("META-INF/")
              && !resourceEntry.getKey().equals("internal/xml/xsd-codegen.xml")) {
        System.out.println(resourceEntry);
        fail = true;
      }
    }
    if (fail) {
      fail("shaded jars have unapproved overlapping resources - see log above for details");
    }
  }

  private void collectResources(IDirectory root, IDirectory dir, TreeMap<String, List<String>> collectedResources) {
    for (IFile file : dir.listFiles()) {
      String resourceName = IDirectoryUtil.relativePath(root, file);
      List<String> jarList = collectedResources.get(resourceName);
      if (jarList == null) {
        jarList = new ArrayList<String>(1);
        collectedResources.put(resourceName, jarList);
      }
      jarList.add(root.getName());
    }
    for (IDirectory subDir : dir.listDirs()) {
      collectResources(root, subDir, collectedResources);
    }
  }

  private void assertGosuCoreApiFiles(IDirectory dir, boolean expected) {
    assertEquals(expected, dir.file(Gosu.class.getName().replace(".", "/") + ".class").exists());
    assertEquals(expected, dir.file("gw/util/OSType.gs").exists());
  }

  private void assertGosuCoreFiles(IDirectory dir, boolean expected) {
    assertEquals(expected, dir.file("gw/internal/gosu/module/Module.class").exists());
  }

  private void assertGosuCoreApiShades(IDirectory dir, boolean expected) {
    assertFalse(dir.dir("gw/lang/launch").exists());
    assertEquals(expected, dir.dir("gw/internal/ext/org/apache/commons/cli").exists());
  }

  private void assertGosuCoreShades(IDirectory dir, boolean expected) {
    assertEquals(expected, dir.dir("gw/internal/ext/org/antlr").exists());
    assertEquals(expected, dir.dir("gw/internal/ext/org/objectweb/asm").exists());
  }

  private static String getVersion() {
    GosuVersion version = GosuVersion.parse(_assembly.getGosuVersion());
    return version.toString();
  }

  private IDirectory getJar(String name) {
    File jar = _assembly.getJar(name);
    return new JarFileDirectoryImpl(jar);
  }

  private List<String> toNamesSorted(List<? extends IResource> dirs) {
    return ITCaseUtils.toNamesSorted(dirs);
  }

}
