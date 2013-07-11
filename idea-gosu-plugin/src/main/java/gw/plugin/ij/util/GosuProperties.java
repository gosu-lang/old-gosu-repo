/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.plugin.ij.util;

import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiModifier;
import com.intellij.psi.PsiType;
import gw.plugin.ij.lang.psi.api.statements.typedef.IGosuMethod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GosuProperties {
  // Setter
  public static boolean isSetter(@NotNull PsiMethod method) {
    return getSetterName(method) != null;
  }

  @Nullable
  public static String getSetterName(@NotNull PsiMethod method) {
    if (!method.hasModifierProperty(PsiModifier.STATIC)) {
      final String name = method.getName();
      if (method instanceof IGosuMethod) {
        if (((IGosuMethod) method).isForPropertySetter()) {
          return name;
        }
      } else {
        if (method.getParameterList().getParametersCount() == 1) {
          if (name.startsWith("set") && PsiType.VOID.equals(method.getReturnType())) {
            return name.substring(3);
          }
        }
      }
    }
    return null;
  }

  // Getter
  public static boolean isGetter(@NotNull PsiMethod method) {
    return getGetterName(method) != null;
  }

  @Nullable
  public static String getGetterName(@NotNull PsiMethod method) {
    if (!method.hasModifierProperty(PsiModifier.STATIC)) {
      final String name = method.getName();
      if (method instanceof IGosuMethod) {
        if (((IGosuMethod) method).isForPropertyGetter()) {
          return name;
        }
      } else {
        if (method.getParameterList().getParametersCount() == 0) {
          if (name.startsWith("get") && name.length() > 3) {
            return name.substring(3);
          }

          if (name.startsWith("is") && name.length() > 2 && PsiType.BOOLEAN.equals(method.getReturnType())) {
            return name.substring(2);
          }
        }
      }
    }
    return null;
  }
}
