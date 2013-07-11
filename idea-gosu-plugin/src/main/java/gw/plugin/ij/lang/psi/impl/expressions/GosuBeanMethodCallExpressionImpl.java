/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl.expressions;

import com.intellij.codeInsight.daemon.impl.analysis.HighlightVisitorImpl;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.JavaElementVisitor;
import com.intellij.psi.JavaResolveResult;
import com.intellij.psi.PsiCallExpression;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiExpressionList;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiReferenceParameterList;
import com.intellij.psi.PsiType;
import com.intellij.psi.impl.source.codeStyle.CodeEditUtil;
import com.intellij.psi.impl.source.tree.TreeElement;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiMatcherImpl;
import com.intellij.psi.util.PsiMatchers;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import gw.lang.parser.expressions.IBeanMethodCallExpression;
import gw.lang.parser.expressions.IMemberAccessExpression;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IType;
import gw.plugin.ij.lang.GosuTokenTypes;
import gw.plugin.ij.lang.parser.GosuCompositeElement;
import gw.plugin.ij.lang.parser.GosuElementTypes;
import gw.plugin.ij.lang.psi.api.types.IGosuCodeReferenceElement;
import gw.plugin.ij.lang.psi.api.types.IGosuTypeElement;
import gw.plugin.ij.lang.psi.api.types.IGosuTypeParameterList;
import gw.plugin.ij.lang.psi.impl.GosuElementVisitor;
import gw.plugin.ij.lang.psi.impl.resolvers.PsiFeatureResolver;
import gw.plugin.ij.lang.psi.impl.statements.typedef.GosuSyntheticClassDefinitionImpl;
import gw.plugin.ij.lang.psi.util.ElementTypeMatcher;
import gw.plugin.ij.lang.psi.util.GosuPsiParseUtil;
import gw.plugin.ij.util.IDEAUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.Callable;


public class GosuBeanMethodCallExpressionImpl extends GosuReferenceExpressionImpl<IBeanMethodCallExpression> implements IGosuCodeReferenceElement, IGosuTypeElement, PsiCallExpression {
  public GosuBeanMethodCallExpressionImpl(GosuCompositeElement node) {
    super(node);
  }

  @Nullable
  public PsiElement getReferenceNameElement() {
    PsiElement child = getLastChild();
    while (child != null) {
      final ASTNode node = child.getNode();
      if (node != null && (node.getElementType() == GosuTokenTypes.TT_IDENTIFIER || GosuTokenTypes.isKeyword(node.getElementType()))) return child;
      child = child.getPrevSibling();
    }
    return null;
  }

  @Override
  public IGosuCodeReferenceElement getQualifier() {
    final PsiElement firstChild = getFirstChild();
    return firstChild instanceof IGosuCodeReferenceElement ? (IGosuCodeReferenceElement) firstChild : null;
  }

  @Override
  public void setQualifier(IGosuCodeReferenceElement newQualifier) {
    throw new UnsupportedOperationException("Men at work");
  }

  @Nullable
  public IGosuTypeParameterList getTypeParameterList() {
    return null;
  }

  @Override
  public PsiType[] getTypeArguments() {
    return PsiType.EMPTY_ARRAY;
  }

  @Override
  public PsiElement resolve() {
    return IDEAUtil.runInModule(new Callable<PsiElement>() {
      @Nullable
      public PsiElement call() throws Exception {
        final IBeanMethodCallExpression pe = getParsedElement();
        if (pe != null) {
          final IMethodInfo mi = pe.getGenericMethodDescriptor();
          if (mi != null) {
            return PsiFeatureResolver.resolveMethodOrConstructor(mi, GosuBeanMethodCallExpressionImpl.this);
          }
        }
        return null;
      }
    }, this);
  }

  //TODO-dp consider adding an extension point for these kinds of special cases

  public TextRange getRangeInElement() {
    if (isDisplayKey()) {
      int startOffset = getTextRange().getStartOffset();
      PsiElement referenceNameElement = getReferenceNameElement();
      if (referenceNameElement != null) {
        int endOffset = referenceNameElement.getTextRange().getEndOffset();
        return new TextRange(0, endOffset - startOffset);
      }
    }
    return super.getRangeInElement();
  }

  private boolean isDisplayKey() {
    return GosuPropertyMemberAccessExpressionImpl.isDisplayKey(getParsedElement());
  }

  protected PsiElement handleElementRenameInner(String newElementName) throws IncorrectOperationException {
    if (isDisplayKey()) {
      GosuIdentifierImpl childOfType = PsiTreeUtil.findChildOfType(this, GosuIdentifierImpl.class);
      GosuPropertyMemberAccessExpressionImpl parent = (GosuPropertyMemberAccessExpressionImpl) childOfType.getParent().getParent();
      PsiElement referenceNameElement = parent.getReferenceNameElement();
      int textOffset = getTextOffset();
      int startOffset = referenceNameElement.getTextRange().getStartOffset() - textOffset;
      int endOffset = getReferenceNameElement().getTextRange().getEndOffset() - textOffset;
      String oldText = getText();
      String newText = oldText.substring(0, startOffset) + newElementName + oldText.substring(endOffset);

      PsiElement element = GosuPsiParseUtil.createReferenceNameFromText(this, newText);
      ASTNode newNode = new PsiMatcherImpl(element.getContainingFile())
          .descendant(PsiMatchers.hasClass(GosuSyntheticClassDefinitionImpl.class))
          .descendant(new ElementTypeMatcher(GosuElementTypes.ELEM_TYPE_ReturnStatement))
          .descendant(PsiMatchers.hasClass(GosuBeanMethodCallExpressionImpl.class))
          .getElement().getNode();

      CodeEditUtil.setOldIndentation((TreeElement) newNode, 0); // this is to avoid a stupid exception
      GosuCompositeElement oldNode = this.getNode();
      oldNode.getTreeParent().replaceChild(oldNode, newNode);
      return this;
    } else {
      return super.handleElementRenameInner(newElementName);
    }
  }

  @Override
  public void accept( @NotNull PsiElementVisitor visitor ) {
    if( visitor instanceof JavaElementVisitor && !(visitor instanceof HighlightVisitorImpl) ) {
      ((JavaElementVisitor)visitor).visitCallExpression( this );
    } else if( visitor instanceof GosuElementVisitor) {
      ((GosuElementVisitor)visitor).visitBeanMethodCallExpression(this);
    }
    else {
      visitor.visitElement( this );
    }

  }

  @NotNull
  @Override
  public PsiExpressionList getArgumentList() {
    return (GosuExpressionListImpl)findChildByType( GosuElementTypes.ELEM_TYPE_ArgumentListClause );
  }

  @Override
  public PsiMethod resolveMethod() {
    PsiElement ref = resolve();
    return ref instanceof PsiMethod ? (PsiMethod)ref : null;
  }

  @NotNull
  @Override
  public JavaResolveResult resolveMethodGenerics() {
    throw new UnsupportedOperationException( "Not implemented yet" );
  }

  @NotNull
  @Override
  public PsiReferenceParameterList getTypeArgumentList() {
    return null;
  }
}
