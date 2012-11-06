/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

/**
 */
public class InnerClassNotFoundException extends RuntimeException
{
  public InnerClassNotFoundException()
  {
  }

  public InnerClassNotFoundException( Throwable cause )
  {
    super( cause );
  }

  public Throwable fillInStackTrace()
  {
    return this;
  }
}
