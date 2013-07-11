/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl.resolvers;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.impl.PsiManagerImpl;
import gw.fs.IFile;
import gw.internal.gosu.properties.IPropertiesPropertyInfo;
import gw.lang.parser.IReducedSymbol;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.IPropertiesType;
import gw.lang.reflect.module.IModule;
import gw.plugin.ij.filesystem.IDEAFile;
import gw.plugin.ij.lang.psi.api.AbstractFeatureResolver;
import gw.plugin.ij.lang.psi.api.IFeatureResolver;
import gw.plugin.ij.lang.psi.impl.expressions.GosuReferenceExpressionImpl;
import gw.plugin.ij.util.GosuModuleUtil;
import gw.plugin.ij.util.IDEAUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PropertiesFeatureResolver extends AbstractFeatureResolver {
  @Nullable
  @Override
  public PsiElement resolve(@NotNull IPropertyInfo propertyInfo, @NotNull PsiElement context) {
    if (propertyInfo instanceof IPropertiesPropertyInfo) {
      final Module ijModule = GosuModuleUtil.getModule(GosuModuleUtil.findModuleForPsiElement(context));
      final Project project = ijModule.getProject();

      final IPropertiesType type = (IPropertiesType) propertyInfo.getOwnersType();
      final VirtualFile file = IDEAUtil.getTypeResourceFiles(type).get(0);
      final PsiFile psiFile = PsiManagerImpl.getInstance(project).findFile(file);
      final String propertiesFileKey = type.getPropertiesFileKey(propertyInfo);
      for (PsiElement property : psiFile.getChildren()[0].getChildren()) {
        if (((PsiNamedElement) property).getName().equals(propertiesFileKey)) {
          return property;
        }
      }
    }
    return null;
  }
}
