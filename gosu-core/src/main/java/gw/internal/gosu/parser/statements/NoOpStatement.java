/*
 * Copyright 2012. Guidewire Software, Inc.
 */
package gw.internal.gosu.parser.statements;

import gw.internal.gosu.parser.Statement;
import gw.lang.parser.statements.INoOpStatement;
import gw.lang.parser.statements.ITerminalStatement;

/**
 * Represents a noop statement as specified in the Gosu grammar:
 *
 * @see gw.lang.parser.IGosuParser
 */
public class NoOpStatement extends Statement implements INoOpStatement
{
  public NoOpStatement()
  {
    super();
  }

  public Object execute()
  {
    return Statement.VOID_RETURN_VALUE;
  }

  @Override
  public ITerminalStatement getLeastSignificantTerminalStatement()
  {
    return null;
  }

  @Override
  public boolean isNoOp()
  {
    return true;
  }

  @Override
  public String toString()
  {
    return "";
  }

}
