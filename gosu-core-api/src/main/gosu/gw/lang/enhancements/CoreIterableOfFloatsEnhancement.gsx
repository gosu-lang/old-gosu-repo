package gw.lang.enhancements
uses java.lang.Float

/*
 *  Copyright 2010 Guidewire Software, Inc.
 */
enhancement CoreIterableOfFloatsEnhancement : java.lang.Iterable<Float> {
  function sum() : Float {
    var sum = 0.0 as float
    for (elt in this) {
      sum += elt  
    }
    return sum
  }
}
