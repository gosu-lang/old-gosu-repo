/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.plugin.ij.filetypes;

import com.intellij.ide.IconProvider;
import com.intellij.openapi.util.Iconable;
import com.intellij.psi.PsiElement;
import gw.plugin.ij.lang.psi.impl.AbstractGosuClassFileImpl;
import gw.plugin.ij.lang.psi.impl.statements.typedef.GosuTypeDefinitionImpl;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class GosuIconProvider extends IconProvider {
  @Override
  public Icon getIcon(@NotNull PsiElement element, @Iconable.IconFlags int flags) {
    if (element instanceof AbstractGosuClassFileImpl) {
      for (PsiElement child : element.getChildren()) {
        if (child instanceof GosuTypeDefinitionImpl) {
          return child.getIcon(flags);
        }
      }
    }
    return null;
  }
}
