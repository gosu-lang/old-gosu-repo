/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.lang.javadoc;

import gw.lang.UnstableAPI;

@UnstableAPI
public interface IExceptionNode {

  String getDescription();

  void setDescription( String value );

  String getType();

  void setType( String name );
  
}
