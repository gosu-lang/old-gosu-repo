package gw.lang.enhancements

/*
 *  Copyright 2013 Guidewire Software, Inc.
 */
enhancement CoreBigIntegerEnhancement : java.math.BigInteger
{
  
  property get IsZero() : boolean
  {
    return this.negate() == this
  }

}
