/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.lang.parser.expressions;

import gw.lang.parser.IExpression;
import gw.lang.reflect.IBlockType;

public interface IBlockInvocation extends IExpression
{
  public IBlockType getBlockType();
}