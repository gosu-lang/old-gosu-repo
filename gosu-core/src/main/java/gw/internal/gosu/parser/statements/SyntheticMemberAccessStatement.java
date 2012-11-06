/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.statements;

import gw.internal.gosu.parser.Statement;
import gw.lang.parser.expressions.ISynthesizedMemberAccessExpression;
import gw.lang.parser.statements.ISyntheticMemberAccessStatement;
import gw.lang.parser.statements.ITerminalStatement;


public class SyntheticMemberAccessStatement extends Statement implements ISyntheticMemberAccessStatement
{
  private ISynthesizedMemberAccessExpression _expression;

  public SyntheticMemberAccessStatement(ISynthesizedMemberAccessExpression expression) {
    _expression = expression;
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
    return null;
  }

  @Override
  public String toString()
  {
    return _expression.toString();
  }

  public ISynthesizedMemberAccessExpression getMemberAccessExpression() {
    return _expression;
  }
}
