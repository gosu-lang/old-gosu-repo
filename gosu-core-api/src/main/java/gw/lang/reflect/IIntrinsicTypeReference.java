/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.lang.reflect;

public interface IIntrinsicTypeReference
{
  /**
   * The type of this feature e.g., for a property this is the property's type.
   */
  public IType getFeatureType();
}
