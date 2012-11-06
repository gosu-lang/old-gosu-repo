/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.statement;

import gw.internal.gosu.parser.ParseTree;
import gw.lang.ir.IRExpression;
import gw.lang.ir.IRStatement;
import gw.lang.ir.statement.IRDoWhileStatement;
import gw.internal.gosu.ir.transform.ExpressionTransformer;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.internal.gosu.parser.statements.DoWhileStatement;

public class DoWhileStatementTransformer extends AbstractStatementTransformer<DoWhileStatement>
{
  public static IRStatement compile( TopLevelTransformationContext cc, DoWhileStatement stmt )
  {
    DoWhileStatementTransformer compiler = new DoWhileStatementTransformer( cc, stmt );
    return compiler.compile();
  }

  private DoWhileStatementTransformer( TopLevelTransformationContext cc, DoWhileStatement stmt )
  {
    super( cc, stmt );
  }

  @Override
  protected IRStatement compile_impl()
  {
    // Push a scope in case while body not a statement list
    _cc().pushScope( false );
    try
    {
      IRDoWhileStatement doWhileLoop = new IRDoWhileStatement();
      // execute body
      doWhileLoop.setBody( _cc().compile(_stmt().getStatement() ) );

      // loop test
      IRExpression loopTest = ExpressionTransformer.compile( _stmt().getExpression(), _cc() );
      ParseTree location = _stmt().getExpression().getLocation();
      if( location != null )
      {
        loopTest.setLineNumber( location.getLineNum() );
      }
      doWhileLoop.setLoopTest( loopTest );

      return doWhileLoop;
    }
    finally
    {
      _cc().popScope();
    }
  }
}