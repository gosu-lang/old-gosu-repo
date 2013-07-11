/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.lang.reflect;

public interface IMetaType extends INonLoadableType
{
  IType getType();

  boolean isLiteral();
}
