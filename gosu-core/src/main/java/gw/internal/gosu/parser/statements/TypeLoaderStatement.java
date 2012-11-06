/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.statements;

import gw.internal.gosu.parser.Statement;
import gw.lang.parser.Keyword;
import gw.lang.parser.statements.ITerminalStatement;
import gw.lang.parser.statements.ITypeLoaderStatement;
import gw.lang.reflect.IType;

/**
 */
public class TypeLoaderStatement extends Statement implements ITypeLoaderStatement
{
  private IType _typeLoader;

  public IType getTypeLoader()
  {
    return _typeLoader;
  }

  public void setTypeLoader( IType typeLoader )
  {
    _typeLoader = typeLoader;
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
    return Keyword.KW_typeloader + " " + getTypeLoader().getName();
  }

}