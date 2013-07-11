/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.lang.parser;

import gw.lang.reflect.IType;
import gw.lang.reflect.gs.IGosuClass;

public interface IReducedSymbol {
  boolean isStatic();
  int getModifiers();
  String getName();
  String getDisplayName();
  String getFullDescription();
  boolean isPrivate();
  boolean isInternal();
  boolean isProtected();
  boolean isPublic();
  boolean isAbstract();
  boolean isFinal();
  IType getType();
  IScriptPartId getScriptPart();
  IGosuClass getGosuClass();
  boolean hasTypeVariables();
  Class<?> getSymbolClass();
  GlobalScope getScope();
  boolean isValueBoxed();
  int getIndex();
  IExpression getDefaultValueExpression();
}
