/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.types;

import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IConstructorInfo;
import gw.lang.reflect.IHasParameterInfos;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.INonLoadableType;
import gw.lang.reflect.IParameterInfo;
import gw.lang.reflect.IPropertyAccessor;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.ITypeLoader;
import gw.lang.reflect.MethodList;
import gw.lang.reflect.PropertyInfoBase;
import gw.lang.reflect.TypeBase;
import gw.lang.reflect.TypeInfoBase;
import gw.lang.reflect.java.JavaTypes;
import gw.util.concurrent.LockingLazyVar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class FunctionLiteralType extends TypeBase implements IType, INonLoadableType
{
  private LockingLazyVar<ITypeInfo> _typeInfo = new LockingLazyVar<ITypeInfo>()
  {
    @Override
    protected ITypeInfo init()
    {
      return makeTypeInfo();
    }
  };

  private IHasParameterInfos _feature;
  private IType _superType;
  private Class _clazz;

  public static FunctionLiteralType resolve( Class clazz, IType superType, IType referenceType, CharSequence name, IType[] parameters )
  {
    IHasParameterInfos feature;
    if( name == null )
    {
      feature = referenceType.getTypeInfo().getConstructor( parameters );
    }
    else
    {
      feature = referenceType.getTypeInfo().getMethod( name, parameters );
    }
    return new FunctionLiteralType( clazz, superType, feature );
  }

  public Class getSerializationClass() {
    return _clazz;
  }

  public IType getSerializationSuperType() {
    return _superType;
  }

  public IType getSerializationReferenceType() {
    return _feature.getOwnersType();
  }

  public String getSerializationName()
  {
    if( _feature instanceof IMethodInfo )
    {
      return _feature.getDisplayName();
    }
    else
    {
      return null;
    }
  }

  public IType[] getSerializationParameters()
  {
    List<IType> paramTypes = new ArrayList<IType>();
    for( IParameterInfo pi : _feature.getParameters() )
    {
      paramTypes.add( pi.getFeatureType() );
    }
    return paramTypes.toArray( new IType[paramTypes.size()] );
  }

  public FunctionLiteralType( Class clazz, IType rawType, IHasParameterInfos feature )
  {
    super( clazz );
    _clazz = clazz;
    _feature = feature;
    _superType = rawType;
  }

  @Override
  public String getName()
  {
    return _feature.getName();
  }

  @Override
  public String getRelativeName()
  {
    return _feature.getName();
  }

  @Override
  public String getNamespace()
  {
    return null;
  }

  @Override
  public ITypeLoader getTypeLoader()
  {
    return _superType.getTypeLoader();
  }

  @Override
  public IType getSupertype()
  {
    return _superType;
  }

  @Override
  public IType[] getInterfaces()
  {
    return _superType.getInterfaces();
  }

  @Override
  public ITypeInfo getTypeInfo()
  {
    return _typeInfo.get();
  }

  protected Set<? extends IType> loadAllTypesInHierarchy() {
    return getAllClassesInClassHierarchyAsIntrinsicTypes( this );
  }

  public boolean isAssignableFrom(IType type) {
    return type.getAllTypesInHierarchy().contains( this );
  }

  private ITypeInfo makeTypeInfo()
  {
    return new FunctionLiteralTypeInfo();
  }

  private class FunctionLiteralTypeInfo extends TypeInfoBase implements ITypeInfo
  {
    private LockingLazyVar<List<? extends IPropertyInfo>> _properties = new LockingLazyVar<List<? extends IPropertyInfo>>()
    {
      @Override
      protected List<? extends IPropertyInfo> init()
      {
        List<IPropertyInfo> properties = new ArrayList( _superType.getTypeInfo().getProperties() );
        IParameterInfo[] parameters = _feature.getParameters();
        for( IParameterInfo parameter : parameters )
        {
          properties.add( new FunctionLiteralParameterProperty( FunctionLiteralTypeInfo.this, parameter ) );
        }
        return properties;
      }
    };

    @Override
    public List<? extends IPropertyInfo> getProperties()
    {
      return _properties.get();
    }

    @Override
    public IPropertyInfo getProperty( CharSequence propName )
    {
      for( IPropertyInfo propertyInfo : getProperties() )
      {
        if( propertyInfo.getName().substring( 0 ).equals( propName ) )
        {
          return propertyInfo;
        }
      }
      return null;
    }

    @Override
    public MethodList getMethods()
    {
      return _superType.getTypeInfo().getMethods();
    }

    @Override
    public List<? extends IConstructorInfo> getConstructors()
    {
      return _superType.getTypeInfo().getConstructors();
    }

    @Override
    public List<IAnnotationInfo> getDeclaredAnnotations()
    {
      return Collections.emptyList();
    }

    @Override
    public IType getOwnersType()
    {
      return FunctionLiteralType.this;
    }
  }

  private class FunctionLiteralParameterProperty extends PropertyInfoBase implements IPropertyInfo
  {
    private String _name;
    private IParameterInfo _parameter;

    public FunctionLiteralParameterProperty( ITypeInfo container, IParameterInfo parameter )
    {
      super( container );
      _name = "$" + parameter.getName();
      _parameter = parameter;
    }

    @Override
    public boolean isReadable()
    {
      return true;
    }

    @Override
    public boolean isWritable( IType whosAskin )
    {
      return false;
    }

    @Override
    public IPropertyAccessor getAccessor()
    {
      return new IPropertyAccessor()
      {
        @Override
        public Object getValue( Object ctx )
        {
          return _parameter;
        }

        @Override
        public void setValue( Object ctx, Object value )
        {
          throw new UnsupportedOperationException( "Can't set property value!" );
        }
      };
    }

    @Override
    public List<IAnnotationInfo> getDeclaredAnnotations()
    {
      return Collections.emptyList();
    }

    @Override
    public String getName()
    {
      return _name;
    }

    @Override
    public IType getFeatureType()
    {
      return JavaTypes.getGosuType(IParameterInfo.class);
    }
  }
}
