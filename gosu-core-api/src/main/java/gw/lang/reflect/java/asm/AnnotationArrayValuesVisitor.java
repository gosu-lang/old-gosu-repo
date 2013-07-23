/*
 * Copyright 2013. Guidewire Software, Inc.
 */

package gw.lang.reflect.java.asm;

import gw.internal.ext.org.objectweb.asm.AnnotationVisitor;

import java.util.ArrayList;
import java.util.List;

/**
*/
class AnnotationArrayValuesVisitor implements AnnotationVisitor {
  private final List<Object> _values;

  public AnnotationArrayValuesVisitor( List<Object> values ) {
    _values = values;
  }

  @Override
  public void visit( String name, Object value ) {
    _values.add( AsmAnnotation.makeAppropriateValue( value ) );
  }

  @Override
  public void visitEnum( String name, String desc, String value ) {
    _values.add( AsmAnnotation.makeAppropriateValue( value ) );
  }

  @Override
  public AnnotationVisitor visitAnnotation( String name, String desc ) {
    AsmAnnotation asmAnnotation = new AsmAnnotation( desc, true );
    _values.add( asmAnnotation );
    return new AsmAnnotationVisitor( asmAnnotation );
  }

  @Override
  public AnnotationVisitor visitArray( String name ) {
    List<Object> values = new ArrayList<Object>();
    _values.add( values );
    return new AnnotationArrayValuesVisitor( values );
  }

  @Override
  public void visitEnd() {
  }
}
