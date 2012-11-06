/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.statements;

import gw.internal.gosu.parser.Expression;
import gw.internal.gosu.parser.Statement;
import gw.internal.gosu.parser.CannotExecuteGosuException;
import gw.internal.gosu.parser.expressions.Literal;
import gw.lang.parser.statements.IWhileStatement;
import gw.lang.parser.statements.ITerminalStatement;
import gw.config.CommonServices;


/**
 * Represents an while-statement as specified in the Gosu grammar:
 * <pre>
 * <i>while-statement</i>
 *   <b>while</b> <b>(</b> &lt;expression&gt; <b>)</b> &lt;statement&gt;
 * </pre>
 * <p/>
 *
 * @see gw.lang.parser.IGosuParser
 */
public final class WhileStatement extends LoopStatement implements IWhileStatement
{
  protected Expression _expression;
  protected Statement _statement;

  /**
   * @return The conditional expression.
   */
  public Expression getExpression()
  {
    return _expression;
  }

  /**
   * @param expression The conditional expression.
   */
  public void setExpression( Expression expression )
  {
    _expression = expression;
  }

  /**
   * @return The statement to execute while the conditional expression evaluates
   *         to true.
   */
  public Statement getStatement()
  {
    return _statement;
  }

  /**
   * @param statement The statement to execute while the conditional expression
   *                  evaluates to true.
   */
  public void setStatement( Statement statement )
  {
    _statement = statement;
  }

  /**
   * Execute the while statement
   */
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
    if( !(_expression instanceof Literal) ||
        !CommonServices.getCoercionManager().makePrimitiveBooleanFrom( getExpression().evaluate() ) )
    {
      return null;
    }
    if( _statement != null )
    {
      ITerminalStatement terminalStmt = _statement.getLeastSignificantTerminalStatement();
      if( terminalStmt instanceof ReturnStatement ||
          terminalStmt instanceof ThrowStatement )
      {
        return terminalStmt;
      }
    }
    return null;
  }

  @Override
  public String toString()
  {
    Expression expression = getExpression();
    Statement statement = getStatement();
    return "while( " + (expression != null ? expression.toString() : "") + " )\n" +
        (statement != null ? statement.toString(): "");
  }

}
