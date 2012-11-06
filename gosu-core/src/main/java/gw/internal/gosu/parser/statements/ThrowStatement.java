/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.statements;


import gw.internal.gosu.parser.Expression;
import gw.internal.gosu.parser.CannotExecuteGosuException;

import gw.lang.parser.statements.IThrowStatement;
import gw.lang.parser.statements.ITerminalStatement;


/**
 * Represents a throw-statement as specified in the Gosu grammar:
 * <pre>
 * <i>throw-statement</i>
 *   <b>throw</b> &lt;expression&gt;
 * </pre>
 * <p/>
 *
 * @see gw.lang.parser.IGosuParser
 */
public final class ThrowStatement extends TerminalStatement implements IThrowStatement
{
  private Expression _expression;


  public Expression getExpression()
  {
    return _expression;
  }

  public void setExpression( Expression expression )
  {
    _expression = expression;
  }

  public Object execute()
  {
    if( !isCompileTimeConstant() )
    {
      return super.execute();
    }
    
    throw new CannotExecuteGosuException();
  }

  @Override
  public ITerminalStatement getLeastSignificantTerminalStatement()
  {
    return this;
  }

  @Override
  public String toString()
  {
    return "throw " + getExpression().toString();
  }

}
