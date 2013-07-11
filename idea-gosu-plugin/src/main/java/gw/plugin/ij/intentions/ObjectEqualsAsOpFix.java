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
import com.intellij.psi.util.PsiMatcherImpl;
import gw.internal.gosu.parser.Expression;
import gw.internal.gosu.parser.expressions.ConditionalAndExpression;
import gw.internal.gosu.parser.expressions.EqualityExpression;
import gw.lang.parser.IExpression;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.expressions.IBeanMethodCallExpression;
import gw.plugin.ij.lang.parser.GosuRawExpression;
import gw.plugin.ij.lang.psi.impl.expressions.GosuBeanMethodCallExpressionImpl;
import gw.plugin.ij.lang.psi.util.GosuPsiParseUtil;
import gw.plugin.ij.util.GosuBundle;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.intellij.psi.util.PsiMatchers.hasClass;

public class ObjectEqualsAsOpFix extends LocalQuickFixAndIntentionActionOnPsiElement {
  public ObjectEqualsAsOpFix(GosuBeanMethodCallExpressionImpl callExpression) {
    super(callExpression);
  }

  @Override
  public void invoke(@NotNull Project project, @NotNull PsiFile file, @Nullable("is null when called from inspection") Editor editor, @NotNull PsiElement startElement, @NotNull PsiElement endElement) {
    if (!CodeInsightUtilBase.prepareFileForWrite(startElement.getContainingFile())) {
      return;
    }
    if (!(startElement instanceof GosuBeanMethodCallExpressionImpl)) {
      return;
    }
    IBeanMethodCallExpression parsedElement = ((GosuBeanMethodCallExpressionImpl) startElement).getParsedElement();
    if (parsedElement == null) {
      return;
    }
    IExpression[] args = parsedElement.getArgs();
    String root = parsedElement.getRootExpression().getLocation().getTextFromTokens();
    if (args != null && args.length == 1) {
      PsiElement toRemove = startElement;
      IParsedElement expr = parsedElement.getParent();
      if (expr instanceof ConditionalAndExpression) {
        ConditionalAndExpression condExpr = (ConditionalAndExpression) expr;
        Expression lhs = condExpr.getLHS();
        if (lhs instanceof EqualityExpression) {
          EqualityExpression eq = (EqualityExpression) lhs;
          if (eq.getLHS().getLocation().getTextFromTokens().equals(root) &&
              eq.getRHS().getLocation().getTextFromTokens().equals("null") &&
              !eq.isEquals()) {
            toRemove = startElement.getParent();
          }
        }
      }
      String src = root + " == " + args[0].getLocation().getTextFromTokens();
      PsiElement stub = GosuPsiParseUtil.parseProgramm(src, startElement, file.getManager(), null);

      PsiElement eq = new PsiMatcherImpl(stub)
              .descendant(hasClass(GosuRawExpression.class))
              .getElement();
      if (eq != null) {
        toRemove.replace(eq);
      }
    }
  }

  @Override
  public boolean isAvailable(@NotNull Project project,
                             @NotNull PsiFile file,
                             @NotNull PsiElement startElement,
                             @NotNull PsiElement endElement) {
    return startElement instanceof GosuBeanMethodCallExpressionImpl;
  }

  @NotNull
  @Override
  public String getText() {
    return GosuBundle.message("inspection.object.equals.as.op");
  }

  @NotNull
  @Override
  public String getFamilyName() {
    return GosuBundle.message("inspection.group.name.expression.issues");
  }
}
