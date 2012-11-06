/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.lang.reflect;

import java.io.Serializable;

public interface IVisibilityModifierType extends Serializable
{
  public String getName();

  public String getDisplayName();

  public boolean isConstraint();
}
