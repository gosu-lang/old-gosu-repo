/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.lang.parser;

import gw.lang.PublishInGosu;
import gw.lang.Scriptable;

@PublishInGosu
public interface IDeclarationSiteValidator
{
  /**
   * Called after the whole class has been defn compiled.
   * Implementors of this method can inspect the parse tree and add warnings/errors as appropriate
   *
   * @param feature the parsed element that this annotation lives on.
   */
  @Scriptable
  public void validate( IParsedElement feature );
}
