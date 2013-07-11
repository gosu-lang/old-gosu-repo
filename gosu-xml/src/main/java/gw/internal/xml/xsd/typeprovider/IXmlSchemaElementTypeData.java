/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider;

public interface IXmlSchemaElementTypeData<T> extends IXmlSchemaTypeData<T> {

  XmlSchemaTypeSchemaInfo getSchemaInfo();

  XmlSchemaTypeInstanceTypeData getXmlTypeInstanceTypeData();

}
