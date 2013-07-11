/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.plugin.ij.util;

import com.intellij.openapi.progress.ProcessCanceledException;
import org.jetbrains.annotations.Nullable;

public class ExceptionUtil {

  public static boolean isWrappedCanceled(@Nullable Throwable cause) {
    while (cause != null) {
      if (cause instanceof ProcessCanceledException) {
        return true;
      }
      cause = cause.getCause();
    }
    return false;
  }

}

