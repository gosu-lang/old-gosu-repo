package gw.lang.enhancements

/*
 *  Copyright 2010 Guidewire Software, Inc.
 */
enhancement CoreBigIntegerEnhancement : java.math.BigInteger
{
  
  property get IsZero() : boolean
  {
    return this.negate() == this
  }

}
