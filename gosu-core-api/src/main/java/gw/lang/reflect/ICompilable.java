/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.lang.reflect;

/**
 */
public interface ICompilable
{
  boolean isCompilable();
  byte[] compile();
}
