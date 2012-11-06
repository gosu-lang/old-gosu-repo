/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.internal.gosu.compiler;

import gw.internal.gosu.parser.IGosuClassInternal;
import gw.lang.reflect.java.IJavaType;
import gw.internal.gosu.ir.TransformingCompiler;
import gw.lang.reflect.gs.IGosuClassLoader;
import gw.lang.reflect.gs.ICompilableType;

public class SingleServingGosuClassLoader extends ClassLoader implements IGosuClassLoader
{
  private GosuClassLoader _parent;

  SingleServingGosuClassLoader( GosuClassLoader parent )
  {
    super( parent.getActualLoader() );
    _parent = parent;
  }

  public Class<?> findClass( String strName ) throws ClassNotFoundException
  {
    return _parent.findClass( strName );
  }

  @Override
  public void dumpAllClasses() {
    // Do nothing here:  it should be taken care of by the main classloader  
  }

  Class _defineClass( ICompilableType gsClass )
  {
    byte[] classBytes = compileClass( gsClass, _parent.shouldDebugClass( gsClass ) );
    CompilationStatistics.instance().collectStats( gsClass, classBytes, true );

    if( classBytes == null )
    {
      throw new IllegalStateException( "Could not generate class for " + gsClass.getName() );
    }
    String strPackage = gsClass.getNamespace();
    if( getPackage( strPackage ) == null )
    {
      definePackage( strPackage, null, null, null, null, null, null, null );
    }

    return defineClass( GosuClassLoader.getJavaName( gsClass ), classBytes, 0, classBytes.length );
  }

  private byte[] compileClass( ICompilableType type, boolean debug )
  {
    return TransformingCompiler.compileClass( type, debug );
  }

  @Override
  public IJavaType getFunctionClassForArity(int length)
  {
    return null;
  }

  @Override
  public ClassLoader getActualLoader() {
    return this;
  }

  @Override
  public Class defineClass(String name, byte[] bytes) {
    return super.defineClass(name, bytes, 0, bytes.length);
  }

  @Override
  public byte[] getBytes( ICompilableType gsClass, boolean compiledToUberModule )
  {
    boolean bPrevValue = ((IGosuClassInternal)gsClass).isCompiledToUberModule();
    ((IGosuClassInternal)gsClass).setCompiledToUberModule( compiledToUberModule );
    try
    {
      return compileClass( gsClass, false );
    }
    finally
    {
      ((IGosuClassInternal)gsClass).setCompiledToUberModule( bPrevValue );
    }
  }

  @Override
  public String getInterfaceMethodsClassName( ICompilableType gsClass ) {
    return null;
  }

  @Override
  public byte[] maybeDefineInterfaceMethodsClass( ICompilableType gosuClass )
  {
    // nothing to do
    return null;
  }

  @Override
  public void assignParent( ClassLoader classLoader )
  {
    throw new UnsupportedOperationException( "Should not happen" );
  }

  public boolean isDisposed()
  {
    return false;
  }
}
