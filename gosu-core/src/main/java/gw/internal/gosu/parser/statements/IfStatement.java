/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.statements;


import gw.internal.gosu.parser.Expression;
import gw.internal.gosu.parser.Statement;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.statements.IBreakStatement;
import gw.lang.parser.statements.IIfStatement;
import gw.lang.parser.statements.IStatementList;
import gw.lang.parser.statements.ITerminalStatement;


/**
 * Represents an if-statement as specified in the Gosu grammar:
 * <pre>
 * <i>if-statement</i>
 *   <b>if</b> <b>(</b> &lt;expression&gt; <b>)</b> &lt;statement&gt; [ <b>else</b> &lt;statement&gt; ] [ <b>unless</b> <b>(</b> &lt;expression&gt; <b>)</b> ]
 * </pre>
 * <p/>
 *
 * @see gw.lang.parser.IGosuParser
 */
public final class IfStatement extends Statement implements IIfStatement
{
  protected Expression _expression;
  protected Statement _statement;
  protected Statement _elseStatement;
  protected Expression _except;
  private ITerminalStatement[] _terminal;

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
   * @return The statement to execute if the conditional expression evaluates
   *         to true.
   */
  public Statement getStatement()
  {
    return _statement;
  }

  /**
   * @param statement The statement to execute if the conditional expression
   *                  evaluates to true.
   */
  public void setStatement( Statement statement )
  {
    _statement = statement;
  }

  /**
   * @return The else statement to execute if the conditional expression evaluates
   *         to false.
   */
  public Statement getElseStatement()
  {
    return _elseStatement;
  }

  /**
   * @return <tt>true</tt> if this if statement has an else statement.
   */
  public boolean hasElseStatement()
  {
    return _elseStatement != null;
  }

  /**
   * @param elseStatement The else statement to execute if the conditional expression
   *                      evaluates to false.
   */
  public void setElseStatement( Statement elseStatement )
  {
    _elseStatement = elseStatement;
  }

  /**
   * @return The exceptional conditional expression.
   * @deprecated
   */
  public Expression getExcept()
  {
    return _except;
  }
  /**
   * @deprecated
   */
  public void setExcept( Expression except )
  {
    _except = except;
  }

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
    if( _terminal == null )
    {
      _terminal = new ITerminalStatement[] {_getLeastSignificantTerminalStatement()};
    }
    return _terminal[0];
  }
  private ITerminalStatement _getLeastSignificantTerminalStatement()
  {
    if( getStatement() == null )
    {
      return null;
    }

    ITerminalStatement followingTerminal = null;
    if( getParent() instanceof IStatementList )
    {
      followingTerminal = ((StatementList)getParent()).getLeastSignificantTerminalStatementAfter( this );
    }

    ITerminalStatement ifStmtTerminal = getStatement().getLeastSignificantTerminalStatement();
    if( (ifStmtTerminal != null && _elseStatement != null) || followingTerminal != null )
    {
      ITerminalStatement elseStmtTerminal = _elseStatement == null ? null :_elseStatement.getLeastSignificantTerminalStatement();
      if( elseStmtTerminal != null || followingTerminal != null )
      {
        if( ifStmtTerminal instanceof IBreakStatement ||
            ifStmtTerminal instanceof ContinueStatement )
        {
          return ifStmtTerminal;
        }
        if( elseStmtTerminal instanceof IBreakStatement ||
            elseStmtTerminal instanceof ContinueStatement )
        {
          return elseStmtTerminal;
        }
        if( followingTerminal instanceof IBreakStatement ||
            followingTerminal instanceof ContinueStatement )
        {
          return followingTerminal;
        }
        // Return any one, doesn't matter because they are either return or throw.
        return ifStmtTerminal != null
               ? ifStmtTerminal
               : elseStmtTerminal != null
                 ? elseStmtTerminal
                 : followingTerminal;
      }
    }
    return null;
  }

  @Override
  public void setParent( IParsedElement rootElement )
  {
    super.setParent( rootElement );
    _terminal = null;
  }

  @Override
  public String toString()
  {
    String strElseStmt = getElseStatement() == null ? "" : ("else\n" + getElseStatement());

    //noinspection deprecation
    return "if( " + toString(getExpression()) + " )\n" +
           toString(getStatement()) +
           strElseStmt +
           (getExcept() == null ? "" : "\nunless( " + getExcept() + " )" );
  }
  
  private String toString(Object o) {
    return o == null ? "" : o.toString();
  }

}
