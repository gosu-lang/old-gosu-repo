/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.expressions;

import gw.internal.gosu.parser.Expression;
import gw.internal.gosu.parser.statements.TryCatchFinallyStatement;
import gw.internal.gosu.parser.statements.CatchClause;


import gw.lang.parser.CaseInsensitiveCharSequence;
import gw.lang.parser.IDynamicFunctionSymbol;
import gw.lang.parser.IParseTree;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.ISymbol;
import gw.lang.parser.expressions.IBlockExpression;
import gw.lang.parser.expressions.ILocalVarDeclaration;
import gw.lang.parser.statements.IForEachStatement;
import gw.lang.parser.statements.IFunctionStatement;

public class LocalVarDeclaration extends Expression implements ILocalVarDeclaration
{
  private final String _strLocalVarName;

  public LocalVarDeclaration( String strLocalVarName )
  {
    _strLocalVarName = strLocalVarName;
  }

  public CharSequence getLocalVarName()
  {
    return _strLocalVarName;
  }

  public Object evaluate()
  {
    return null; // Nothing to do
  }

  @Override
  public String toString()
  {
    return getLocalVarName().toString();
  }

  public int getNameOffset( CaseInsensitiveCharSequence identifierName )
  {
    return getLocation().getOffset();
  }
  public void setNameOffset( int iOffset, CaseInsensitiveCharSequence identifierName )
  {
    // Can't set the name offset w/o also setting the location, so this is a no-op 
  }

  public boolean declares( CaseInsensitiveCharSequence identifierName )
  {
    return identifierName != null && identifierName.equalsIgnoreCase( _strLocalVarName );
  }

  public TypeLiteral getTypeLiteral()
  {
    for( IParseTree parseTree : getLocation().getChildren() )
    {
      if( parseTree.getParsedElement() instanceof TypeLiteral )
      {
        return (TypeLiteral)parseTree.getParsedElement();
      }
    }

    return null;
  }

  public ISymbol getSymbol() {
    return findSymbol( getParent() );
  }

  private ISymbol findSymbol( IParsedElement elem ) {
    if( elem == null ) {
      return null;
    }

    if( elem instanceof IFunctionStatement ) {
      IDynamicFunctionSymbol dfs = ((IFunctionStatement)elem).getDynamicFunctionSymbol();
      for(ISymbol symbol: dfs.getArgs()) {
        if( _strLocalVarName.equalsIgnoreCase(symbol.getName())) {
          return symbol;
        }
      }
    }
    else if( elem instanceof IBlockExpression ) {
      for(ISymbol symbol : ((IBlockExpression)elem).getArgs()) {
        if( _strLocalVarName.equalsIgnoreCase(symbol.getName())) {
          return symbol;
        }
      }
    }
    else if( elem instanceof IForEachStatement ) {
      ISymbol symbol = ((IForEachStatement)elem).getIdentifier();
      if( _strLocalVarName.equalsIgnoreCase(symbol.getName())) {
        return symbol;
      }
      symbol = ((IForEachStatement)elem).getIndexIdentifier();
      if( _strLocalVarName.equalsIgnoreCase(symbol.getName())) {
        return symbol;
      }
    }
    else if( elem instanceof TryCatchFinallyStatement ) {
      for(CatchClause catchClause : ((TryCatchFinallyStatement)elem).getCatchStatements() ) {
        if( _strLocalVarName.equalsIgnoreCase(catchClause.getSymbol().getName())) {
          return catchClause.getSymbol();
        }
      }
    }

    return findSymbol( elem.getParent() );
  }

  @Override
  public String[] getDeclarations() {
    return new String[]{_strLocalVarName};
  }

}