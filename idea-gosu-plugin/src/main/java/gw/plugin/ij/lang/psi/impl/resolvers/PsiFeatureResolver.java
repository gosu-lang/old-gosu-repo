/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl.resolvers;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.psi.PsiElement;
import gw.lang.parser.IFunctionSymbol;
import gw.lang.parser.IReducedSymbol;
import gw.lang.reflect.*;
import gw.lang.reflect.gs.IGosuClass;
import gw.plugin.ij.lang.psi.api.IFeatureResolver;
import gw.plugin.ij.lang.psi.api.IGosuResolveResult;
import gw.plugin.ij.lang.psi.impl.expressions.GosuReferenceExpressionImpl;
import gw.plugin.ij.util.IDEAUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PsiFeatureResolver {
  private static final Logger LOG = Logger.getInstance(PsiFeatureResolver.class);

  @Nullable
  public static PsiElement resolveMethodOrConstructor(@NotNull IHasParameterInfos info, @NotNull PsiElement context) {
    final IGosuResolveResult result = resolveMethodOrConstructorWithSubstitutor(info, context);
    return result != null ? result.getElement() : null;
  }

  @Nullable
  public static IGosuResolveResult resolveMethodOrConstructorWithSubstitutor(@NotNull IHasParameterInfos info, @NotNull PsiElement context) {
    if (info instanceof IMethodInfoDelegate) {
      return resolveMethodOrConstructorWithSubstitutor(((IMethodInfoDelegate) info).getSource(), context);
    } else {
      for (IFeatureResolver resolver : FeatureResolverExtensionBean.getResolvers()) {
        final IGosuResolveResult element = resolver.resolveMethodOrConstructor(info, context);
        if (element != null) {
          return element;
        }
      }
      return null;
    }
  }

  @Nullable
  public static PsiElement resolveMethodOrConstructor(@NotNull IFunctionSymbol symbol, @NotNull PsiElement context) {
    for (IFeatureResolver resolver : FeatureResolverExtensionBean.getResolvers()) {
      final PsiElement element = resolver.resolveMethodOrConstructor( symbol, context );
      if (element != null) {
        return element;
      }
    }
    return null;
  }

  @Nullable
  public static PsiElement resolveProperty(@NotNull IPropertyInfo pi, PsiElement ctx) {
    PsiElement result;
    while ( true ) {
      result = resolvePropertyWithoutDelegating( pi, ctx );
      if ( result == null && pi instanceof IPropertyInfoDelegate ) {
        IPropertyInfoDelegate propertyInfoDelegate = (IPropertyInfoDelegate) pi;
        pi = propertyInfoDelegate.getSource();
      }
      else {
        return result;
      }
    }
  }

  @Nullable
  private static PsiElement resolvePropertyWithoutDelegating(@NotNull IPropertyInfo pi, PsiElement ctx) {
    if (!isResolvable(pi)) {
      return null;
    }

    for (IFeatureResolver resolver : FeatureResolverExtensionBean.getResolvers()) {
      final PsiElement element = resolver.resolve(pi, ctx);
      if (element != null) {
        return element;
      }
    }
    return null;
  }

  private static boolean isResolvable(@NotNull IPropertyInfo pi) {
    if (pi instanceof ILocationAwareFeature) {
      return true;
    }
    if (!(pi instanceof IFileBasedFeature)) {
      final IType ownersType = pi.getOwnersType();
      if (ownersType == null || ownersType instanceof IErrorType) {
        return false;
      }

      if (!(ownersType instanceof IFileBasedType)) {
//        LOG.warn("Reference resolution is only supported for file-based types: " + ownersType.getClass().getName());
        return false;
      }
    }
    return true;
  }

  @Nullable
  public static PsiElement resolveSymbol(IReducedSymbol symbol, IGosuClass gsClass, GosuReferenceExpressionImpl context) {
    for (IFeatureResolver resolver : FeatureResolverExtensionBean.getResolvers()) {
      final PsiElement element = resolver.resolve(symbol, gsClass, context);
      if (element != null) {
        return element;
      }
    }
    return null;
  }
}
