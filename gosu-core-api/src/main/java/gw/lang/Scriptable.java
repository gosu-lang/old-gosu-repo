/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.lang;

import gw.lang.annotation.ScriptabilityModifier;
import gw.lang.annotation.AnnotationUsage;
import gw.lang.annotation.UsageModifier;
import gw.lang.annotation.UsageTarget;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Inherited
@AnnotationUsage(target = UsageTarget.AllTarget, usageModifier = UsageModifier.Many)
public @interface Scriptable {
  public ScriptabilityModifier[] value() default {ScriptabilityModifier.ALL};
}
