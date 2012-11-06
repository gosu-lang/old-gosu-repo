/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.lang.parser.statements;

import gw.lang.parser.IStatement;
import gw.lang.parser.IExpression;

public interface INotAStatement extends IStatement
{
  IExpression getExpression();
}
