/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.compiler.bytecode;

import gw.internal.ext.org.objectweb.asm.Type;
import gw.internal.gosu.parser.*;
import gw.internal.ext.org.objectweb.asm.AnnotationVisitor;
import gw.lang.ir.IRAnnotation;
import gw.lang.ir.IRType;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.java.IJavaClassMethod;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;
import gw.util.GosuExceptionUtil;
import gw.internal.gosu.ir.transform.AbstractElementTransformer;

import java.lang.reflect.Array;

public class IRAnnotationCompiler
{
  private AnnotationVisitor _visitor;
  private IRAnnotation _annotation;

  public IRAnnotationCompiler( AnnotationVisitor annotationVisitor, IRAnnotation annotation )
  {
    _visitor = annotationVisitor;
    _annotation = annotation;
  }

  public void compile()
  {
    IAnnotationInfo annotationValue = _annotation.getValue();
    compileAnnotationInfo( (GosuAnnotationInfo)annotationValue );
  }

  private void compileAnnotationInfo( GosuAnnotationInfo gai )
  {
    IJavaClassInfo classInfo = ((IJavaType) gai.getType()).getBackingClassInfo();
    IJavaClassMethod[] methods = classInfo.getDeclaredMethods();
    for( IJavaClassMethod method : methods )
    {
      if( method.getParameterTypes().length == 0 )
      {
        String fieldName = method.getName();
        if( !fieldName.equals( "hashCode" ) && !fieldName.equals( "toString" ) && !fieldName.equals( "annotationType" ) )
        {
          IRType returnIRType = AbstractElementTransformer.getDescriptor( method.getReturnType() );
          Object value = gai.getFieldValue( fieldName );
          visitAnnotationField( _visitor, method.getReturnClassInfo(), fieldName, returnIRType, value );
        }
      }
    }
    _visitor.visitEnd();
  }

  private void visitAnnotationField( AnnotationVisitor visitor, IJavaClassInfo returnClassInfo, String fieldName, IRType returnIRType, Object value )
  {
    if( value == null )
    {
      visitor.visit( fieldName, null );
      return;
    }
    try
    {
      if( returnClassInfo.isAnnotation() )
      {
        AnnotationVisitor annotationVisitor = visitor.visitAnnotation( fieldName, returnIRType.getDescriptor() );
        new IRAnnotationCompiler( annotationVisitor, new IRAnnotation( returnIRType, true, (IAnnotationInfo)value ) ).compile();
      }
      else if( JavaTypes.CLASS().getBackingClassInfo().isAssignableFrom( returnClassInfo ) )
      {
        if( value instanceof Class ) {
          visitor.visit( fieldName, Type.getType( AbstractElementTransformer.getDescriptor( (Class)value ).getDescriptor() ) );
        }
        else {
          value = TypeLord.getPureGenericType( (IType)value );
          visitor.visit( fieldName, Type.getType( AbstractElementTransformer.getDescriptor( (IType)value ).getDescriptor() ) );
        }
      }
      else if( returnClassInfo.isArray() )
      {
        visitArray( returnClassInfo, returnIRType, fieldName, value );
      }
      else if( returnClassInfo.isEnum() )
      {
        visitor.visitEnum( fieldName, returnIRType.getDescriptor(), (String)value );
      }
      else
      {
        visitor.visit( fieldName, value );
      }
    }
    catch( Exception e )
    {
      throw GosuExceptionUtil.forceThrow( e );
    }
  }

  private void visitArray( IJavaClassInfo returnClassInfo, IRType returnIRType, String name, Object value )
  {
    AnnotationVisitor arrVisitor = _visitor.visitArray( name );

    if( returnClassInfo.getComponentType().isPrimitive() )
    {
      if( value.getClass().isArray() )
      {
        for( int i = 0; i < Array.getLength( value ); i++ )
        {
          arrVisitor.visit( name, Array.get( value, i ) );
        }
      }
      else
      {
        arrVisitor.visit( name, value );
      }
    }
    else
    {
      if( value.getClass().isArray() )
      {
        Object[] valArr = (Object[])value;
        for( Object o : valArr )
        {
          visitAnnotationField( arrVisitor, returnClassInfo.getComponentType(), name, returnIRType.getComponentType(), o );
        }
      }
      else
      {
        visitAnnotationField( arrVisitor, returnClassInfo.getComponentType(), name, returnIRType.getComponentType(), value );
      }
    }
    arrVisitor.visitEnd();
  }
}
