/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.statements;

import gw.lang.parser.Keyword;
import gw.lang.parser.statements.IClasspathStatement;
import gw.lang.parser.statements.ITerminalStatement;
import gw.internal.gosu.parser.Statement;

import java.util.List;
import java.util.Arrays;

/**
 */
public class ClasspathStatement extends Statement implements IClasspathStatement
{
  private String _strClasspath;

  public ClasspathStatement()
  {
  }

  public String getClasspath()
  {
    return _strClasspath;
  }

  public void setClasspath( String strTypeName )
  {
    _strClasspath = strTypeName;
  }

  public List<String> getPaths()
  {
    return Arrays.asList( _strClasspath.split( "," ) );
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
    return Keyword.KW_class + " " + getClasspath();
  }

}