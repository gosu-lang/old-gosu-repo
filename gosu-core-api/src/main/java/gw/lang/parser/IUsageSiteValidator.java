/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.lang.parser;

import gw.lang.PublishInGosu;
import gw.lang.Scriptable;

@PublishInGosu
public interface IUsageSiteValidator
{
  /**
   * Called after the Gosu source corresponding to the parsed element is fully parsed.
   *
   * @param pe The parsed element to validate.
   */
  @Scriptable
  public void validate( IParsedElement pe );
}
