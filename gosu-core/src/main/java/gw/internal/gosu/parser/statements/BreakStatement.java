/*
 * Copyright 2012. Guidewire Software, Inc.
 */
package gw.internal.gosu.parser.statements;

import gw.lang.parser.statements.IBreakStatement;
import gw.lang.parser.statements.ITerminalStatement;
import gw.internal.gosu.parser.CannotExecuteGosuException;

/**
 * Represents a break statement as specified in the Gosu grammar:
 *
 * @see gw.lang.parser.IGosuParser
 */
public final class BreakStatement extends TerminalStatement implements IBreakStatement
{
  public Object execute()
  {
    if( !isCompileTimeConstant() )
    {
      return super.execute();
    }

    throw new CannotExecuteGosuException();
  }

  @Override
  public String toString()
  {
    return "break";
  }

  @Override
  public ITerminalStatement getLeastSignificantTerminalStatement()
  {
    return this;
  }

}
