/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.lang.parser.expressions;

public interface INumericLiteralExpression extends ILiteralExpression, Cloneable
{
  Number getValue();
}
