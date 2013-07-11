/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.plugin.ij.usages;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.util.Computable;
import com.intellij.pom.PomTarget;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.targets.AliasingPsiTarget;
import com.intellij.psi.targets.AliasingPsiTargetMapper;
import gw.lang.parser.Keyword;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.module.IModule;
import gw.plugin.ij.util.GosuModuleUtil;
import gw.plugin.ij.util.IDEAUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class GosuAliasingPsiTargetMapper implements AliasingPsiTargetMapper {
  private Set<AliasingPsiTarget> getMethodTargets(@NotNull final PsiMethod method) {
    return ApplicationManager.getApplication().runReadAction(new Computable<Set<AliasingPsiTarget>>() {
      @Override
      public Set<AliasingPsiTarget> compute() {
        final List<String> aliasNames;
        if (method.isConstructor()) {
          aliasNames = ImmutableList.of(Keyword.KW_construct.getName(), Keyword.KW_this.getName(), Keyword.KW_super.getName());
        } else {
          aliasNames = IDEAUtil.getGosuPropertyNames(method);
        }

        final Set<AliasingPsiTarget> aliases = Sets.newHashSet();
        for (String propName : aliasNames) {
          final GosuAliasingPsiTarget alias = new GosuAliasingPsiTarget(method);
          alias.setName(propName);
          aliases.add(alias);
        }
        return aliases;
      }
    });
  }

  private Set<AliasingPsiTarget> getClassTargets(@NotNull final PsiClass klass) {
    final String qualifiedName = ApplicationManager.getApplication().runReadAction(new Computable<String>() {
      @Nullable
      public String compute() {
        return klass.getQualifiedName();
      }
    });

    IModule rootModule = GosuModuleUtil.getGlobalModule(klass.getProject());
    TypeSystem.pushModule( rootModule );
    try {
      final Set<AliasingPsiTarget> aliases = Sets.newHashSet();
      if (TypeSystem.getDefaultType().getType().getName().equals(qualifiedName)) {
        final GosuAliasingPsiTarget alias = new GosuAliasingPsiTarget(klass);
        alias.setName("Type");
        aliases.add(alias);
      }
      return aliases;
    }
    finally {
      TypeSystem.popModule( rootModule );
    }
  }

  @NotNull
  @Override
  public Set<AliasingPsiTarget> getTargets(@NotNull final PomTarget target) {
    if (target instanceof PsiMethod) {
      return getMethodTargets((PsiMethod) target);
    } else if (target instanceof PsiClass) {
      return getClassTargets((PsiClass) target);
    }

    return Collections.emptySet();
  }
}
