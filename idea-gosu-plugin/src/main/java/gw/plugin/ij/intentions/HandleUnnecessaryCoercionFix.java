/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.plugin.ij.intentions;

import com.intellij.codeInsight.CodeInsightUtilBase;
import com.intellij.codeInspection.LocalQuickFixAndIntentionActionOnPsiElement;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import gw.plugin.ij.lang.psi.impl.expressions.GosuBeanMethodCallExpressionImpl;
import gw.plugin.ij.lang.psi.impl.expressions.GosuParenthesizedExpressionImpl;
import gw.plugin.ij.lang.psi.impl.expressions.GosuPropertyMemberAccessExpressionImpl;
import gw.plugin.ij.lang.psi.impl.expressions.GosuTypeAsExpressionImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HandleUnnecessaryCoercionFix extends LocalQuickFixAndIntentionActionOnPsiElement {

  public HandleUnnecessaryCoercionFix(@Nullable PsiElement element) {
    super(element);
  }


  @Override
  public void invoke(@NotNull Project project,
                     @NotNull PsiFile file,
                     @Nullable("is null when called from inspection") Editor editor,
                     @NotNull PsiElement startElement,
                     @NotNull PsiElement endElement) {
    if( !CodeInsightUtilBase.prepareFileForWrite(startElement.getContainingFile()) ) {
      return;
    }
    PsiElement pe = startElement.getParent();
    boolean isMemberAcc = pe instanceof GosuBeanMethodCallExpressionImpl || pe instanceof GosuPropertyMemberAccessExpressionImpl;
    if ((pe instanceof GosuTypeAsExpressionImpl || isMemberAcc) && editor != null) {
      PsiElement typeAsExpr = pe.getParent();
      PsiElement replaceMe;
      if (typeAsExpr instanceof GosuTypeAsExpressionImpl) {
        if (typeAsExpr.getParent() instanceof GosuParenthesizedExpressionImpl) {
          typeAsExpr = typeAsExpr.getParent();
        }
        replaceMe = typeAsExpr;
      } else {
        replaceMe = pe;
      }
      replaceMe.replace(isMemberAcc ? pe : startElement);
    }
  }

  @NotNull
  @Override
  public String getText() {
    return "Remove unnecessary cast";
  }

  @NotNull
  @Override
  public String getFamilyName() {
    return getText();
  }
}
