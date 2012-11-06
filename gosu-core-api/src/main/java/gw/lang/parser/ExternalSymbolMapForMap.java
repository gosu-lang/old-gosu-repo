/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.lang.parser;

import gw.util.CaseInsensitiveHashMap;

public class ExternalSymbolMapForMap extends ExternalSymbolMapBase {

  private CaseInsensitiveHashMap<CaseInsensitiveCharSequence, ISymbol> _externalSymbols;

  public ExternalSymbolMapForMap(CaseInsensitiveHashMap<CaseInsensitiveCharSequence, ISymbol> externalSymbols) {
    this(externalSymbols, false);
  }

  public ExternalSymbolMapForMap(CaseInsensitiveHashMap<CaseInsensitiveCharSequence, ISymbol> externalSymbols, boolean assumeSymbolsRequireExternalSymbolMapArgument) {
    super(assumeSymbolsRequireExternalSymbolMapArgument);
    _externalSymbols = externalSymbols;
  }

  public ISymbol getSymbol(String name) {
    return _externalSymbols.get(CaseInsensitiveCharSequence.get(name));
  }

  public boolean isExternalSymbol(String name) {
    return _externalSymbols.containsKey(CaseInsensitiveCharSequence.get(name));
  }

  public CaseInsensitiveHashMap<CaseInsensitiveCharSequence, ISymbol> getMap() {
    return _externalSymbols;
  }
}
