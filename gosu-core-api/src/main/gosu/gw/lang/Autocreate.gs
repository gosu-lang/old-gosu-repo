package gw.lang

uses gw.lang.annotation.AnnotationUsage
uses gw.lang.annotation.IInherited

/**
 * Properties that are null and are annotated with @Autocreate will be populated automatically during an assignment
 * of a subproperty. If a block is supplied, the block will be called to create the new object, otherwise
 * the parameterless contructor will be used.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@AnnotationUsage(:target = PropertyTarget, :usageModifier = One)
class Autocreate implements IAnnotation, IInherited, IAutocreate
{
  var _block():Object
  
  construct()
  {
  }
  
  construct(b():Object)
  {
    _block = b
  }
  
  override property get Block() : block():Object 
  {
    return _block
  }
}
