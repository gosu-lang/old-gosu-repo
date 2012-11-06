/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.lang.annotation;

import gw.lang.reflect.IConstructorInfo;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IPropertyInfo;
import gw.lang.reflect.ITypeInfo;

import java.lang.annotation.ElementType;

public enum UsageTarget
{
  /**
   * Specifies this modifier applies to everything
   */
  AllTarget,

  /**
   * Specifies this modifier applies to a class
   */
  TypeTarget,

  /**
   * Specifies this modifier applies to a constructor
   */
  ConstructorTarget,

  /**
   * Specifies this modifier applies to a property
   */
  PropertyTarget,

  /**
   * Specifies this modifier applies to a method
   */
  MethodTarget;

  public static UsageTarget convert( ElementType elementType )
  {
    switch( elementType )
    {
      case CONSTRUCTOR:
        return ConstructorTarget;
      case FIELD:
        return PropertyTarget;
      case TYPE:
        return TypeTarget;
      case METHOD:
        return UsageTarget.MethodTarget;
    }
    return AllTarget;
  }

  public static UsageTarget getForFeature( IFeatureInfo fi )
  {
    if( fi instanceof IConstructorInfo )
    {
      return ConstructorTarget;
    }
    else if( fi instanceof IPropertyInfo )
    {
      return PropertyTarget;
    }
    else if( fi instanceof IMethodInfo )
    {
      return MethodTarget;
    }
    else if( fi instanceof ITypeInfo )
    {
      return TypeTarget;
    }
    else
    {
      return AllTarget;
    }
  }
}
