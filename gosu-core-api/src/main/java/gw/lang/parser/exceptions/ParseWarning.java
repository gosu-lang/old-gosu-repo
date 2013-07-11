/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.lang.parser.exceptions;

import gw.lang.parser.resources.ResourceKey;
import gw.lang.parser.IParserState;
import gw.lang.parser.ISymbolTable;
import gw.lang.reflect.IType;

public class ParseWarning extends ParseIssue
{
  public ParseWarning( IParserState state, ResourceKey msgKey, Object... msgArgs )
  {
    super( state, msgKey, msgArgs );
  }

  public ParseWarning( ParseWarning e )
  {
    super( e.getLineNumber(), e.getLineOffset(), e.getTokenColumn(), e.getTokenStart(), e.getTokenEnd(),
           e.getSymbolTable(), e.getMessageKey(), e.getMessageArgs() );
    setSource( e.getSource() );
  }

  public ParseWarning( Integer lineNumber, Integer lineOffset, Integer tokenColumn, Integer tokenStart, Integer tokenEnd, ISymbolTable symbolTable, ResourceKey key, Object... msgArgs )
  {
    super( lineNumber, lineOffset, tokenColumn, tokenStart, tokenEnd, symbolTable, key, msgArgs );
  }

  public IType getExpectedType()
  {
    return null;
  }

  public void setExpectedType( IType argType )
  {
    throw new UnsupportedOperationException( "Parse warnings to do not maintain 'expected types'" );
  }
}
