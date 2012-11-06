/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.lang.parser;

public interface ITokenizerInstructorState
{
  void restore();
  void restoreTo( ITokenizerInstructor instructor );
}
