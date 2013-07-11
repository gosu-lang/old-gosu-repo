/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.schema;

import gw.internal.xml.xsd.typeprovider.XmlSchemaIndex;
import gw.lang.reflect.LocationInfo;

public abstract class XmlSchemaFacet<T extends XmlSchemaFacet> extends XmlSchemaObject<T> {

  private final String _value;

  public XmlSchemaFacet( XmlSchemaIndex schemaIndex, LocationInfo locationInfo, String value ) {
    super( schemaIndex, locationInfo );
    _value = value;
  }

  // TODO - each facet class could store its own value in its own way for better performance.
  // For example, the pattern facet could pre-compile the pattern
  public String getValue() {
    return _value;
  }
}
