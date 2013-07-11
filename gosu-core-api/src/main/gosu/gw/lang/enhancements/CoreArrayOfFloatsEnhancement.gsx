package gw.lang.enhancements
uses java.lang.Float

/*
 *  Copyright 2013 Guidewire Software, Inc.
 */
enhancement CoreArrayOfFloatsEnhancement : Float[] {
  function sum() : Float {
    var sum = 0.0 as float
    for (elt in this) {
      sum += elt  
    }
    return sum
  }
}
