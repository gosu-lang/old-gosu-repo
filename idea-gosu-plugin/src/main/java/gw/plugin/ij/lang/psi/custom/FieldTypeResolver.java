/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.custom;

import gw.lang.reflect.IType;

public interface FieldTypeResolver {

  IType resolve(IType type, String fieldName);

}
