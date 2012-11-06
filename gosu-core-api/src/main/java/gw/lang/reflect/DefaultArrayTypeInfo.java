/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.lang.reflect;

import gw.config.CommonServices;
import gw.lang.GosuShop;
import gw.util.CaseInsensitiveHashMap;
import gw.util.concurrent.LockingLazyVar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DefaultArrayTypeInfo implements ITypeInfo
{
  private IType _type;
  private LockingLazyVar<MethodList> _methods;
  private Map<String, IPropertyInfo> _properties;
  private ArrayList<IPropertyInfo> _propertiesList;


  public DefaultArrayTypeInfo( IType defaultArrayIntrinsicType )
  {
    _type = defaultArrayIntrinsicType;

    _methods =
      new LockingLazyVar<MethodList>()
      {
        protected MethodList init()
        {
          MethodList methods = new MethodList();
          CommonServices.getEntityAccess().addEnhancementMethods(_type, methods );
          return methods;
        }
      };

    _properties = new CaseInsensitiveHashMap<String, IPropertyInfo>();
    _properties.put( "length", makeLengthProperty() );
    CommonServices.getEntityAccess().addEnhancementProperties(_type, _properties, false );
    _propertiesList = new ArrayList<IPropertyInfo>( _properties.values() );
  }

  private IPropertyInfo makeLengthProperty()
  {
    return GosuShop.createLengthProperty(this);
  }

  public List<? extends IPropertyInfo> getProperties()
  {
    return _propertiesList;
  }

  public IPropertyInfo getProperty( CharSequence propName )
  {
    //noinspection SuspiciousMethodCalls
    return _properties.get( propName );
  }

  public CharSequence getRealPropertyName(CharSequence propName)
  {
    IPropertyInfo property = getProperty( propName );
    return property == null ? null : property.getName();
  }

  @SuppressWarnings({"unchecked"})
  public MethodList getMethods()
  {
    return _methods.get();
  }

  public IMethodInfo getMethod( CharSequence methodName, IType... params )
  {
    //noinspection unchecked
    return FIND.method( getMethods(), methodName, params );
  }

  @SuppressWarnings({"unchecked"})
  public List getConstructors()
  {
    return Collections.EMPTY_LIST;
  }

  public IConstructorInfo getConstructor( IType... params )
  {
    return null;
  }

  public IMethodInfo getCallableMethod( CharSequence strMethod, IType... params )
  {
    //noinspection unchecked
    return FIND.callableMethod( getMethods(), strMethod, params );
  }

  public IConstructorInfo getCallableConstructor( IType... params )
  {
    //noinspection unchecked
    return FIND.callableConstructor( getConstructors(), params );
  }

  @SuppressWarnings({"unchecked"})
  public List getEvents()
  {
    return Collections.EMPTY_LIST;
  }

  public IEventInfo getEvent( CharSequence strEvent )
  {
    return null;
  }

  public List<IAnnotationInfo> getAnnotations()
  {
    return Collections.emptyList();
  }

  public List<IAnnotationInfo> getDeclaredAnnotations()
  {
    return Collections.emptyList();
  }

  public List<IAnnotationInfo> getAnnotationsOfType( IType type )
  {
    return Collections.emptyList();
  }

  @Override
  public IAnnotationInfo getAnnotation( IType type )
  {
    return null;
  }

  @Override
  public boolean hasDeclaredAnnotation( IType type )
  {
    return false;
  }

  public boolean hasAnnotation( IType type )
  {
    return false;
  }

  public IFeatureInfo getContainer()
  {
    return null;
  }

  public IType getOwnersType()
  {
    return _type;
  }

  /**
   */
  public String getName()
  {
    return _type.getRelativeName();
  }

  public String getDisplayName()
  {
    return _type.getComponentType().getTypeInfo().getDisplayName() + "[]";
  }

  public String getDescription()
  {
    return "Array of " + _type.getComponentType().getRelativeName() + " objects";
  }

  public boolean isDeprecated() {
    return false;
  }

  public String getDeprecatedReason() {
    return null;
  }
}