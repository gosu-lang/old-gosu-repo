/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang;

/**
 */
public class GosuTemplateLexer extends GosuLexer {
  @Override
  protected boolean isForTemplate() {
    return true;
  }
}
