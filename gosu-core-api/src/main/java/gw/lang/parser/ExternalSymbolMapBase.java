/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.lang.parser;

import gw.lang.reflect.gs.IExternalSymbolMap;

public abstract class ExternalSymbolMapBase implements IExternalSymbolMap {

  private boolean _assumeSymbolsRequireExternalSymbolMapArgument;

  protected ExternalSymbolMapBase(boolean assumeSymbolsRequireExternalSymbolMapArgument) {
    _assumeSymbolsRequireExternalSymbolMapArgument = assumeSymbolsRequireExternalSymbolMapArgument;
  }

  @Override
  public Object getValue(String name) {
    ISymbol symbol = getSymbol(name);
    verifySymbol( name, symbol );
    // Any external symbols that are properties are coming from the code block, so they implicitly take
    // an IExternalSymbolMap as their first argument
    if (_assumeSymbolsRequireExternalSymbolMapArgument && symbol instanceof IDynamicPropertySymbol) {
      return ((IDynamicPropertySymbol) symbol).getGetterDfs().invoke(new Object[]{this});
    } else {
      return symbol.getValue();
    }
  }

  @Override
  public void setValue(String name, Object value) {
    ISymbol symbol = getSymbol(name);
    verifySymbol( name, symbol );
    // Any external symbols that are properties are coming from the code block, so they implicitly take
    // an IExternalSymbolMap as their first argument
    if (_assumeSymbolsRequireExternalSymbolMapArgument && symbol instanceof IDynamicPropertySymbol) {
      ((IDynamicPropertySymbol) symbol).getSetterDfs().invoke(new Object[]{this, value});
    } else {
      symbol.setValue(value);
    }
  }

  @Override
  public Object invoke(String name, Object[] args) {
    IFunctionSymbol functionSymbol = (IFunctionSymbol) getSymbol(name);

    // Any external symbols that are functions are coming from the code block, so they implicitly take
    // an IExternalSymbolMap as their first argument
    if (shouldAddInExternalSymbolMapArgumentForFunctionSymbol(functionSymbol)) {
      // Swap the external symbols map into the first position
      if (args == null) {
        args = new Object[]{this};
      } else {
        Object[] originalArgs = args;
        args = new Object[originalArgs.length + 1];
        args[0] = this;
        System.arraycopy(originalArgs, 0, args, 1, originalArgs.length);
      }
    }

    return functionSymbol.invoke(args);
  }

  protected boolean shouldAddInExternalSymbolMapArgumentForFunctionSymbol(IFunctionSymbol symbol) {
    return _assumeSymbolsRequireExternalSymbolMapArgument;
  }

  protected void verifySymbol( String name, ISymbol symbol ) {
    if( symbol == null ) {
      throw new IllegalStateException( "External symbol not found: " + name );
    }
  }

  protected abstract ISymbol getSymbol(String name);
}