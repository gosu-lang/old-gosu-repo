/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.internal.xml.xsd.typeprovider;

import gw.lang.parser.GosuParserFactory;
import gw.lang.parser.IExpression;
import gw.lang.parser.IGosuParser;
import gw.lang.parser.TypelessScriptPartId;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IBlockType;
import gw.lang.reflect.IConstructorInfo;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IParameterInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.java.GosuTypes;
import gw.lang.reflect.java.JavaTypes;

/**
 *
 */
class XmlSchemaAutocreateAnnotationData implements IAnnotationInfo {
  private final IType _concreteType;
  private final IFeatureInfo _container;

  public XmlSchemaAutocreateAnnotationData( IType concreteType, IFeatureInfo container ) {
    _concreteType = concreteType;
    _container = container;
  }

  @Override
  public Object getInstance() {
    ITypeInfo autocreateTypeInfo = GosuTypes.AUTOCREATE().getTypeInfo();
    if ( _concreteType != null && JavaTypes.LIST().isAssignableFrom( _concreteType ) ) {
      for ( IConstructorInfo constructor : autocreateTypeInfo.getConstructors()) {
        IParameterInfo[] constructorParams = constructor.getParameters();
        if (constructorParams.length > 0 && constructorParams[0].getFeatureType() instanceof IBlockType ) {
          try {
            IGosuParser parser = GosuParserFactory.createParser( "\\ -> new java.util.ArrayList()" );
            IExpression expr = parser.parseExp( new TypelessScriptPartId( "Xml Schema autocreate block" ) );
            Object block = expr.evaluate();
            return constructor.getConstructor().newInstance(block);
          } catch (Exception e) {
            throw new RuntimeException(e);
          }
        }
      }
    }
    return autocreateTypeInfo.getConstructor().getConstructor().newInstance();
  }

  @Override
  public Object getFieldValue(String field) {
    throw new RuntimeException("Not supported yet");
  }

  @Override
  public IType getType() {
    return GosuTypes.AUTOCREATE();
  }

  @Override
  public String getName() {
    return GosuTypes.AUTOCREATE().getName();
  }

  @Override
  public String getDescription() {
    return "";
  }

  @Override
  public IType getOwnersType() {
    return _container.getOwnersType();
  }

}
