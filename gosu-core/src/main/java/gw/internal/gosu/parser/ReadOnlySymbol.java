/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.parser.CaseInsensitiveCharSequence;
import gw.lang.reflect.IType;
import gw.lang.parser.IStackProvider;
import gw.lang.parser.ISymbol;

/**
 */
public class ReadOnlySymbol extends Symbol
{
  public ReadOnlySymbol( CharSequence strName, IType type, IStackProvider provider, Object value )
  {
    super( strName, type, provider, value );
  }

  public ReadOnlySymbol( CaseInsensitiveCharSequence strName, IType type, IStackProvider provider, Object value )
  {
    super( strName, type, provider, value );
  }

  public ReadOnlySymbol( CharSequence strName, IType type, IStackProvider provider, Object value, CaseInsensitiveCharSequence caseInsensitiveName )
  {
    super( strName, type, provider, value, caseInsensitiveName );
  }

  public ISymbol getLightWeightReference()
  {
    return new ReadOnlySymbol( getName(), getType(), _stackProvider, _value, getCaseInsensitiveName() );
  }

  public boolean isWritable()
  {
    return false;
  }
}
