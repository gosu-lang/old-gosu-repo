/*
 * Copyright 2013. Guidewire Software, Inc.
 */

package gw.lang.reflect.java.asm;

import gw.internal.ext.org.objectweb.asm.AnnotationVisitor;
import gw.internal.ext.org.objectweb.asm.Attribute;
import gw.internal.ext.org.objectweb.asm.Label;
import gw.internal.ext.org.objectweb.asm.MethodVisitor;

/**
 */
public class MethodDeclarationVisitor implements MethodVisitor {
  private AsmMethod _asmMethod;

  public MethodDeclarationVisitor( AsmMethod method ) {
    _asmMethod = method;
  }

  @Override
  public AnnotationVisitor visitAnnotationDefault() {
    return new AsmAnnotationMethodDefaultValueVisitor( _asmMethod );
  }

  @Override
  public AnnotationVisitor visitAnnotation( String desc, boolean bVisibleAtRuntime ) {
    AsmAnnotation asmAnnotation = new AsmAnnotation( desc, bVisibleAtRuntime );
    _asmMethod.addAnnotation( asmAnnotation );
    return new AsmAnnotationVisitor( asmAnnotation );
  }

  @Override
  public AnnotationVisitor visitParameterAnnotation( int parameter, String desc, boolean bVisibleAtRuntime ) {
    AsmAnnotation asmAnnotation = new AsmAnnotation( desc, bVisibleAtRuntime );
    _asmMethod.addParameterAnnotation( parameter, asmAnnotation );
    return new AsmAnnotationVisitor( asmAnnotation );
  }

  @Override
  public void visitAttribute( Attribute attribute ) {
  }

  @Override
  public void visitCode() {
  }

  @Override
  public void visitFrame( int i, int i2, Object[] objects, int i3, Object[] objects2 ) {
  }

  @Override
  public void visitInsn( int i ) {
  }

  @Override
  public void visitIntInsn( int i, int i2 ) {
  }

  @Override
  public void visitVarInsn( int i, int i2 ) {
  }

  @Override
  public void visitTypeInsn( int i, String s ) {
  }

  @Override
  public void visitFieldInsn( int i, String s, String s2, String s3 ) {
  }

  @Override
  public void visitMethodInsn( int i, String s, String s2, String s3 ) {
  }

  @Override
  public void visitJumpInsn( int i, Label label ) {
  }

  @Override
  public void visitLabel( Label label ) {
  }

  @Override
  public void visitLdcInsn( Object o ) {
  }

  @Override
  public void visitIincInsn( int i, int i2 ) {
  }

  @Override
  public void visitTableSwitchInsn( int i, int i2, Label label, Label[] labels ) {
  }

  @Override
  public void visitLookupSwitchInsn( Label label, int[] ints, Label[] labels ) {
  }

  @Override
  public void visitMultiANewArrayInsn( String s, int i ) {
  }

  @Override
  public void visitTryCatchBlock( Label label, Label label2, Label label3, String s ) {
  }

  @Override
  public void visitLocalVariable( String s, String s2, String s3, Label label, Label label2, int i ) {
  }

  @Override
  public void visitLineNumber( int i, Label label ) {
  }

  @Override
  public void visitMaxs( int i, int i2 ) {
  }

  @Override
  public void visitEnd() {
  }
}
