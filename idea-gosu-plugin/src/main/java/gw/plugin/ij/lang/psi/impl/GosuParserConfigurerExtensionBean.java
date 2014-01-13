/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl;

import com.intellij.openapi.extensions.AbstractExtensionPointBean;
import com.intellij.openapi.extensions.ExtensionPointName;
import com.intellij.openapi.util.LazyInstance;
import com.intellij.util.xmlb.annotations.Attribute;
import org.jetbrains.annotations.NotNull;

public class GosuParserConfigurerExtensionBean extends AbstractExtensionPointBean {
  static final ExtensionPointName<GosuParserConfigurerExtensionBean> EP_NAME = new ExtensionPointName<>("com.guidewire.gosu-internal.gosuParserConfigurer");

  @Attribute("class")
  public String className;

  private final LazyInstance<IGosuParserConfigurer> myHandler = new LazyInstance<IGosuParserConfigurer>() {
    @NotNull
    protected Class<IGosuParserConfigurer> getInstanceClass() throws ClassNotFoundException {
      return findClass(className);
    }
  };

  @NotNull
  public IGosuParserConfigurer getHandler() {
    return myHandler.getValue();
  }
}
