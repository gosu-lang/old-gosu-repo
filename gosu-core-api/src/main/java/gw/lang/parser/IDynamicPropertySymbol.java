/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.lang.parser;

import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.gs.IGosuClass;

public interface IDynamicPropertySymbol extends IDynamicSymbol
{
  boolean isReadable();

  IDynamicFunctionSymbol getGetterDfs();

  IDynamicFunctionSymbol getSetterDfs();

  IDynamicPropertySymbol getParent();

  IDynamicFunctionSymbol getFunction( CaseInsensitiveCharSequence strFunctionName );

  CaseInsensitiveCharSequence getVarIdentifier();

  String getFullDescription();

  IDynamicPropertySymbol getParameterizedVersion( IGosuClass gsClass );

  boolean isStatic();

  void setGetterDfs( IDynamicFunctionSymbol dfsGetter );

  void setSetterDfs( IDynamicFunctionSymbol dfsSetter );

  IFeatureInfo getPropertyInfo();
}
