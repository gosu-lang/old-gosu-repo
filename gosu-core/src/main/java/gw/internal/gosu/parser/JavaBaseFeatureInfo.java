/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.Deprecated;
import gw.lang.PublishInGosu;
import gw.lang.reflect.BaseFeatureInfo;
import gw.lang.reflect.IAnnotationInfo;
import gw.lang.reflect.IFeatureInfo;
import gw.lang.reflect.IScriptabilityModifier;
import gw.lang.reflect.IType;
import gw.lang.reflect.java.IJavaAnnotatedElement;
import gw.lang.reflect.java.IJavaClassConstructor;
import gw.lang.reflect.java.IJavaClassField;
import gw.lang.reflect.java.IJavaClassInfo;
import gw.lang.reflect.java.IJavaClassMethod;
import sun.reflect.annotation.AnnotationParser;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class JavaBaseFeatureInfo extends BaseFeatureInfo
{
  public JavaBaseFeatureInfo( IFeatureInfo container )
  {
    super( container );
  }

  public JavaBaseFeatureInfo( IType intrType )
  {
    super( intrType );
  }

  protected abstract IJavaAnnotatedElement getAnnotatedElement();

  public List<IAnnotationInfo> getDeclaredAnnotations()
  {
    List<IAnnotationInfo> retValue = new ArrayList<IAnnotationInfo>();
    for( IAnnotationInfo annotation : getAnnotatedElement().getDeclaredAnnotations() )
    {
      retValue.add( annotation );
    }
    return retValue;
  }

  protected abstract boolean isVisibleViaFeatureDescriptor(IScriptabilityModifier constraint);
  protected abstract boolean isHiddenViaFeatureDescriptor();

  public boolean isVisible( IScriptabilityModifier constraint )
  {
    boolean visible;
    if (isScriptableTagPresent()) {
      visible = super.isVisible(constraint);
    } else {
      if (isPublishInGosu() && !isDefaultEnumFeature()) {
        visible = false;
      } else {
        visible = isVisibleViaFeatureDescriptor(constraint);
      }
    }

    return visible || isProxyClassCompiling();
  }

  @SuppressWarnings({"unchecked"})
  private boolean isPublishInGosu() {
    IJavaAnnotatedElement annotatedElement = getAnnotatedElement();
    if( annotatedElement instanceof Member )
    {
      return ((Member)getAnnotatedElement()).getDeclaringClass().isAnnotationPresent( PublishInGosu.class);
    }
    if (annotatedElement instanceof IJavaClassField) {
      return annotatedElement.getEnclosingClass().isAnnotationPresent(PublishInGosu.class);
    }
    if (annotatedElement instanceof IJavaClassMethod) {
      return annotatedElement.getEnclosingClass().isAnnotationPresent(PublishInGosu.class);
    }
    if (annotatedElement instanceof IJavaClassConstructor) {
      return annotatedElement.getEnclosingClass().isAnnotationPresent(PublishInGosu.class);
    }
    if (annotatedElement instanceof IJavaClassInfo) {
      return annotatedElement.isAnnotationPresent(PublishInGosu.class);
    }

    if (annotatedElement == null) {
      return false;
    }
    
    throw new IllegalStateException( "Unexpected type: " + annotatedElement );
  }

  public boolean isHidden()
  {
    boolean bHidden;
    if (isScriptableTagPresent()) {
      bHidden = super.isHidden();
    } else {
      if (isPublishInGosu() && !isDefaultEnumFeature()) {
        bHidden = true;
      } else {
        bHidden = isHiddenViaFeatureDescriptor();
      }
    }

    return bHidden && !isProxyClassCompiling();
  }

  private boolean isProxyClassCompiling()
  {
    IType compilingClass = GosuClassCompilingStack.getCurrentCompilingType();
    if (compilingClass != null && getOwnersType() != null) {
      IType owner = getOwnersType();
      if (compilingClass instanceof IGosuClassInternal && ((IGosuClassInternal) compilingClass).isProxy()) {
        return owner.isAssignableFrom(compilingClass);  // the proxy class should have the extended type in its hierarchy
      }
    }
    return false;
  }

  protected gw.lang.Deprecated makeDeprecated(String reason) {
    Map<String, Object> annotationMap = new HashMap<String, Object>();
    annotationMap.put("value", reason);
    return (Deprecated) AnnotationParser.annotationForMap(Deprecated.class, annotationMap);
  }

  protected abstract boolean isDefaultEnumFeature();

}
