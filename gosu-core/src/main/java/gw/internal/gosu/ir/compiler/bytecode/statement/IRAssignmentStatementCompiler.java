/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.compiler.bytecode.statement;

import gw.internal.gosu.ir.compiler.bytecode.AbstractBytecodeCompiler;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeContext;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeCompiler;
import gw.lang.ir.statement.IRAssignmentStatement;
import gw.lang.ir.IRSymbol;
import gw.internal.ext.org.objectweb.asm.Opcodes;

public class IRAssignmentStatementCompiler extends AbstractBytecodeCompiler {
  public static void compile(IRAssignmentStatement statement, IRBytecodeContext context) {

    IRBytecodeCompiler.compileIRExpression( statement.getValue(), context );
    
    IRSymbol symbol = statement.getSymbol();
    context.getMv().visitVarInsn( getIns( Opcodes.ISTORE, symbol.getType() ),
                                  context.getLocalVar( symbol ).getIndex() );
  }
}
