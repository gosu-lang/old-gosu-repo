/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.lang.parser;

public interface IParserPart
{
  IGosuParser getOwner();

  void setDontOptimizeStatementLists( boolean dontOptimizeStatementLists );
  boolean isDontOptimizeStatementLists();

  void setValidator( IGosuValidator validator );

  void setLineNumShift( int lineNumShift );

  int getLineNumShift();

  int getOffsetShift();
}
