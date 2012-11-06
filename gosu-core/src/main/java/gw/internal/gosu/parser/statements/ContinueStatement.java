/*
 * Copyright 2012. Guidewire Software, Inc.
 */
package gw.internal.gosu.parser.statements;

import gw.lang.parser.statements.IContinueStatement;
import gw.lang.parser.statements.ITerminalStatement;


/**
 * Represents a continue statement as specified in the Gosu grammar:
 *
 * @see gw.lang.parser.IGosuParser
 */
public final class ContinueStatement extends TerminalStatement implements IContinueStatement
{
  public Object execute()
  {
    if( !isCompileTimeConstant() )
    {
      return super.execute();
    }

    throw new IllegalStateException( "Can't execute this parsed element directly" );
  }

  @Override
  public ITerminalStatement getLeastSignificantTerminalStatement()
  {
    return this;
  }

  @Override
  public String toString()
  {
    return "continue";
  }

}
