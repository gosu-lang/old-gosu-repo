/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.plugin.ij.completion.proposals;

public interface ICompletionHasAdditionalSyntax {
  String getTrailingText();

  int getCaretOffsetFromEnd();
}
