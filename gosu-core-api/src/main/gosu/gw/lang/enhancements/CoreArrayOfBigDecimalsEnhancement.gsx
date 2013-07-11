package gw.lang.enhancements
uses java.math.BigDecimal

/*
 *  Copyright 2013 Guidewire Software, Inc.
 */
enhancement CoreArrayOfBigDecimalsEnhancement : BigDecimal[] {
  function sum() : BigDecimal {
    var sum = BigDecimal.ZERO
    for (elt in this) {
      sum += elt  
    }
    return sum
  }
}
