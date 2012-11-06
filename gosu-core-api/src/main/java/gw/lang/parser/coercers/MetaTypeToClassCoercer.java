/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.lang.parser.coercers;

import gw.lang.parser.IResolvingCoercer;
import gw.lang.reflect.IHasJavaClass;
import gw.lang.reflect.IType;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.java.JavaTypes;

public class MetaTypeToClassCoercer extends BaseCoercer implements IResolvingCoercer
{
  private static final MetaTypeToClassCoercer INSTANCE = new MetaTypeToClassCoercer();

  public Object coerceValue( IType typeToCoerceTo, Object value )
  {
    IHasJavaClass  javaIntrinsicType = (IHasJavaClass )value;
    return javaIntrinsicType.getBackingClass();
  }

  public boolean isExplicitCoercion()
  {
    return false;
  }

  public boolean handlesNull()
  {
    return false;
  }

  public IType resolveType( IType target, IType source )
  {
    if( target.getGenericType() == JavaTypes.CLASS() )
    {
      return JavaTypes.CLASS().getParameterizedType(source.getTypeParameters()[0]);
    }
    else
    {
      return source;
    }
  }

  public int getPriority( IType to, IType from )
  {
    return 1;
  }

  public static MetaTypeToClassCoercer instance()
  {
    return INSTANCE;
  }
}
