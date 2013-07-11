/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.statements;

import gw.internal.gosu.parser.Statement;
import gw.lang.parser.statements.ITerminalStatement;
import gw.lang.parser.statements.IUsesStatement;
import gw.lang.parser.statements.IUsesStatementList;

import java.util.List;

public class UsesStatementList extends Statement implements IUsesStatementList
{
  private List<IUsesStatement> _stmts;

  public UsesStatementList()
  {
  }

  public List<IUsesStatement> getUsesStatements()
  {
    return _stmts;
  }

  public void setUsesStatements( List<IUsesStatement> stmts )
  {
    _stmts = stmts;
  }

  public Object execute()
  {
    // no-op
    return Statement.VOID_RETURN_VALUE;
  }

  @Override
  protected ITerminalStatement getLeastSignificantTerminalStatement_internal( boolean[] bAbsolute )
  {
    bAbsolute[0] = false;
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
    StringBuilder sb = new StringBuilder();
    if( _stmts != null ) {
      for( IUsesStatement stmt : _stmts ) {
        sb.append( stmt.toString() + "\n" );
      }
    }
    return sb.toString();
  }

}
