package gw.lang.enhancements
uses java.math.BigDecimal

/*
 *  Copyright 2013 Guidewire Software, Inc.
 */
enhancement CoreArrayOfNumbersEnhancement<N extends java.lang.Number> : N[]
{
  function average() : BigDecimal {
    return this.toList().average( \ i -> i )
  }
}
