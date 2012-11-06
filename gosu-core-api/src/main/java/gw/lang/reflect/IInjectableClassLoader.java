/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.lang.reflect;

public interface IInjectableClassLoader {

  Class defineClass(String className, byte[] bytes);
  void dispose();
}
