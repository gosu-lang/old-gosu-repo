/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.plugin.ij.formatting;

import com.intellij.formatting.Spacing;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import org.jetbrains.annotations.NotNull;

public class GosuSpaces {
  public static final Spacing EMPTY = Spacing.createSpacing(0, 0, 0, false, 0);
  public static final Spacing LINE_FEED = Spacing.createSpacing(0, Integer.MAX_VALUE, 1, false, 0);

  public static Spacing getSpace(boolean hasSpace, @NotNull CommonCodeStyleSettings settings) {
    final boolean keepLineBreaks = settings.KEEP_LINE_BREAKS;
    return hasSpace ? Spacing.createSpacing(1, 1, 0, keepLineBreaks, 0) : Spacing.createSpacing(0, 0, 0, keepLineBreaks, 0);
  }

  public static Spacing blankLines(int blankLines) {
    return Spacing.createSpacing(1, Integer.MAX_VALUE, blankLines + 1, false, 0);
  }

  private GosuSpaces() {
  }
}

