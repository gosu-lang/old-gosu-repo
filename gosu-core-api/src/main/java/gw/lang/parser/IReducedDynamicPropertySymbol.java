/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.lang.parser;

public interface IReducedDynamicPropertySymbol extends IReducedSymbol
{
  boolean isReadable();

  IReducedDynamicFunctionSymbol getGetterDfs();

  IReducedDynamicFunctionSymbol getSetterDfs();

  IReducedDynamicPropertySymbol getParent();

  IReducedDynamicFunctionSymbol getFunction( String strFunctionName);

  String getVarIdentifier();
}
