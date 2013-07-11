/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider.validator;

import gw.internal.xml.XmlDeserializationContext;
import gw.internal.xml.XmlSimpleValueValidationContext;
import gw.internal.xml.xsd.typeprovider.XmlWhitespaceHandling;
import gw.internal.xml.xsd.typeprovider.primitive.XmlSchemaPrimitiveType;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaEnumerationFacet;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaFacet;
import gw.internal.xml.xsd.typeprovider.schema.XmlSchemaWhiteSpaceFacet;
import gw.internal.xml.xsd.typeprovider.simplevaluefactory.XmlSimpleValueFactory;
import gw.xml.XmlSimpleValue;
import gw.xml.XmlSimpleValueException;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class XmlSimpleTypeSimpleValueValidator extends XmlSimpleValueValidator {

  private final XmlSimpleValueValidator _parent;
  private final List<XmlSchemaFacet> _facets;
  private final XmlSchemaPrimitiveType _primitiveType;
  private final XmlSimpleValueFactory _valueFactory;

  public XmlSimpleTypeSimpleValueValidator( XmlSchemaPrimitiveType primitiveType, XmlSimpleValueValidator parent, List<XmlSchemaFacet> facets, XmlSimpleValueFactory valueFactory ) {
    _primitiveType = primitiveType;
    _parent = parent;
    _facets = facets;
    _valueFactory = valueFactory;
  }

  public void validate( String value, XmlSimpleValueValidationContext validationContext ) throws XmlSimpleValueException {
    final XmlSchemaPrimitiveType primitiveType = getPrimitiveType();
    Set<String> enumerationOptions = new HashSet<String>();
    for ( XmlSchemaFacet facet : _facets ) {
      if ( facet instanceof XmlSchemaEnumerationFacet ) {
        XmlSchemaEnumerationFacet enumFacet = (XmlSchemaEnumerationFacet) facet;
        XmlDeserializationContext context = new XmlDeserializationContext( null );
        for ( Map.Entry<String, String> entry : enumFacet.getNamespaceContext().entrySet() ) {
          context.addNamespace( entry.getKey(), entry.getValue() );
        }
        XmlSimpleValue simpleValue = _valueFactory.deserialize( context, facet.getValue(), false );
        enumerationOptions.add( simpleValue.getStringValue() );
      }
      else {
        XmlSchemaFacetValidator.validate( facet, collapseWhitespace( value, validationContext ), validationContext, primitiveType );
      }
    }
    if ( ! enumerationOptions.isEmpty() ) {
      // check enumeration options
      if ( ! enumerationOptions.contains( value ) ) {
        StringBuilder sb = new StringBuilder();
        for ( String option : enumerationOptions ) {
          if ( sb.length() > 0 ) {
            sb.append( ", " );
          }
          sb.append( option );
        }
        throw new XmlSimpleValueException( "value does not match enumeration [ " + sb + " ]" );
      }
    }
    if ( _parent != null ) {
      _parent.validate( value, validationContext );
    }
  }

  public XmlSchemaPrimitiveType getPrimitiveType() {
    return _primitiveType == null ? _parent.getPrimitiveType() : _primitiveType;
  }

  @Override
  protected XmlWhitespaceHandling getWhitespaceHandling( String value ) {
    for ( XmlSchemaFacet facet : _facets ) {
      if ( facet instanceof XmlSchemaWhiteSpaceFacet ) {
        return XmlWhitespaceHandling.valueOf( facet.getValue() );
      }
    }
    return _parent == null ? XmlWhitespaceHandling.preserve : _parent.getWhitespaceHandling( value );
  }

}
