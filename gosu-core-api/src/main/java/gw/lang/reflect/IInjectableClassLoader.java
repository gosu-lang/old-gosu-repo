/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.lang.reflect;

public interface IInjectableClassLoader {

  Class defineClass(String className, byte[] bytes);
  void dispose();
}
