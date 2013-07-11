package gw.lang.enhancements
uses java.lang.Integer

/*
 *  Copyright 2013 Guidewire Software, Inc.
 */
enhancement CoreArrayOfIntegersEnhancement : Integer[] {
  function sum() : Integer {
    var sum = 0
    for (elt in this) {
      sum += elt  
    }
    return sum
  }
}
