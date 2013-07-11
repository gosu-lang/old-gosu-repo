/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.internal.xml.ws.typeprovider;

import gw.lang.reflect.IType;

public class HeaderInfo {

  private final String _name;
  private final String _description;
  private final IType _type;

  public HeaderInfo( String name, String description, IType type ) {
    _name = name;
    _description = description;
    _type = type;
  }

  public String getName() {
    return _name;
  }

  public String getDescription() {
    return _description;
  }

  public IType getType() {
    return _type;
  }
  
}
