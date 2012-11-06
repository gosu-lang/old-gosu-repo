/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.lang.reflect.gs;

import gw.lang.parser.IExpression;

public interface IGosuFragment extends ICompilableType {

  public static final String FRAGMENT_PACKAGE = "fragment_";

  Object evaluate(IExternalSymbolMap externalSymbols);

  Object evaluateRoot(IExternalSymbolMap externalSymbols);

  IExpression getExpression();

  void setExpression(IExpression expression);

  boolean isExternalSymbol(String name);
}
