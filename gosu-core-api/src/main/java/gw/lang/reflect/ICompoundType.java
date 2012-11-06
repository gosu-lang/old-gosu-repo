/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.lang.reflect;

import java.util.Set;

public interface ICompoundType extends IType
{
  Set<IType> getTypes();
}
