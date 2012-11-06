/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.lang.parser.coercers;

import gw.lang.reflect.IType;

public class RegExpMatchToBooleanCoercer extends BaseCoercer
{
  private static final RegExpMatchToBooleanCoercer INSTANCE = new RegExpMatchToBooleanCoercer();

  private RegExpMatchToBooleanCoercer()
  {
  }

  @Override
  public Object coerceValue(IType typeToCoerceTo, Object value) {
    return value != null;
  }

  @Override
  public boolean isExplicitCoercion() {
    return false;
  }

  @Override
  public boolean handlesNull() {
    return true;
  }

  @Override
  public int getPriority(IType to, IType from) {
    return 0;
  }

  public static RegExpMatchToBooleanCoercer instance()
  {
    return INSTANCE;
  }
}
