/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.compiler.bytecode;

import gw.lang.ir.IRType;
import gw.lang.ir.IRSymbol;
import gw.internal.ext.org.objectweb.asm.Label;

public class IRCompilerLocalVar {
  private String _name;
  private IRType _type;
  private int _index;
  private IRCompilerScope _scope;
  private Label _startLabel;
  private Label _endLabel;
  private boolean _temp;

  public IRCompilerLocalVar(IRSymbol symbol, int index, IRCompilerScope scope) {
    _name = symbol.getName();
    _type = symbol.getType();
    _temp = symbol.isTemp();
    _index = index;
    _scope = scope;
  }

  public String getName() {
    return _name;
  }

  public IRType getType() {
    return _type;
  }

  public int getIndex() {
    return _index;
  }

  public void setIndex(int index) {
    _index = index;
  }

  public IRCompilerScope getScope() {
    return _scope;
  }

  public Label getStartLabel() {
    return _startLabel;
  }

  public Label getEndLabel() {
    return _endLabel;
  }

  public void setStartLabel(Label startLabel) {
    _startLabel = startLabel;
  }

  public void setEndLabel(Label endLabel) {
    _endLabel = endLabel;
  }

  public boolean isTemp() {
    return _temp;
  }

  public int getWidth() {
    return (_type.getName().equals("long") || _type.getName().equals("double") ? 2 : 1);
  }
}
