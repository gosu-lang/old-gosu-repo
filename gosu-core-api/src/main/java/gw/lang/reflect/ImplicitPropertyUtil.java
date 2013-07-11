/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.lang.reflect;

public class ImplicitPropertyUtil {
  public static final String IS = "is";
  public static final String GET = "get";
  public static final String SET = "set";

  public static class ImplicitPropertyInfo {
    private boolean _isSetter;
    private boolean _isGetter;
    private String _name;

    public ImplicitPropertyInfo(boolean isSetter, boolean isGetter, String name) {
      _isSetter = isSetter;
      _isGetter = isGetter;
      _name = name;
    }

    public boolean isSetter() {
      return _isSetter;
    }

    public boolean isGetter() {
      return _isGetter;
    }

    public String getName() {
      return _name;
    }
  }

  public static String capitalizeFirstChar(String name) {
    if (name == null || name.length() == 0) {
      return name;
    }
    if (name.startsWith("_")) {
      return capitalizeFirstChar(name.substring(1));
    }
    char chars[] = name.toCharArray();
    chars[0] = Character.toUpperCase(chars[0]);
    return new String(chars);
  }

  public static boolean isPropertyName(String name) {
    return name.startsWith(IS) || name.startsWith(GET) || name.startsWith(SET);
  }

  public static String getPropertyName(String name) {
    if (name.startsWith(IS)) {
      return name.substring(IS.length());
    } else if (name.startsWith(GET)) {
      return name.substring(GET.length());
    } else if (name.startsWith(SET)) {
      return name.substring(SET.length());
    }
    return null;
  }
}
