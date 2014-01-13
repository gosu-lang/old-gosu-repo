/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl.resolvers;

import com.intellij.openapi.extensions.AbstractExtensionPointBean;
import com.intellij.openapi.extensions.ExtensionPointName;
import com.intellij.openapi.util.LazyInstance;
import com.intellij.util.xmlb.annotations.Attribute;
import gw.plugin.ij.lang.psi.api.ITypeResolver;
import org.jetbrains.annotations.NotNull;

public class TypeResolverExtensionBean extends AbstractExtensionPointBean {
  static final ExtensionPointName<TypeResolverExtensionBean> EP_NAME = new ExtensionPointName<>("com.guidewire.gosu-internal.typeResolver");

  @Attribute("class")
  public String className;

  private final LazyInstance<ITypeResolver> myHandler = new LazyInstance<ITypeResolver>() {
    @NotNull
    protected Class<ITypeResolver> getInstanceClass() throws ClassNotFoundException {
      return findClass(className);
    }
  };

  @NotNull
  public ITypeResolver getHandler() {
    return myHandler.getValue();
  }
}
