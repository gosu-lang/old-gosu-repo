/*
 * Copyright 2013. Guidewire Software, Inc.
 */

package gw.lang.reflect.java.asm;

import gw.internal.ext.org.objectweb.asm.AnnotationVisitor;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class AsmAnnotationVisitor implements AnnotationVisitor {
  private AsmAnnotation _annotation;

  public AsmAnnotationVisitor( AsmAnnotation annotation ) {
    _annotation = annotation;
  }

  @Override
  public void visit( String name, Object value ) {
    _annotation.setValue( name, value );
  }

  @Override
  public void visitEnum( String name, String desc, String value ) {
    _annotation.setValue( name, value );
  }

  @Override
  public AnnotationVisitor visitAnnotation( String name, String desc ) {
    AsmAnnotation asmAnnotation = new AsmAnnotation( desc, true );
    _annotation.setValue( name, asmAnnotation );
    return new AsmAnnotationVisitor( asmAnnotation );
  }

  @Override
  public AnnotationVisitor visitArray( String name ) {
    List<Object> values = new ArrayList<Object>();
    _annotation.setValue( name, values );
    return new AnnotationArrayValuesVisitor( values );
  }

  @Override
  public void visitEnd() {
  }
}
