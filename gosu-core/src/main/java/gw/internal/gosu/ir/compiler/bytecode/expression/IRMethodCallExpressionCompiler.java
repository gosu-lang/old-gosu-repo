/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.compiler.bytecode.expression;

import gw.internal.gosu.ir.compiler.bytecode.AbstractBytecodeCompiler;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeContext;
import gw.internal.gosu.ir.compiler.bytecode.IRBytecodeCompiler;
import gw.internal.gosu.ir.nodes.JavaClassIRType;
import gw.lang.ir.IRTypeConstants;
import gw.lang.ir.expression.IRMethodCallExpression;
import gw.lang.ir.IRType;
import gw.lang.ir.IRExpression;
import gw.internal.ext.org.objectweb.asm.Opcodes;

public class IRMethodCallExpressionCompiler extends AbstractBytecodeCompiler {

  public static void compile( IRMethodCallExpression expression, IRBytecodeContext context ) {
    if (expression.getRoot() != null) {
      IRBytecodeCompiler.compileIRExpression( expression.getRoot(), context );
    }
    for (IRExpression arg : expression.getArgs()) {
      IRBytecodeCompiler.compileIRExpression( arg, context );
    }

    int opCode;

    IRType type;

    // If the root is null, use INVOKESTATIC, regardless of the types or the "special" flag
    if (expression.getRoot() == null) {
      type = expression.getOwnersType();
      opCode = Opcodes.INVOKESTATIC;
    } else if (expression.isSpecial()) {
      type = expression.getOwnersType();
      opCode = Opcodes.INVOKESPECIAL;
    } else if (isObjectMethod(expression)) {
      // Methods on Object always need to be invoked with INVOKEVIRTUAL and Object as the root.  Why?
      // Because it's legal to invoke an Object method like toString() on a root expression that's an interface type,
      // but using INVOKEINTERFACE to invoke an Object method not defined directly on the interface will result in an
      // IncompatibleClassChangeException at runtime in the IBM VM, and using INVOKEVIRTUAL with an interface type
      // as the root will result in such an exception at runtime in both the Sun and IBM VMs
      type = IRTypeConstants.OBJECT;
      opCode = Opcodes.INVOKEVIRTUAL;
    } else {
      type = expression.getRoot().getType();
      if (type.isInterface()) {
        opCode = Opcodes.INVOKEINTERFACE;
      } else {
        opCode = Opcodes.INVOKEVIRTUAL;
      }
    }

    StringBuilder descriptor = new StringBuilder();
    descriptor.append("(");
    for (IRType param : expression.getParameterTypes()) {
      descriptor.append(param.getDescriptor());
    }
    descriptor.append(")");
    descriptor.append(expression.getReturnType().getDescriptor());

    context.getMv().visitMethodInsn( opCode,
                                     type.isArray() ? JavaClassIRType.get( Object.class ).getSlashName() : type.getSlashName(),
                                     expression.getName(),
                                     descriptor.toString() );
  }

  /**
   * Determines whether or not the method in question is a method directly on Object, which thus
   * needs to be invoked using INVOKEVIRTUAL regardless of the expression's root type.
   */
  private static boolean isObjectMethod(IRMethodCallExpression expression) {
    return expression.getOwnersType().getName().equals("java.lang.Object");
  }
}
