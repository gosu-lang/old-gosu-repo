/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.lang.reflect.gs;

import gw.lang.reflect.IDefaultTypeLoader;

public enum ClassType
{
  Enhancement,
  Program,
  Template,
  Eval,
  Class,
  Interface,
  Enum,
  JavaClass,
  Unknown
  ;

  public boolean isJava() {
    return this == JavaClass;
  }

  public boolean isGosu() {
    return
        this == Enhancement ||
        this == Program ||
        this == Template ||
        this == Eval ||
        this == Class ||
        this == Interface ||
        this == Enum;
  }

  public static ClassType getFromFileName(String name) {
    if (name.endsWith( IDefaultTypeLoader.DOT_JAVA_EXTENSION)) {
      return JavaClass;
    }
    if (name.endsWith( GosuClassTypeLoader.GOSU_ENHANCEMENT_FILE_EXT )) {
      return Enhancement;
    }
    if (name.endsWith( GosuClassTypeLoader.GOSU_PROGRAM_FILE_EXT )) {
      return Program;
    }
    if (name.endsWith( GosuClassTypeLoader.GOSU_TEMPLATE_FILE_EXT )) {
      return Template;
    }
    if (name.endsWith( GosuClassTypeLoader.GOSU_CLASS_FILE_EXT) || name.endsWith( ".gr" ) || name.endsWith( ".grs" )) {
      return Class;
    }
    return Unknown;
  }

  public String getExt()
  {
    switch( this )
    {
      case Class:
        return GosuClassTypeLoader.GOSU_CLASS_FILE_EXT;
      case Program:
        return GosuClassTypeLoader.GOSU_PROGRAM_FILE_EXT;
      case Enhancement:
        return GosuClassTypeLoader.GOSU_ENHANCEMENT_FILE_EXT;
      case Template:
        return GosuClassTypeLoader.GOSU_TEMPLATE_FILE_EXT;
      default:
        return "";
    }
  }

}
