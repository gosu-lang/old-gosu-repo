/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.parser.IReducedSymbol;
import gw.lang.reflect.*;
import gw.lang.reflect.gs.IGosuMethodParamInfo;
import gw.lang.reflect.java.JavaTypes;
import gw.util.concurrent.LockingLazyVar;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 */
public class GosuMethodParamInfo extends GosuBaseAttributedFeatureInfo implements IGosuMethodParamInfo
{
  private IReducedSymbol _arg;
  private IType _type;

  private LockingLazyVar<String> _description = new LockingLazyVar<String>()
  {
    protected String init()
    {
      HashMap<String, String> nameToDescMap = new HashMap<String, String>();
      List<IAnnotationInfo> paramAnnotations = ((IAnnotatedFeatureInfo)getContainer()).getAnnotationsOfType(JavaTypes.PARAM());
      if( paramAnnotations != null )
      {
        for( IAnnotationInfo annotation : paramAnnotations )
        {
          nameToDescMap.put( (String)annotation.getFieldValue( "FieldName" ),
                             (String)annotation.getFieldValue( "FieldDescription" ) );
        }
      }

      return nameToDescMap.get( _arg.getName() );
    }
  };

  public GosuMethodParamInfo( IFeatureInfo container, IReducedSymbol arg, IType type )
  {
    super( container );
    _arg = arg;
    _type = type; // getActualTypeInContainer( getContainer(), type );
  }

  public IType getFeatureType()
  {
    return _type;
  }

  public String getName()
  {
    return _arg.getName();
  }

  protected List<IGosuAnnotation> getGosuAnnotations()
  {
    return Collections.emptyList();
  }

  public List<IAnnotationInfo> getDeclaredAnnotations()
  {
    return Collections.emptyList();
  }

  public String getDisplayName()
  {
    return _arg.getDisplayName();
  }

  public String getShortDescription()
  {
    return _arg.getDisplayName();
  }

  public String getDescription()
  {
    return _description.get();
  }

  public boolean isStatic()
  {
    return false;
  }
}
