/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.compiler.bytecode;

import gw.internal.ext.org.objectweb.asm.ClassWriter;
import gw.internal.ext.org.objectweb.asm.ClassReader;
import gw.internal.ext.org.objectweb.asm.ClassVisitor;
import gw.internal.ext.org.objectweb.asm.Opcodes;
import gw.internal.ext.org.objectweb.asm.FieldVisitor;
import gw.internal.ext.org.objectweb.asm.MethodVisitor;
import gw.internal.ext.org.objectweb.asm.Label;
import gw.internal.ext.org.objectweb.asm.AnnotationVisitor;
import gw.internal.ext.org.objectweb.asm.util.TraceClassVisitor;
import gw.internal.ext.org.objectweb.asm.util.CheckClassAdapter;
import gw.internal.gosu.compiler.DebugFlag;
import gw.lang.ir.IRClass;
import gw.lang.ir.IRType;
import gw.lang.ir.IRSymbol;
import gw.lang.ir.IRAnnotation;
import gw.lang.ir.statement.IRFieldDecl;
import gw.lang.ir.statement.IRMethodStatement;
import gw.lang.reflect.Modifier;
import gw.lang.reflect.gs.BytecodeOptions;

import java.io.StringWriter;
import java.io.PrintWriter;
import java.util.List;

public class IRClassCompiler extends AbstractBytecodeCompiler
{
  private static boolean COMPILE_WITH_DEBUG_INFO = true;

  //## todo: this s/b configurable
  public static final int JAVA_VER = Opcodes.V1_6;

  private ClassVisitor _cv;
  private IRClass _irClass;


  public static byte[] compileClass( IRClass irClass, boolean debug )
  {
    boolean alreadyDebugging = DebugFlag.isDebugFlagsOn();
    if( !alreadyDebugging && debug )
    {
      DebugFlag.setDebugFlagsOn();
    }
    try
    {
      return new IRClassCompiler( irClass ).compile( );
    }
    finally
    {
      if( !alreadyDebugging )
      {
        DebugFlag.setDebugFlagsOff();
      }
    }
  }

  public IRClassCompiler(IRClass irClass) {
    _irClass = irClass;
  }

  private byte[] compile( )
  {
    ClassWriter writer = new GosuClassWriter();
    StringWriter trace = configClassVisitor( writer );

    try
    {
      compileClassHeader();

      addSourceFileRef();

      compileInnerClasses();

      compileFields();

      compileMethods();

      addAnnotations();

      _cv.visitEnd();
    }
    finally
    {
      if( BytecodeOptions.shouldDebug( _irClass.getName() ))
      {
        System.out.println( "========================================================================" );
        System.out.println( _irClass.getName() );
        System.out.println( "========================================================================" );
        System.out.println( trace );
      }
    }
    byte[] bytes = writer.toByteArray();
//    verify( bytes );
    return bytes;
  }

  public void addAnnotations() {
    for (IRAnnotation annotation : _irClass.getAnnotations() ) {
      AnnotationVisitor annotationVisitor = _cv.visitAnnotation(annotation.getDescriptor().getDescriptor(), annotation.isInclude());
      new IRAnnotationCompiler( annotationVisitor, annotation ).compile();
    }
  }

  private StringWriter configClassVisitor( ClassWriter writer )
  {
    _cv = writer;
    StringWriter trace = null;
    if( isDebugFlagSet( DebugFlag.TRACE ) || BytecodeOptions.shouldDebug( _irClass.getName() ) )
    {
      trace = new StringWriter();
      _cv = new TraceClassVisitor( _cv, new PrintWriter( trace ) );
      if( isDebugFlagSet( DebugFlag.ASM_CHECKER ) )
      {
        _cv = new CheckClassAdapter( _cv );
      }
    }
    return trace;
  }

  public static void verify( byte[] bytes )
  {
    if( isDebugFlagSet( DebugFlag.VERIFY ) )
    {
      StringWriter sw = new StringWriter();
      PrintWriter pw = new PrintWriter( sw );
      CheckClassAdapter.verify( new ClassReader( bytes ), false, pw );
      String out = sw.toString();
      if( out.length() > 0 )
      {
        System.out.println( out );
      }
    }
  }

  static public boolean isDebugFlagSet( DebugFlag flag )
  {
    return DebugFlag.getDebugFlags().contains( flag );
  }

  private void compileClassHeader()
  {
    int modifiers = _irClass.getModifiers();
    _cv.visit( JAVA_VER,
            modifiers,
               _irClass.getThisType().getSlashName(),
               getClassSignature(),
               _irClass.getSuperType().getSlashName(),
               getInterfaceNames() );
  }

  /**
   * Deals with generics.
   *
   * Since Gosu generics are parameterized explicitly in the bytecode world,
   * we'll probably not ever conform to java generics.
   */
  private String getClassSignature()
  {
    return null;
  }

  private String[] getInterfaceNames()
  {
    List<? extends IRType> interfaces = _irClass.getInterfaces();
    if( interfaces == null || interfaces.isEmpty() )
    {
      return null;
    }

    String[] ifaceNames = new String[interfaces.size()];
    for( int i = 0; i < ifaceNames.length; i++ )
    {
      IRType iface = interfaces.get( i );
      ifaceNames[i] = iface.getSlashName();
    }
    return ifaceNames;
  }

  private void addSourceFileRef() {
    _cv.visitSource( _irClass.getSourceFile(), null );
  }

  private void compileInnerClasses()
  {
    for( IRClass.InnerClassInfo innerClass : _irClass.getInnerClasses() )
    {
      visitInnerClass( innerClass );
    }
  }

  private void visitInnerClass( IRClass.InnerClassInfo innerClass )
  {
    _cv.visitInnerClass( innerClass.getInnerClass().getSlashName(),
                         innerClass.getEnclosingType().getSlashName(),
                         innerClass.getInnerClass().getRelativeName(),
                         innerClass.getModifiers());
  }

  private void compileFields() {
    for( IRFieldDecl field : _irClass.getFields() )
    {
      FieldVisitor fv = _cv.visitField( field.getModifiers(),
                                        field.getName(),
                                        field.getType().getDescriptor(),
                                        null,
                                        field.getValue() );
      for (IRAnnotation annotation : field.getAnnotations() ) {
        AnnotationVisitor annotationVisitor = fv.visitAnnotation(annotation.getDescriptor().getDescriptor(), annotation.isInclude());
        new IRAnnotationCompiler( annotationVisitor, annotation ).compile();
      }
      fv.visitEnd();
    }
  }

  private void compileMethods() {
    for ( IRMethodStatement method : _irClass.getMethods() ) {
      compileMethod( method );
    }
  }

  private void compileMethod( IRMethodStatement method )
  {
    MethodVisitor mv = _cv.visitMethod( method.getModifiers(),
                                         method.getName(),
                                         getMethodDescriptor( method ),
                                         null, null );
    for( IRAnnotation annotation : method.getAnnotations() )
    {
      AnnotationVisitor annotationVisitor = mv.visitAnnotation( annotation.getDescriptor().getDescriptor(), annotation.isInclude() );
      new IRAnnotationCompiler( annotationVisitor, annotation ).compile();
    }
    for( IRSymbol param: method.getParameters() )
    {
      List<IRAnnotation> paramAnnotations = param.getAnnotations();
      if( paramAnnotations != null )
      {
        int i = 0;
        for( IRAnnotation annotation: paramAnnotations )
        {
          AnnotationVisitor annotationVisitor = mv.visitParameterAnnotation( i++, annotation.getDescriptor().getDescriptor(), annotation.isInclude() );
          new IRAnnotationCompiler( annotationVisitor, annotation ).compile();
        }
      }
    }

    if( method.getMethodBody() != null )
    {
      mv.visitCode();

      IRBytecodeContext context = new IRBytecodeContext( mv );
      if( !Modifier.isStatic( method.getModifiers() ) )
      {
        context.indexThis( _irClass.getThisType() );
      }
      context.indexSymbols( method.getParameters() );

      IRBytecodeCompiler.compileIRElement( method.getMethodBody(), context );
      terminateFunction( context );
    }
    mv.visitEnd();
  }

  private void terminateFunction( IRBytecodeContext context )
  {
    context.popScope();
    MethodVisitor mv = context.getMv();
    Label endLabel = new Label();
    context.visitLabel( endLabel );
    if( COMPILE_WITH_DEBUG_INFO )
    {
      context.visitLocalVars();
    }
    //mv.visitMaxs( context.getMaxScopeSize(), context.getLocalCount() );
    mv.visitMaxs( 0, 0 );
  }

  public static String getMethodDescriptor(IRMethodStatement m) {
    StringBuilder sb = new StringBuilder();
    sb.append("(");
    for (IRSymbol param : m.getParameters()) {
      sb.append(param.getType().getDescriptor());
    }
    sb.append(")");
    sb.append(m.getReturnType().getDescriptor());
    return sb.toString();
  }

  @Override
  public String toString()
  {
    return "Compiling: " + _irClass.getName(); 
  }
}
