/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform;

import gw.internal.gosu.ir.transform.statement.EvalStatementTransformer;
import gw.internal.gosu.parser.statements.EvalStatement;
import gw.internal.gosu.parser.statements.UsesStatementList;
import gw.lang.ir.IRExpression;
import gw.lang.ir.IRStatement;
import gw.internal.gosu.ir.transform.statement.ArrayAssignmentStatementTransformer;
import gw.internal.gosu.ir.transform.statement.AssignmentStatementTransformer;
import gw.internal.gosu.ir.transform.statement.BeanMethodCallStatementTransformer;
import gw.internal.gosu.ir.transform.statement.BreakStatementTransformer;
import gw.internal.gosu.ir.transform.statement.ContinueStatementTransformer;
import gw.internal.gosu.ir.transform.statement.DoWhileStatementTransformer;
import gw.internal.gosu.ir.transform.statement.ForEachStatementTransformer;
import gw.internal.gosu.ir.transform.statement.IfStatementTransformer;
import gw.internal.gosu.ir.transform.statement.InitializerAssignmentTransformer;
import gw.internal.gosu.ir.transform.statement.MapAssignmentStatementTransformer;
import gw.internal.gosu.ir.transform.statement.MemberAssignmentStatementTransformer;
import gw.internal.gosu.ir.transform.statement.MethodCallStatementTransformer;
import gw.internal.gosu.ir.transform.statement.ReturnStatementTransformer;
import gw.internal.gosu.ir.transform.statement.StatementListTransformer;
import gw.internal.gosu.ir.transform.statement.SwitchStatementTransformer;
import gw.internal.gosu.ir.transform.statement.SyntheticMemberAccessStatementTransformer;
import gw.internal.gosu.ir.transform.statement.ThrowStatementTransformer;
import gw.internal.gosu.ir.transform.statement.TryCatchFinallyStatementTransformer;
import gw.internal.gosu.ir.transform.statement.UsingStatementTransformer;
import gw.internal.gosu.ir.transform.statement.VarStatementTransformer;
import gw.internal.gosu.ir.transform.statement.WhileStatementTransformer;
import gw.internal.gosu.ir.transform.statement.BlockInvocationStatementTransformer;
import gw.internal.gosu.parser.expressions.InitializerAssignment;
import gw.internal.gosu.parser.statements.ArrayAssignmentStatement;
import gw.internal.gosu.parser.statements.AssignmentStatement;
import gw.internal.gosu.parser.statements.BeanMethodCallStatement;
import gw.internal.gosu.parser.statements.BreakStatement;
import gw.internal.gosu.parser.statements.ContinueStatement;
import gw.internal.gosu.parser.statements.DoWhileStatement;
import gw.internal.gosu.parser.statements.ForEachStatement;
import gw.internal.gosu.parser.statements.IfStatement;
import gw.internal.gosu.parser.statements.MapAssignmentStatement;
import gw.internal.gosu.parser.statements.MemberAssignmentStatement;
import gw.internal.gosu.parser.statements.MethodCallStatement;
import gw.internal.gosu.parser.statements.NoOpStatement;
import gw.internal.gosu.parser.statements.ReturnStatement;
import gw.internal.gosu.parser.statements.StatementList;
import gw.internal.gosu.parser.statements.SwitchStatement;
import gw.internal.gosu.parser.statements.SyntheticFunctionStatement;
import gw.internal.gosu.parser.statements.SyntheticMemberAccessStatement;
import gw.internal.gosu.parser.statements.ThrowStatement;
import gw.internal.gosu.parser.statements.TryCatchFinallyStatement;
import gw.internal.gosu.parser.statements.UsesStatement;
import gw.internal.gosu.parser.statements.UsingStatement;
import gw.internal.gosu.parser.statements.VarStatement;
import gw.internal.gosu.parser.statements.WhileStatement;
import gw.internal.gosu.parser.statements.BlockInvocationStatement;
import gw.lang.ir.statement.IRNoOpStatement;
import gw.lang.parser.IStatement;

/**
 */
public class StatementTransformer
{
  public static IRStatement compile( TopLevelTransformationContext context, IStatement stmt )
  {
    try
    {
      if( stmt instanceof StatementList )
      {
        return StatementListTransformer.compile( context, (StatementList)stmt );
      }
      else if( stmt instanceof VarStatement )
      {
        return VarStatementTransformer.compile( context, (VarStatement)stmt );
      }
      else if( stmt instanceof AssignmentStatement )
      {
        return AssignmentStatementTransformer.compile( context, (AssignmentStatement)stmt );
      }
      else if( stmt instanceof MemberAssignmentStatement )
      {
        return MemberAssignmentStatementTransformer.compile( context, (MemberAssignmentStatement)stmt );
      }
      else if( stmt instanceof ArrayAssignmentStatement )
      {
        return ArrayAssignmentStatementTransformer.compile( context, (ArrayAssignmentStatement)stmt );
      }
      else if( stmt instanceof MapAssignmentStatement)
      {
        return MapAssignmentStatementTransformer.compile( context, (MapAssignmentStatement)stmt );
      }
      else if( stmt instanceof MethodCallStatement )
      {
        return MethodCallStatementTransformer.compile( context, (MethodCallStatement)stmt );
      }
      else if( stmt instanceof BlockInvocationStatement )
      {
        return BlockInvocationStatementTransformer.compile( context, (BlockInvocationStatement)stmt );
      }
      else if( stmt instanceof BeanMethodCallStatement )
      {
        return BeanMethodCallStatementTransformer.compile( context, (BeanMethodCallStatement)stmt );
      }
      else if( stmt instanceof ReturnStatement )
      {
        return ReturnStatementTransformer.compile( context, (ReturnStatement)stmt );
      }
      else if( stmt instanceof BreakStatement )
      {
        return BreakStatementTransformer.compile( context, (BreakStatement)stmt );
      }
      else if( stmt instanceof ContinueStatement )
      {
        return ContinueStatementTransformer.compile( context, (ContinueStatement)stmt );
      }
      else if( stmt instanceof IfStatement)
      {
        return IfStatementTransformer.compile( context, (IfStatement)stmt );
      }
      else if( stmt instanceof WhileStatement )
      {
        return WhileStatementTransformer.compile( context, (WhileStatement)stmt );
      }
      else if( stmt instanceof DoWhileStatement )
      {
        return DoWhileStatementTransformer.compile( context, (DoWhileStatement)stmt );
      }
      else if( stmt instanceof ForEachStatement )
      {
        return ForEachStatementTransformer.compile( context, (ForEachStatement)stmt );
      }
      else if( stmt instanceof SwitchStatement)
      {
        return SwitchStatementTransformer.compile( context, (SwitchStatement)stmt );
      }
      else if( stmt instanceof TryCatchFinallyStatement)
      {
        return TryCatchFinallyStatementTransformer.compile( context, (TryCatchFinallyStatement)stmt );
      }
      else if( stmt instanceof ThrowStatement )
      {
        return ThrowStatementTransformer.compile( context, (ThrowStatement)stmt );
      }
      else if( stmt instanceof UsingStatement )
      {
        return UsingStatementTransformer.compile( context, (UsingStatement)stmt );
      }
      else if( stmt instanceof EvalStatement )
      {
        return EvalStatementTransformer.compile( context, (EvalStatement)stmt );
      }
      else if( stmt instanceof SyntheticFunctionStatement )
      {
        throw new IllegalArgumentException("SyntheticFunctionStatements need to be compiled explicitly, since they require the backing DFS");
      }
      else if( stmt instanceof SyntheticMemberAccessStatement )
      {
        return SyntheticMemberAccessStatementTransformer.compile( context, (SyntheticMemberAccessStatement)stmt );
      }
      else if( stmt instanceof NoOpStatement )
      {
        // noop
        return new IRNoOpStatement();
      }
      else if( stmt instanceof UsesStatement || stmt instanceof UsesStatementList )
      {
        // noop
        return null;
      }
      else
      {
        throw new UnsupportedOperationException(
          "Statement Compiler not yet implemented for: " + stmt.getClass().getName() );
      }
    }
    finally
    {
      context.updateSuperInvokedAfterLastExpressionCompiles();
    }
  }

  // This is in its own method because it requires additional context
  public static IRStatement compileInitializerAssignment( TopLevelTransformationContext context, InitializerAssignment stmt, IRExpression root )
  {
    return InitializerAssignmentTransformer.compile( context, stmt, root );
  }
}