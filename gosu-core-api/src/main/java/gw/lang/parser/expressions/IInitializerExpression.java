/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.lang.parser.expressions;

import gw.lang.parser.IExpression;

public interface IInitializerExpression extends IExpression
{
  public void initialize( Object newObject );
}