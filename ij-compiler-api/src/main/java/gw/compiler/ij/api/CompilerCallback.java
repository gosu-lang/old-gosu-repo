/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.compiler.ij.api;

import gw.compiler.ij.api.messages.CompilationItem;

import java.util.List;

public interface CompilerCallback {
  void compile(String moduleName, List<CompilationItem> item) throws Exception;
}
