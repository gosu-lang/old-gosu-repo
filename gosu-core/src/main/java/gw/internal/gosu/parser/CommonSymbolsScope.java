/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.config.CommonServices;
import gw.lang.parser.GosuParserTypes;
import gw.lang.parser.ISymbol;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.StandardScope;
import gw.lang.parser.StandardSymbolTable;
import gw.lang.reflect.FunctionType;
import gw.lang.reflect.IType;
import gw.lang.reflect.java.JavaTypes;
import gw.util.GosuExceptionUtil;

import java.util.Map;

/**
 */
public class CommonSymbolsScope<K extends CharSequence, V extends ISymbol> extends StandardScope<K, V>
{
  private static final String NOW = "now" ;
  private static final String PRINT = "print";

  public static CommonSymbolsScope make()
  {
    return new CommonSymbolsScope();
  }

  //## Note these are all singletons, so there's no need to specify a
  //## IStackProvider -- there's no need for a stack for these at runtime
  //## because a Symbol w/o a IStackProvider manages its own storage.
  @SuppressWarnings({"unchecked"})
  private CommonSymbolsScope()
  {
    super( null, 6 );
    try
    {
      // Methods for testing
      if( CommonServices.getEntityAccess().getLanguageLevel().allowGlobalNowSymbol() )
      {
        super.put( (K) NOW, (V)new LockedDownSymbol( NOW, new FunctionType( "now", GosuParserTypes.STRING_TYPE(), null ),
                                                     StandardSymbolTable.NOW ) );
      }
      super.put( (K) PRINT, (V)new LockedDownSymbol( PRINT, new FunctionType( "print", GosuParserTypes.NULL_TYPE(), new IType[]{JavaTypes.OBJECT()} ),
                                                     StandardSymbolTable.PRINT ) );
    }
    catch( Exception e )
    {
      throw GosuExceptionUtil.forceThrow( e );
    }
  }

  @Override
  public V put( K key, V value )
  {
    throw new UnsupportedOperationException( "Cannot add symbols to the CommonSymbolsScope" );
  }

  @Override
  public void putAll( Map m )
  {
    throw new UnsupportedOperationException( "Cannot add symbols to the CommonSymbolsScope" );
  }

  public static class LockedDownSymbol extends Symbol
  {
    public LockedDownSymbol( CharSequence strName, IType type, Object value )
    {
      super( strName.toString(), type, value );
    }

    @Override
    public void setDynamicSymbolTable( ISymbolTable symTable )
    {
      // Do nothing
    }

    @Override
    public Object getValue()
    {
      return getValueDirectly();
    }
  }


}
