/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.lang;

import gw.lang.annotation.AnnotationUsage;
import gw.lang.annotation.UsageModifier;
import gw.lang.annotation.UsageTarget;
import gw.lang.annotation.AnnotationUsages;

@AnnotationUsages({
  @AnnotationUsage(target = UsageTarget.MethodTarget, usageModifier = UsageModifier.Many),
  @AnnotationUsage(target = UsageTarget.ConstructorTarget, usageModifier = UsageModifier.Many)
})
public class Param implements IAnnotation
{
  private String FieldName;
  private String FieldDescription;

  public Param(String FieldName, String FieldDescription )
  {
    this.FieldName = FieldName;
    this.FieldDescription = FieldDescription;
  }

  public String getFieldName() {
    return FieldName;
  }

  public String getFieldDescription() {
    return FieldDescription;
  }

}