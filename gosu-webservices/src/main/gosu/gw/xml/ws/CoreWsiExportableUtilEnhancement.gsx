/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.xml.ws
uses gw.xml.XmlElement

enhancement CoreWsiExportableUtilEnhancement : gw.xml.ws.WsiExportableUtil {
  public static function unmarshal<T>(el : XmlElement) : T {
    return WsiExportableUtil.unmarshal(el, T) as T
  }
  
}
