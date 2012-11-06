/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.lang.parser.expressions;

public interface IWhereClauseEqualityExpression extends IConditionalExpression, IQueryPartAssembler
{
  boolean isEquals();
}
