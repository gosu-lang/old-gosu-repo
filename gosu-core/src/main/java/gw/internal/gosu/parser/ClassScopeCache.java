/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.parser.CaseInsensitiveCharSequence;
import gw.lang.parser.IFunctionSymbol;
import gw.lang.parser.IScope;

import java.util.Map;
import java.util.Set;

public class ClassScopeCache
{
  private final IScope _staticScope;
  private final Map<CaseInsensitiveCharSequence, Set<IFunctionSymbol>> _staticDfsMap;
  private final IScope _nonstaticScope;
  private final Map<CaseInsensitiveCharSequence, Set<IFunctionSymbol>> _nonstaticDfsMap;

  public ClassScopeCache( IScope staticScope, Map<CaseInsensitiveCharSequence, Set<IFunctionSymbol>> staticDfsMap, IScope nonstaticScope, Map<CaseInsensitiveCharSequence, Set<IFunctionSymbol>> nonstaticDfsMap )
  {
    _staticScope = staticScope;
    _staticDfsMap = staticDfsMap;
    _nonstaticScope = nonstaticScope;
    _nonstaticDfsMap = nonstaticDfsMap;
  }

  public IScope getStaticScope()
  {
    return _staticScope;
  }

  public Map<CaseInsensitiveCharSequence, Set<IFunctionSymbol>> getStaticDfsMap()
  {
    return _staticDfsMap;
  }

  public IScope getNonstaticScope()
  {
    return _nonstaticScope;
  }

  public Map<CaseInsensitiveCharSequence, Set<IFunctionSymbol>> getNonstaticDfsMap()
  {
    return _nonstaticDfsMap;
  }
}
