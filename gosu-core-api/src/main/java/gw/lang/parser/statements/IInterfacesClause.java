/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.lang.parser.statements;

import gw.lang.parser.IExpression;
import gw.lang.reflect.IType;

public interface IInterfacesClause extends IExpression
{
  IType[] getInterfaces();
}
