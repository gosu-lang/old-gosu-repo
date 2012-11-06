/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.compiler.bytecode.expression;

import gw.internal.gosu.ir.compiler.bytecode.AbstractBytecodeCompiler;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeContext;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeCompiler;
import gw.lang.ir.expression.IRCastExpression;
import gw.internal.ext.org.objectweb.asm.MethodVisitor;
import gw.internal.ext.org.objectweb.asm.Opcodes;

public class IRCastExpressionCompiler extends AbstractBytecodeCompiler {

  public static void compile( IRCastExpression expression, IRBytecodeContext context ) {
    MethodVisitor mv = context.getMv();

    IRBytecodeCompiler.compileIRExpression( expression.getRoot(), context );

    String name = expression.getType().isArray() ? expression.getType().getDescriptor() : expression.getType().getSlashName();
    mv.visitTypeInsn( Opcodes.CHECKCAST, name );
  }
}
