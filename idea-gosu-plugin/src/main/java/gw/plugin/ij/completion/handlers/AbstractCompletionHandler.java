/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.plugin.ij.completion.handlers;

import com.intellij.codeInsight.completion.AllClassesGetter;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.codeInsight.lookup.PsiTypeLookupItem;
import com.intellij.codeInsight.lookup.VariableLookupItem;
import com.intellij.openapi.util.Key;
import com.intellij.patterns.PsiJavaPatterns;
import com.intellij.psi.*;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.module.IModule;
import gw.plugin.ij.completion.*;
import gw.plugin.ij.completion.handlers.filter.CompletionFilter;
import gw.plugin.ij.completion.handlers.filter.CompletionFilterExtensionPointBean;
import gw.plugin.ij.completion.proposals.*;
import gw.plugin.ij.lang.psi.impl.AbstractGosuClassFileImpl;
import gw.plugin.ij.util.GosuModuleUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class AbstractCompletionHandler implements IPathCompletionHandler {
  public static final Key<Integer> COMPLETION_WEIGHT = new Key<>("_gosuCompletionWeight");

  protected final CompletionParameters _params;
  private final CompletionResultSet _resultSet;
  private int _completionCount;

  public AbstractCompletionHandler(CompletionParameters params, CompletionResultSet resultSet) {
    _params = params;
    _resultSet = resultSet;
  }

  public CompletionParameters getContext() {
    return _params;
  }

  public CompletionResultSet getResult() {
    return _resultSet;
  }

  @Nullable
  public String getStatusMessage() {
    return null;
  }

  public int getTotalCompletions() {
    return _completionCount;
  }

  @Nullable
  private static LookupElement createLookup(@NotNull CompletionParameters parameters, @NotNull GosuCompletionProposal gosuCompletionProposal) {
    return createLookup(parameters, gosuCompletionProposal, true);
  }

  @Nullable
  private static LookupElement createLookup(@NotNull CompletionParameters parameters, @NotNull GosuCompletionProposal proposal, boolean inJavaContext) {
    if (proposal instanceof InitializerCompletionProposal) {
      return new InitializerCompletionProposalLookupElement((InitializerCompletionProposal) proposal);
    }

    if (proposal instanceof ICompletionHasAdditionalSyntax) {
      return new GosuAdditionalSyntaxLookupElement((ICompletionHasAdditionalSyntax) proposal);
    }

    if (proposal instanceof PathCompletionProposal) {
      return new GosuFeatureInfoLookupItem(((PathCompletionProposal) proposal).getBeanTree(), parameters.getPosition());
    }

    if (proposal instanceof PrimitiveCompletionProposal) {
      return PsiTypeLookupItem.createLookupItem(((PrimitiveCompletionProposal) proposal).getPrimitiveType(), null);
    }

    LookupElement completion = null;
    final PsiElement element = proposal.resolve(parameters.getPosition());
    if (element != null) {
      if (element instanceof PsiClass) {
        completion = AllClassesGetter.createLookupItem((PsiClass) element, inJavaContext ? GosuClassNameInsertHandler.GOSU_CLASS_INSERT_HANDLER : AllClassesGetter.TRY_SHORTENING);
      } else if (element instanceof PsiVariable) {
        completion = new VariableLookupItem((PsiVariable) element);
      } else if (element instanceof PsiMethod) {
        completion = null; // let the feature info fill this completion in
      } else if (element instanceof PsiNamedElement) {
        completion = LookupElementBuilder.create((PsiNamedElement) element);
      } else {
        completion = LookupElementBuilder.create(element.getText());
      }
    }

    if (completion == null) {
      final IFeatureInfo featureInfo = proposal.getFeatureInfo();
      if (featureInfo != null) {
        completion = new GosuFeatureCallLookupElement(featureInfo);
      }
    }

    if (proposal instanceof SymbolCompletionProposal) {
      final IModule module = GosuModuleUtil.findModuleForPsiElement(parameters.getPosition());
      completion = new RawSymbolLookupItem(((SymbolCompletionProposal) proposal).getSymbol(), module);
    }

    if (completion == null) {
      completion = LookupElementBuilder.create(proposal.getGenericName());
    }

    completion.putUserData(COMPLETION_WEIGHT, proposal.getWeight());
    return completion;
  }

  public void addCompletion(@NotNull GosuCompletionProposal gosuCompletionProposal, boolean javaCtx) {
    addCompletion(_resultSet, gosuCompletionProposal, javaCtx);
  }

  public void addCompletion(@NotNull GosuCompletionProposal gosuCompletionProposal) {
    addCompletion(_resultSet, gosuCompletionProposal);
  }

  public void addCompletion(@NotNull CompletionResultSet customResult, @NotNull GosuCompletionProposal gosuCompletionProposal) {
    addCompletion(customResult,  gosuCompletionProposal, false);
  }

  // Stupid telescoping methods
  public void addCompletion(@NotNull CompletionResultSet customResult, @NotNull GosuCompletionProposal gosuCompletionProposal, boolean javaCtx) {
    if (!canBeShown(gosuCompletionProposal)) {
      return;
    }
    _completionCount++;
    final LookupElement completion = createLookup(_params, gosuCompletionProposal, javaCtx);
    if (completion != null) {
      customResult.addElement(completion);
    }
  }

  public boolean canBeShown(GosuCompletionProposal proposal) {
    List<CompletionFilter> filters = CompletionFilterExtensionPointBean.getFilters();
    for (CompletionFilter filter : filters) {
      if (!filter.allows(proposal)) {
        return false;
      }
    }
    return true;
  }

  public IType getCtxType() {
    return ((AbstractGosuClassFileImpl)getContext().getPosition().getContainingFile()).getParseData().getContextType();
  }

  public boolean atAnnotation(PsiElement position) {
    return PsiJavaPatterns.psiElement().afterLeaf("@").accepts(position);
  }

  public static boolean isAnnotation(IType type) {
    return JavaTypes.ANNOTATION().isAssignableFrom(type) ||
      JavaTypes.IANNOTATION().isAssignableFrom(type);
  }
}
