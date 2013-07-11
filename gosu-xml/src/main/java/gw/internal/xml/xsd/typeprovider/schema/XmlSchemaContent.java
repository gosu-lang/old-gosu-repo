/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.schema;

import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.lang.reflect.LocationInfo;

public abstract class XmlSchemaContent<T extends XmlSchemaContent> extends XmlSchemaObject<T> {

  public XmlSchemaContent( XmlSchemaIndex schemaIndex, LocationInfo locationInfo ) {
    super( schemaIndex, locationInfo );
  }

}
