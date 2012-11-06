/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.lang.annotation;

import gw.lang.UnstableAPI;
import gw.lang.reflect.IScriptabilityModifier;
import gw.lang.parser.ScriptabilityModifiers;

@UnstableAPI
public enum ScriptabilityModifier
{
  HIDDEN("hidden", null),
  ALL("all", ScriptabilityModifiers.SCRIPTABLE),
  EXTERNAL("external", ScriptabilityModifiers.SCRIPTABLE_EXTERNAL),
  UI("ui", ScriptabilityModifiers.SCRIPTABLE_UI),
  RULES("rules", ScriptabilityModifiers.SCRIPTABLE_RULES),
  WORKFLOW("workflow", ScriptabilityModifiers.SCRIPTABLE_WORKFLOW),
  WEBSERVICE("webservice", ScriptabilityModifiers.SCRIPTABLE_WEBSERVICE);

  private IScriptabilityModifier _associatedModifier;
  private String _name;

  ScriptabilityModifier(String name, IScriptabilityModifier modifier) {
    _name = name;
    _associatedModifier = modifier;
  }

  public IScriptabilityModifier getModifier() {
    return _associatedModifier;
  }

  public String getName() {
    return _name;
  }

  public boolean isVisible( IScriptabilityModifier constraint) {
    if (constraint == null) {
      return true;
    }

    if (_associatedModifier == null) {
      return false;
    } else {
      return _associatedModifier.satisfiesConstraint(constraint);
    }
  }
}
