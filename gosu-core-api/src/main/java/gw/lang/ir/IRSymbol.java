/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.lang.ir;

import gw.lang.UnstableAPI;

@UnstableAPI
public class IRSymbol {
  private String _name;
  private IRType _type;
  private boolean _temp;

  public IRSymbol(String name, IRType type, boolean temp) {
    _name = name;
    _type = type;
    _temp = temp;
  }

  public String getName() {
    return _name;
  }

  public IRType getType() {
    return _type;
  }

  public boolean isTemp() {
    return _temp;
  }

  @Override
  public String toString()
  {
    return _name + " : " + _type;
  }
}
