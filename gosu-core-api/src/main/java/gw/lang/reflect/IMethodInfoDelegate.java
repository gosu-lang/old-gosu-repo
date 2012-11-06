/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.lang.reflect;

public interface IMethodInfoDelegate extends IFeatureInfoDelegate, IMethodInfo
{
  public IMethodInfo getSource();
}