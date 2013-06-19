package gw.internal.gosu.ir.compiler.bytecode;

import gw.internal.ext.org.objectweb.asm.ClassWriter;
import gw.internal.ext.org.objectweb.asm.MethodVisitor;

public class GosuClassWriter extends ClassWriter
{
  public GosuClassWriter()
  {
    super( ClassWriter.COMPUTE_MAXS );
  }

  @Override
  public MethodVisitor visitMethod( int i, String s, String s2, String s3, String[] strings )
  {
    return new GosuMethodVisitor( super.visitMethod( i, s, s2, s3, strings ) );
  }


}
