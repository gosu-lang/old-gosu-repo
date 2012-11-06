/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.lang.parser.expressions;

public interface IWhereClauseRelationalExpression extends IConditionalExpression, IQueryPartAssembler
{
  String getOperator();
}
