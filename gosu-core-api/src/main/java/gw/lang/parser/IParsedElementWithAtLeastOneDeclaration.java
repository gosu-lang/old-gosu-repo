/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.lang.parser;

import gw.lang.reflect.IFeatureInfo;


import java.util.Stack;
import java.util.List;

public interface IParsedElementWithAtLeastOneDeclaration extends IParsedElement
{
  /**
   * The offset of the token representing the name for the declaration
   * @param identifierName
   */
  int getNameOffset( CaseInsensitiveCharSequence identifierName );
  void setNameOffset( int iOffset, CaseInsensitiveCharSequence identifierName );

  /**
   * @param identifierName
   * @return True if this statement declares the given identifier; false otherwise
   */
  boolean declares(CaseInsensitiveCharSequence identifierName);
  
  /**
   * @return all names declared by this element
   */
  String[] getDeclarations();
}
