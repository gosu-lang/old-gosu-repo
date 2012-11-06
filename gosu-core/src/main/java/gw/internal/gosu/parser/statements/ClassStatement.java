/*
 * Copyright 2012. Guidewire Software, Inc.
 */
package gw.internal.gosu.parser.statements;

import gw.internal.gosu.parser.Statement;
import gw.internal.gosu.parser.FileSystemGosuClassRepository;
import gw.internal.gosu.parser.IGosuClassInternal;
import gw.internal.gosu.parser.expressions.ClassDeclaration;
import gw.lang.reflect.gs.GosuClassTypeLoader;
import gw.lang.reflect.gs.IGosuEnhancement;
import gw.lang.reflect.gs.ISourceFileHandle;
import gw.lang.reflect.module.IModule;
import gw.lang.parser.statements.IClassStatement;
import gw.lang.parser.statements.ITerminalStatement;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.TypeSystem;


/**
 */
public final class ClassStatement extends Statement implements IClassStatement
{
  private IGosuClassInternal _gsClass;
  private ClassFileStatement _cfs;
  private ClassDeclaration _classDeclaration;

  public ClassStatement( IGosuClassInternal gsClass )
  {
    _gsClass = gsClass;
    if( _gsClass != null && _gsClass.getEnclosingType() == null )
    {
      _cfs = new ClassFileStatement();
    }
  }

  public ClassFileStatement getClassFileStatement()
  {
    //## todo: get outer-most class file stmt?
    return _cfs;
  }

  public Object execute()
  {
    // No-Op
    return Statement.VOID_RETURN_VALUE;
  }

  @Override
  public ITerminalStatement getLeastSignificantTerminalStatement()
  {
    return null;
  }

  @Override
  public boolean isNoOp()
  {
    return true;
  }

  @Override
  public String toString()
  {
    return (_gsClass.isInterface() ? "interface " : "class " ) + _gsClass.getName() + "{ ... }";
  }

  public IGosuClassInternal getGosuClass()
  {
    return _gsClass;
  }

  @Override
  public void clearParseTreeInformation()
  {
    TypeSystem.lock();
    try
    {
      super.clearParseTreeInformation();
      if( _cfs != null )
      {
        _cfs.setLocation( null );
      }
    }
    finally
    {
      TypeSystem.unlock();
    }
  }

  public IModule getModule()
  {
    return _gsClass.getTypeLoader().getModule();
  }

  @Override
  public String getFileName() {
    IGosuClassInternal gosuClass = getEnclosingClass();
    ISourceFileHandle sourceFileHandle = gosuClass.getSourceFileHandle();
    if( sourceFileHandle instanceof FileSystemGosuClassRepository.FileSystemSourceFileHandle )
    {
      return ((FileSystemGosuClassRepository.FileSystemSourceFileHandle)sourceFileHandle).getFileInfo().getFileName();
    }
    else
    {
      if (gosuClass instanceof IGosuEnhancement) {
        return gosuClass.getRelativeName() + GosuClassTypeLoader.GOSU_ENHANCEMENT_FILE_EXT;
      } else {
        return gosuClass.getRelativeName() + ".gs";
      }
    }
  }

  private IGosuClassInternal getEnclosingClass()
  {
    IGosuClassInternal clazz = _gsClass;
    while( clazz.getEnclosingType() != null )
    {
      clazz = (IGosuClassInternal)clazz.getEnclosingType();
    }
    return clazz;
  }

  private IFeatureInfo getFeatureInfoIfAnyThatEnclosesItselfAndItsChildren()
  {
    return getGosuClass().getTypeInfo();
  }

  public ClassDeclaration getClassDeclaration() {
    return _classDeclaration;
  }

  public void setClassDeclaration(ClassDeclaration classDeclaration) {
    _classDeclaration = classDeclaration;
  }

}
