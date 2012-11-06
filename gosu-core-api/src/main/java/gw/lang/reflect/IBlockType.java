/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.lang.reflect;

import gw.lang.parser.CaseInsensitiveCharSequence;

public interface IBlockType extends IFunctionType, IGenericMethodInfo
{
  String getRelativeNameSansBlock();

  CaseInsensitiveCharSequence getRelativeParamSignature( boolean bSansBlock );
}
