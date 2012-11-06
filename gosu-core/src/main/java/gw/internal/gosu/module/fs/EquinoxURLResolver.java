/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.internal.gosu.module.fs;

import org.eclipse.core.runtime.FileLocator;

import java.io.IOException;
import java.net.URL;

/**
 * Resolves bundleresource: URLs (Equinox-specific) into jar: or file: URLs.
 */
public class EquinoxURLResolver {
  public static URL resolveBundleResource(URL url) {
    try {
      return FileLocator.resolve(url);
    } catch (IOException e) {
      throw new RuntimeException("Cannot resolve bundleresource: URL", e);
    }
  }
}


