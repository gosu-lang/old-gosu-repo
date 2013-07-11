/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang;

import gw.lang.parser.IParsedElement;

public interface IGosuElementType {
  Class<? extends IParsedElement> getParsedElementType();
}
