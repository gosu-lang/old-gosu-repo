package gw.lang.enhancements
uses java.lang.Short

/*
 *  Copyright 2010 Guidewire Software, Inc.
 */
enhancement CoreIterableOfShortsEnhancement : java.lang.Iterable<Short> {
  function sum() : int {
    var sum = 0
    for (elt in this) {
      sum += elt  
    }
    return sum
  }
}
