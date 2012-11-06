/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.lang.parser;

import gw.lang.reflect.IType;
import gw.lang.reflect.gs.IGosuClass;

public interface IReducedDynamicPropertySymbol extends IReducedSymbol
{
  boolean isReadable();

  IReducedDynamicFunctionSymbol getGetterDfs();

  IReducedDynamicFunctionSymbol getSetterDfs();

  IReducedDynamicPropertySymbol getParent();

  IReducedDynamicFunctionSymbol getFunction(CaseInsensitiveCharSequence strFunctionName);

  CaseInsensitiveCharSequence getVarIdentifier();
}
