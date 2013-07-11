/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.lang.parser.expressions;

public interface IIntervalExpression extends IBinaryExpression
{
  boolean isLeftClosed();
  boolean isRightClosed();
}