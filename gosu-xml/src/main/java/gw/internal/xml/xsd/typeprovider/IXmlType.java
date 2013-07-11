/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider;

import gw.lang.reflect.IFileBasedType;

public interface IXmlType extends IFileBasedType {

  IXmlTypeData getTypeData();

}
