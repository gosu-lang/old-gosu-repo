/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.plugin.ij.completion.handlers;

import com.google.common.collect.ImmutableList;
import com.intellij.codeInsight.completion.AllClassesGetter;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.completion.PrefixMatcher;
import com.intellij.patterns.ElementPattern;
import com.intellij.patterns.PsiJavaElementPattern;
import com.intellij.patterns.PsiJavaPatterns;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiCodeBlock;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiIdentifier;
import com.intellij.psi.PsiKeyword;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiParameterList;
import com.intellij.psi.PsiPrimitiveType;
import com.intellij.psi.PsiReferenceList;
import com.intellij.psi.PsiType;
import com.intellij.psi.PsiTypeParameter;
import com.intellij.psi.filters.ClassFilter;
import com.intellij.psi.filters.ElementFilter;
import com.intellij.psi.filters.TrueFilter;
import com.intellij.psi.filters.classes.AnyInnerFilter;
import com.intellij.psi.filters.element.ExcludeDeclaredFilter;
import com.intellij.psi.filters.types.AssignableFromFilter;
import com.intellij.util.Consumer;
import gw.lang.reflect.ICanBeAnnotation;
import gw.lang.reflect.IDefaultTypeLoader;
import gw.lang.reflect.IFileBasedType;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeLoader;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.GosuClassTypeLoader;
import gw.plugin.ij.completion.proposals.PrimitiveCompletionProposal;
import gw.plugin.ij.completion.proposals.PsiClassCompletionProposal;
import gw.plugin.ij.completion.proposals.RawCompletionProposal;
import gw.plugin.ij.lang.psi.impl.CustomPsiClassCache;
import gw.plugin.ij.lang.psi.impl.GosuScratchpadFileImpl;
import gw.plugin.ij.lang.psi.impl.expressions.GosuIdentifierImpl;
import gw.plugin.ij.lang.psi.impl.expressions.GosuTypeLiteralImpl;
import gw.plugin.ij.lang.psi.impl.statements.GosuUsesStatementImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.intellij.patterns.PlatformPatterns.psiElement;

public class TypeCompletionHandler extends AbstractCompletionHandler {
  private static final List<PsiPrimitiveType> PRIMITIVE_TYPES = ImmutableList.of(
      PsiType.BYTE,
      PsiType.CHAR,
      PsiType.DOUBLE, PsiType.FLOAT,
      PsiType.INT, PsiType.LONG, PsiType.SHORT,
      PsiType.BOOLEAN,
      PsiType.VOID);

  public static final PsiJavaElementPattern.Capture<PsiElement> IN_TYPE_PARAMETER = PsiJavaPatterns.psiElement()
      .afterLeaf(PsiKeyword.EXTENDS, PsiKeyword.SUPER, "&")
      .withParent(PsiJavaPatterns.psiElement(PsiReferenceList.class).withParent(PsiTypeParameter.class));

  public static final PsiJavaElementPattern.Capture<PsiElement> INSIDE_METHOD_THROWS_CLAUSE = PsiJavaPatterns.psiElement()
      .afterLeaf(PsiKeyword.THROWS, ",")
      .inside(PsiMethod.class)
      .andNot(PsiJavaPatterns.psiElement().inside(PsiCodeBlock.class))
      .andNot(PsiJavaPatterns.psiElement().inside(PsiParameterList.class));

  public static final ElementPattern<PsiElement> AFTER_THROW_NEW = psiElement()
      .afterLeaf(psiElement().withText(PsiKeyword.NEW).afterLeaf(psiElement().withText(PsiKeyword.THROW)));

  private final ElementFilter _filter;
  private int _weight;
  @NotNull
  private final PrefixMatcher _prefixMatcher;
  private final boolean _javaCtx;

  public TypeCompletionHandler(@NotNull CompletionParameters parameters, @NotNull CompletionResultSet result, int weight) {
    this(parameters, result, getDefaultFilter(parameters.getPosition()), weight);
  }

  public TypeCompletionHandler(@NotNull CompletionParameters parameters, @NotNull CompletionResultSet result, ElementFilter filter, int weight) {
    super(parameters, result);

    _filter = filter;
    _weight = weight;
    _prefixMatcher = result.getPrefixMatcher();
    _javaCtx = parameters.getPosition() instanceof PsiIdentifier;
  }

  @NotNull
  private static ElementFilter getDefaultFilter(PsiElement insertedElement) {
    return AFTER_THROW_NEW.accepts(insertedElement) ? new AssignableFromFilter("java.lang.Throwable") :
        IN_TYPE_PARAMETER.accepts(insertedElement) ? new ExcludeDeclaredFilter(new ClassFilter(PsiTypeParameter.class)) :
            INSIDE_METHOD_THROWS_CLAUSE.accepts(insertedElement) ? new AnyInnerFilter(new AssignableFromFilter("java.lang.Throwable")) :
                TrueFilter.INSTANCE;
  }

  @Override
  public void handleCompletePath() {
    final PsiElement position = getContext().getPosition();
    if (position instanceof GosuIdentifierImpl) {
      if (position.getParent() instanceof GosuTypeLiteralImpl) {
        if (position.getParent().getParent() instanceof GosuUsesStatementImpl) {
          return;
        }
      }
    }

    // primitives are always available
    addPrimitives();

    addScratchpadInnerClasses(position);

    final boolean atAnnotation = atAnnotation(_params.getPosition());

//    long t1 = System.nanoTime();
    // use IJ to dal with Java and Gosu classes
    AllClassesGetter.processJavaClasses(_params, _prefixMatcher, _params.getInvocationCount() <= 1, new Consumer<PsiClass>() {
      @Override
      public void consume(@NotNull PsiClass psiClass) {
        if (atAnnotation && !psiClass.isAnnotationType()) {
          return;
        }

//        if (_filter == null || _filter.isAcceptable(psiClass, _params.getPosition())) {
        if (_prefixMatcher.prefixMatches(psiClass.getName())) {
          addCompletion(new PsiClassCompletionProposal(psiClass, _weight), _javaCtx);
        }
      }
    });
//    System.out.println("Java & Gosu types - " + (System.nanoTime() - t1) *1e-6);

    // process all typeloaders other than Java and Gosu and collect appropriate types
    for (ITypeLoader loader : TypeSystem.getAllTypeLoaders()) {
      if (!(loader instanceof GosuClassTypeLoader || loader instanceof IDefaultTypeLoader)) {
        for (CharSequence typeName : loader.getAllTypeNames()) {
          String s = typeName.toString();
          int lastDot = s.lastIndexOf('.');
          String simpleName = lastDot >= 0 ? s.substring(lastDot + 1) : s;
          if (_prefixMatcher.prefixMatches(simpleName)) {
            IType type = TypeSystem.getByFullNameIfValid(s, loader.getModule());
            if (type instanceof IFileBasedType) {
              if (atAnnotation && (
                  !(type instanceof ICanBeAnnotation) ||
                  !((ICanBeAnnotation) type).isAnnotation()) ) {
                return;
              }
              PsiClass psiClass = CustomPsiClassCache.instance().getPsiClass(type);
              if (psiClass != null) {
                addCompletion(new PsiClassCompletionProposal(psiClass, _weight), _javaCtx);
              }
            }
          }
        }
      }
    }
  }

  private void addScratchpadInnerClasses(@Nullable PsiElement position) {
    if (position == null) {
      return;
    }

    PsiFile psiFile = position.getContainingFile();
    if (!(psiFile instanceof GosuScratchpadFileImpl)) {
      return;
    }

    GosuScratchpadFileImpl scratchpadFile = (GosuScratchpadFileImpl) psiFile;
    for (PsiClass cls : scratchpadFile.getPsiClass().getInnerClasses()) {
      if (cls.getName().startsWith(_prefixMatcher.getPrefix())) {
        addCompletion(new PsiClassCompletionProposal(cls, _weight), _javaCtx);
      }
    }
  }

  private void addPrimitives() {
    for (PsiType type : PRIMITIVE_TYPES) {
      if (type.getCanonicalText().startsWith(_prefixMatcher.getPrefix())) {
        addCompletion(new PrimitiveCompletionProposal(type), _javaCtx);
      }
    }

    if ("block".startsWith(_prefixMatcher.getPrefix())) {
      addCompletion(new RawCompletionProposal("block"));
    }
  }
}