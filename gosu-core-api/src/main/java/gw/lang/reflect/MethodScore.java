/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.lang.reflect;

import gw.lang.parser.IExpression;

import java.util.List;

public final class MethodScore implements Comparable<MethodScore>
{
  private long _iScore;
  private boolean _bValid;
  private IInvocableType _rawFuncType;
  private IInvocableType _inferredFuncType;
  private List<IExpression> _exprs;
  private int[] _namedArgOrder;

  /**
   * @return true if this score represents an actual matching method score rather than
   * just a placeholder indicating that no method matched
   */
  public boolean isValid()
  {
    return _bValid;
  }

  public long getScore()
  {
    return _iScore;
  }

  public void setScore( long iScore )
  {
    _iScore = iScore;
  }

  public void incScore( int amount )
  {
    _iScore += amount;
  }

  public void setValid( boolean valid )
  {
    _bValid = valid;
  }

  public IInvocableType getRawFunctionType()
  {
    return _rawFuncType;
  }

  public void setRawFunctionType( IInvocableType funcType )
  {
    _rawFuncType = funcType;
  }

  public IInvocableType getInferredFunctionType()
  {
    return _inferredFuncType;
  }

  public void setInferredFunctionType( IInvocableType funcType )
  {
    _inferredFuncType = funcType;
  }

  public int compareTo( MethodScore o )
  {
    // if the scores are the same, compare their signatures for great stability justice
    if( _iScore == o._iScore )
    {
      return o._rawFuncType.getParamSignature().toString().compareTo( _rawFuncType.getParamSignature().toString() );
    }
    else
    {
      return _iScore < o._iScore ? 1 : -1;
    }
  }

  public void setArguments( List<IExpression> argExpressions )
  {
    _exprs = argExpressions;
  }

  public List<IExpression> getArguments()
  {
    return _exprs;
  }

  public boolean matchesArgSize( List<? extends IExpression> argExpressions )
  {
    return _rawFuncType.getParameterTypes().length == argExpressions.size();
  }

  public int[] getNamedArgOrder()
  {
    return _namedArgOrder;
  }
  public void setNamedArgOrder( List<Integer> namedArgOrder )
  {
    if( namedArgOrder != null )
    {
      _namedArgOrder = new int[namedArgOrder.size()];
      for( int i = 0; i < _namedArgOrder.length; i++ )
      {
        _namedArgOrder[i] = namedArgOrder.get( i );
      }
    }
    else
    {
      _namedArgOrder = null;
    }
  }
}