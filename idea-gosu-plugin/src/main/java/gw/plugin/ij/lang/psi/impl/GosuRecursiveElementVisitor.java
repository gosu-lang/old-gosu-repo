/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl;

import gw.plugin.ij.lang.psi.IGosuPsiElement;
import org.jetbrains.annotations.NotNull;

public abstract class GosuRecursiveElementVisitor extends GosuElementVisitor {
  public void visitElement(@NotNull IGosuPsiElement element) {
    element.acceptChildren(this);
  }
}