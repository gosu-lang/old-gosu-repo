/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.lang.ir;

import gw.lang.UnstableAPI;

@UnstableAPI
public abstract class IRElement {
  private IRElement _parent;
  private int _iLineNumber;
  private boolean _bImplicit;

  protected IRElement() {
    _iLineNumber = -1;
  }

  public IRElement getParent() {
    return _parent;
  }

  public void setParent( IRElement parent ) {
    _parent = parent;
  }

  protected void setParentToThis( IRElement element ) {
    if (element != null) {
      element.setParent( this );
    }
  }

  public boolean isImplicit() {
    return _bImplicit || (getParent() != null && getParent().isImplicit());
  }
  public void setImplicit( boolean bImplicit ) {
    _bImplicit = bImplicit;
  }

  public int getLineNumber() {
    if( !isImplicit() ) {
      return _iLineNumber;
    }
    return -1;
  }
  public void setLineNumber( int iLineNumber ) {
    if( !isImplicit() ) {
      _iLineNumber = iLineNumber;
    }
  }
}
