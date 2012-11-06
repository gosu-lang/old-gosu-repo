/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.statements;

import gw.internal.gosu.parser.Expression;
import gw.internal.gosu.parser.Statement;
import gw.internal.gosu.parser.CannotExecuteGosuException;
import gw.lang.parser.statements.IBreakStatement;
import gw.lang.parser.statements.ISwitchStatement;
import gw.lang.parser.statements.ITerminalStatement;

import java.util.List;


/**
 * Represents a switch-statement as specified in the Gosu grammar:
 * <pre>
 * <i>switch-statement</i>
 *   <b>switch</b> <b>(</b>&lt;expression&gt;<b>) {</b> [switch-cases] [switch-default] <b>}</b>
 * <p/>
 * <i>switch-cases</i>
 *   &lt;switch-case&gt;
 *   &lt;switch-cases&gt; &lt;switch-case&gt;
 * <p/>
 * <i>switch-case</i>
 *   <b>case</b> &lt;expression&gt; <b>:</b> [statement-list]
 * <p/>
 * <i>switch-default</i>
 *   <b>default</b> <b>:</b> [statement-list]
 * </pre>
 * <p/>
 *
 * @see gw.lang.parser.IGosuParser
 */
public final class SwitchStatement extends Statement implements ISwitchStatement
{
  protected Expression _switchExpression;
  protected CaseClause[] _cases;
  protected List<Statement> _defaultStatements;

  public Expression getSwitchExpression()
  {
    return _switchExpression;
  }

  public void setSwitchExpression( Expression switchExpression )
  {
    _switchExpression = switchExpression;
  }

  public CaseClause[] getCases()
  {
    return _cases;
  }

  public void setCases( CaseClause[] cases )
  {
    _cases = cases;
  }

  public List<Statement> getDefaultStatements()
  {
    return _defaultStatements;
  }

  public void setDefaultStatements( List<Statement> defaultStatements )
  {
    _defaultStatements = defaultStatements;
  }

  /**
   * Execute the switch statement
   */
  public Object execute()
  {
    if( !isCompileTimeConstant() )
    {
      return super.execute();
    }

    throw new CannotExecuteGosuException();
  }

  /**
   * The switch statement has a non-null terminal stmt iff:
   * 1) There are no case stmts or all the the case stmts have non-break terminator and
   * 2) The default stmt exists and has a non-break terminator
   */
  @Override
  public ITerminalStatement getLeastSignificantTerminalStatement()
  {
    if( _defaultStatements == null || _defaultStatements.isEmpty() )
    {
      return null;
    }
    ContinueStatement caseStmtContinue = null;
    if( _cases != null )
    {
      outer:
      for( int i = 0; i < _cases.length; i++ )
      {
        List caseStatements = _cases[i].getStatements();
        if( caseStatements != null && caseStatements.size() > 0 )
        {
          for( int iStmt = 0; iStmt < caseStatements.size(); iStmt++ )
          {
            ITerminalStatement terminalStmt = ((Statement)caseStatements.get( iStmt )).getLeastSignificantTerminalStatement();
            if( terminalStmt != null && !(terminalStmt instanceof IBreakStatement) )
            {
              if( terminalStmt instanceof ContinueStatement )
              {
                caseStmtContinue = (ContinueStatement)terminalStmt;
              }
              continue outer;
            }
          }
          return null;
        }
      }
    }
    for( int i = 0; i < _defaultStatements.size(); i++ )
    {
      ITerminalStatement terminalStmt = _defaultStatements.get( i ).getLeastSignificantTerminalStatement();
      if( terminalStmt != null && !(terminalStmt instanceof IBreakStatement) )
      {
        return caseStmtContinue != null ? caseStmtContinue : terminalStmt;
      }
    }
    return null;
  }

  @Override
  public String toString()
  {
    String strRet = "switch( " + getSwitchExpression().toString() + " )\n {\n";
    if( _cases != null )
    {
      for( int i = 0; i < _cases.length; i++ )
      {
        strRet += "case " + _cases[i].getExpression().toString() + ":\n";
        List caseStatements = _cases[i].getStatements();
        if( caseStatements != null )
        {
          for( int iStmt = 0; iStmt < caseStatements.size(); iStmt++ )
          {
            strRet += (caseStatements.get( iStmt )).toString();
          }
        }
      }

      if( _defaultStatements != null )
      {
        strRet += "default:\n";
        for( int iStmt = 0; iStmt < _defaultStatements.size(); iStmt++ )
        {
          strRet += (_defaultStatements.get( iStmt )).toString();
        }
      }
    }
    strRet += "\n}";
    return strRet;
  }


}
