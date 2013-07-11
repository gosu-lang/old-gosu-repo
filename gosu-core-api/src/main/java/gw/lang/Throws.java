/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.lang;

import gw.lang.reflect.IType;

import gw.lang.annotation.UsageTarget;
import gw.lang.annotation.UsageModifier;
import gw.lang.annotation.AnnotationUsage;
import gw.lang.annotation.AnnotationUsages;

@AnnotationUsages({
  @AnnotationUsage(target = UsageTarget.MethodTarget, usageModifier = UsageModifier.Many),
  @AnnotationUsage(target = UsageTarget.PropertyTarget, usageModifier = UsageModifier.Many),
  @AnnotationUsage(target = UsageTarget.ConstructorTarget, usageModifier = UsageModifier.Many)
})
public class Throws implements IAnnotation
{
  private IType ExceptionType;
  private String Description;

  /**
   *
   * @param ExceptionType the type of the exception
   * @param Description the description of why the exception can be thrown
   */
  public Throws(IType ExceptionType, String Description )
  {
    this.ExceptionType = ExceptionType;
    this.Description = Description;
  }

  public IType getExceptionType() {
    return ExceptionType;
  }

  public String getExceptionDescription() {
    return Description;
  }
}
