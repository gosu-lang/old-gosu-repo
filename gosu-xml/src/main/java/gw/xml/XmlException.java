/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.xml;

public class XmlException extends RuntimeException {

  public XmlException( String msg, Throwable cause ) {
    super( msg, cause );
  }

  public XmlException() {
  }

  public XmlException( String msg ) {
    super( msg );
  }
}
