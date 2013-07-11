/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.lang.reflect.gs;

import gw.lang.parser.IReducedDynamicPropertySymbol;
import gw.lang.reflect.*;

public interface IGosuPropertyInfo extends IAttributedFeatureInfo, IPropertyInfo, IGenericMethodInfo, IMethodBackedPropertyInfo
{
  String getShortDescription();

  IReducedDynamicPropertySymbol getDps();

  IType getContainingType();

}
