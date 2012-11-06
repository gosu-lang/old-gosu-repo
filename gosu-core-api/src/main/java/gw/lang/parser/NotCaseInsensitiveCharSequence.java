/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.lang.parser;


public class NotCaseInsensitiveCharSequence extends CaseInsensitiveCharSequence
{
  /**
   */
  NotCaseInsensitiveCharSequence( CharSequence rawCharSequence )
  {
    super( rawCharSequence );
  }

  public boolean equals( Object o )
  {
    if( this == o )
    {
      return true;
    }
    if( !(o instanceof NotCaseInsensitiveCharSequence) )
    {
      return false;
    }
    return _string.equals( ((NotCaseInsensitiveCharSequence)o)._string );
  }

  public static int _getLowerCaseHashCode( CharSequence charSeq )
  {
    return charSeq.hashCode();
  }

  public boolean equalsIgnoreCase( CharSequence cs )
  {
    return equalsIgnoreCase( this, cs );
  }

  private Object readResolve()
  {
    return get( _string );
  }

  public String toLowerCase()
  {
    return _string.toLowerCase();
  }
}
