/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.lang.reflect;

public interface IBlockType extends IFunctionType, IGenericMethodInfo
{
  String getRelativeNameSansBlock();

  String getRelativeParamSignature( boolean bSansBlock );
}
