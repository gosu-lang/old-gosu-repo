/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.lang.parser.template;

import gw.lang.reflect.IType;
import gw.lang.reflect.ReflectUtil;

import java.io.Writer;

public interface ITemplateObserver {

  public boolean beforeTemplateRender(IType type, Writer writer);
  public StringEscaper getEscaper();
  public void afterTemplateRender(IType type, Writer writer);

  //internal access (yes, apparently it must be this hard)
  public static final ITemplateObserverManager MANAGER = ReflectUtil.construct("gw.internal.gosu.template.TemplateObserverAccess");
  interface ITemplateObserverManager {
    public void pushTemplateObserver(ITemplateObserver observer);
    public void popTemplateObserver();
  }
}
