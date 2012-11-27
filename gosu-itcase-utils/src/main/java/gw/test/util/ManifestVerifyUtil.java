package gw.test.util;

import gw.fs.IDirectory;
import gw.fs.IDirectoryUtil;
import gw.fs.IFile;
import gw.util.DynamicArray;
import gw.util.StreamUtil;
import org.fest.assertions.Assertions;
import org.junit.Assert;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.jar.Manifest;

/**
 * Created with IntelliJ IDEA.
 * User: bchang
 * Date: 11/26/12
 * Time: 4:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class ManifestVerifyUtil extends Assert {
  private static final String GOSU_TYPELOADERS_ATTR_NAME = "Gosu-Typeloaders";
  private final Manifest _mf;
  private final String _expectedVersion;

  public ManifestVerifyUtil(IDirectory dir, String expectedVersion) {
    InputStream in = null;
    try {
      in = dir.file("META-INF/MANIFEST.MF").openInputStream();
      _mf = new Manifest(in);
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
    finally {
      try {
        StreamUtil.close(in);
      } catch (IOException e) {
        // ignore
      }
    }
    _expectedVersion = expectedVersion;
  }

  public void assertManifestImplementationEntries() {
    assertTrue(_mf.getMainAttributes().getValue("Implementation-Vendor-Id").startsWith("org.gosu-lang.gosu"));
    assertEquals(_expectedVersion, _mf.getMainAttributes().getValue("Implementation-Version"));
  }

  public void assertManifestContainsSourcesEntry(IDirectory dir, String expectedSources) {
    HashSet<String> found = new HashSet<String>();
    DynamicArray<? extends IFile> files = IDirectoryUtil.allContainedFilesExcludingIgnored(dir);
    for (IFile file : files) {
      String extension = file.getExtension();
      if (extension.equals("gs") || extension.equals("gsx") || extension.equals("xsd")) {
        found.add(extension);
      }
    }
    List<String> foundExtensions = new ArrayList<String>(found);
    Collections.sort(foundExtensions);

    if (expectedSources != null) {
      List<String> expectedSourceExtensions = Arrays.asList(expectedSources.split(","));
      Assertions.assertThat(foundExtensions)
              .as("the set of extensions in the manifest (Contains-Sources) don't match the set found in the jar")
              .isEqualTo(expectedSourceExtensions);

      assertEquals(expectedSources, _mf.getMainAttributes().getValue("Contains-Sources"));
    }
    else {
      Assertions.assertThat(foundExtensions).isEmpty();
      assertNull(_mf.getMainAttributes().getValue("Contains-Sources"));
    }
  }

  public void assertTypeloaderEntry(String typeLoaderFqn) {
    assertEquals(typeLoaderFqn, _mf.getMainAttributes().getValue(GOSU_TYPELOADERS_ATTR_NAME));
  }
}
