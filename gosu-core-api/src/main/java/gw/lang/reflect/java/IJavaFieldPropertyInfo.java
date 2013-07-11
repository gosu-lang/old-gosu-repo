/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.lang.reflect.java;

import gw.lang.reflect.IAttributedFeatureInfo;
import gw.lang.reflect.IPropertyInfo;

public interface IJavaFieldPropertyInfo extends IAttributedFeatureInfo, IPropertyInfo, IJavaBasePropertyInfo, ICompileTimeConstantValue
{
  IJavaClassField getField();
}
