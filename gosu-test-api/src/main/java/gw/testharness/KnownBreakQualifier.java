/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.testharness;

import gw.util.Predicate;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotations which have this annotation are used to qualify a KnownBreak. If a test is annotated with a qualifier
 * annotation, then the test is only a KnownBreak if the qualifier's condition is satisfied.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface KnownBreakQualifier {
  Class<? extends Predicate<? super Annotation>> value();
}