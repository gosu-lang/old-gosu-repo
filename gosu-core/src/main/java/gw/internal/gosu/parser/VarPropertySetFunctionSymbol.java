/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.internal.gosu.parser.statements.SyntheticFunctionStatement;
import gw.lang.parser.GosuParserTypes;
import gw.lang.reflect.FunctionType;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.parser.CaseInsensitiveCharSequence;
import gw.lang.parser.IGosuParser;
import gw.lang.parser.ISymbol;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.ScriptPartId;
import gw.lang.reflect.gs.ICompilableType;
import gw.lang.reflect.IType;

import java.util.ArrayList;

/**
 */
public class VarPropertySetFunctionSymbol extends DynamicFunctionSymbol
{
  private CaseInsensitiveCharSequence _varIdentifier;

  public VarPropertySetFunctionSymbol( ICompilableType gsClass, ISymbolTable symTable, String strProperty, CharSequence strVarIdentifier, IType varType )
  {
    super( symTable, '@' + strProperty,
           new FunctionType( '@' + strProperty, GosuParserTypes.NULL_TYPE(), new IType[]{varType} ),
           getSymbolArray( "__value_", varType ), new SyntheticFunctionStatement() );
    SyntheticFunctionStatement stmt = (SyntheticFunctionStatement)getValueDirectly();
    stmt.setDfsOwner( this );
    _scriptPartId = new ScriptPartId( gsClass, null );
    _varIdentifier = CaseInsensitiveCharSequence.get( strVarIdentifier );
  }

  private static ArrayList<ISymbol> getSymbolArray( CharSequence strVarIdentifier, IType varType )
  {
    ArrayList<ISymbol> arr = new ArrayList<ISymbol>( 1 );
    final Symbol symbol = new Symbol( strVarIdentifier, varType, null );
    symbol._iIndex = 1;
    arr.add( symbol );
    return arr;
  }

  public CaseInsensitiveCharSequence getVarIdentifier()
  {
    return _varIdentifier;
  }

  public DynamicFunctionSymbol getParameterizedVersion( IGosuClass gsClass )
  {
    return this;
  }
}
