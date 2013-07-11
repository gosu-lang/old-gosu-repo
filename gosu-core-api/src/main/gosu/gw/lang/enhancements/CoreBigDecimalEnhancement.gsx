package gw.lang.enhancements

/*
 *  Copyright 2013 Guidewire Software, Inc.
 */
enhancement CoreBigDecimalEnhancement : java.math.BigDecimal
{
  
  property get IsZero() : boolean
  {
    return this.negate() == this
  }

}
