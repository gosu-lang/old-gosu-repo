/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.lang.reflect.gs;

import gw.lang.parser.CaseInsensitiveCharSequence;
import gw.lang.parser.IDynamicPropertySymbol;
import gw.lang.parser.IReducedDynamicPropertySymbol;
import gw.lang.reflect.*;

public interface IGosuPropertyInfo extends IAttributedFeatureInfo, IPropertyInfo, IGenericMethodInfo, IMethodBackedPropertyInfo
{
  String getShortDescription();

  IReducedDynamicPropertySymbol getDps();

  IType getContainingType();

}
