/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.statements;

import gw.internal.gosu.parser.Statement;
import gw.lang.parser.statements.IClassFileStatement;
import gw.lang.parser.statements.INamespaceStatement;
import gw.lang.parser.statements.ITerminalStatement;
import gw.lang.reflect.module.IModule;

/**
 */
public class NamespaceStatement extends Statement implements INamespaceStatement
{
  private String _strNamespace;

  public NamespaceStatement()
  {
  }

  public String getNamespace()
  {
    return _strNamespace;
  }

  public void setNamespace( String strNamespace )
  {
    _strNamespace = strNamespace == null ? null : strNamespace.intern();
  }

  public Object execute()
  {
    // no-op
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
    return "package " + getNamespace();
  }

  public IModule getModule() {
    IClassFileStatement cfs = (IClassFileStatement) getParent();
    return cfs.getGosuClass().getTypeLoader().getModule();
  }
  
}
