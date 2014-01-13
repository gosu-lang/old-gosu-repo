/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl.resolvers;

import com.intellij.openapi.extensions.AbstractExtensionPointBean;
import com.intellij.openapi.extensions.ExtensionPointName;
import com.intellij.openapi.util.LazyInstance;
import com.intellij.util.xmlb.annotations.Attribute;
import gw.plugin.ij.lang.psi.api.IFileShadowingResolver;
import org.jetbrains.annotations.NotNull;

public class FileShadowingResolverExtensionBean extends AbstractExtensionPointBean {
  static final ExtensionPointName<FileShadowingResolverExtensionBean> EP_NAME = new ExtensionPointName<>("com.guidewire.gosu.fileShadowingResolver");

  @Attribute("class")
  public String className;

  private final LazyInstance<IFileShadowingResolver> myHandler = new LazyInstance<IFileShadowingResolver>() {
    @NotNull
    protected Class<IFileShadowingResolver> getInstanceClass() throws ClassNotFoundException {
      return findClass(className);
    }
  };

  @NotNull
  public IFileShadowingResolver getHandler() {
    return myHandler.getValue();
  }
}
