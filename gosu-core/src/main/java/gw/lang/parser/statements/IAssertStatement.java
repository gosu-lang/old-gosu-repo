package gw.lang.parser.statements;

import gw.lang.parser.IExpression;

public interface IAssertStatement extends ITerminalStatement
{
  IExpression getCondition();
  IExpression getDetail();
}
