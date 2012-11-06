/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.lang.parser;

import gw.config.CommonServices;

import java.util.HashMap;
import java.util.Map;

public class Keyword implements CharSequence
{
  //
  // Key/Reserved Words
  //
  static final Map<String, Keyword> RESERVED_WORDS = new HashMap<String, Keyword>( 64 );

  public static final Keyword KW_true = addReservedWord( "true", true );
  public static final Keyword KW_false = addReservedWord( "false", true );
  public static final Keyword KW_NaN = addReservedWord( "NaN", true );
  public static final Keyword KW_Infinity = addReservedWord( "Infinity", true );
  public static final Keyword KW_and = addReservedWord( "and" );
  public static final Keyword KW_or = addReservedWord( "or" );
  public static final Keyword KW_not = addReservedWord( "not" );
  public static final Keyword KW_null = addReservedWord( "null", true );
  public static final Keyword KW_length = addReservedWord( "length", true );
  public static final Keyword KW_exists = addReservedWord( "exists", true );
  public static final Keyword KW_in = addReservedWord( "in" );
  public static final Keyword KW_startswith = addReservedWord( "startswith", true );
  public static final Keyword KW_contains = addReservedWord( "contains", true );
  public static final Keyword KW_where = addReservedWord( "where", true );
  public static final Keyword KW_find = addReservedWord( "find", true );
  public static final Keyword KW_var = addReservedWord( "var" );
  public static final Keyword KW_delegate = addReservedWord( "delegate" );
  public static final Keyword KW_represents = addReservedWord( "represents" );
  public static final Keyword KW_as = addReservedWord( "as", true );
  public static final Keyword KW_typeof = addReservedWord( "typeof" );
  public static final Keyword KW_statictypeof = addReservedWord( "statictypeof" );
  public static final Keyword KW_typeis = addReservedWord( "typeis" );
  public static final Keyword KW_typeas = addReservedWord( "typeas" );
  public static final Keyword KW_package = addReservedWord( "package" );
  public static final Keyword KW_uses = addReservedWord( "uses" );
  public static final Keyword KW_if = addReservedWord( "if" );
  public static final Keyword KW_else = addReservedWord( "else" );
  public static final Keyword KW_except = addReservedWord( "except", true );
  public static final Keyword KW_unless = addReservedWord( "unless" );
  public static final Keyword KW_foreach = addReservedWord( "foreach" );
  public static final Keyword KW_for = addReservedWord( "for" );
  public static final Keyword KW_index = addReservedWord( "index", true );
  public static final Keyword KW_iterator = addReservedWord( "iterator", true, false );
  public static final Keyword KW_while = addReservedWord( "while" );
  public static final Keyword KW_do = addReservedWord( "do" );
  public static final Keyword KW_continue = addReservedWord( "continue" );
  public static final Keyword KW_break = addReservedWord( "break" );
  public static final Keyword KW_return = addReservedWord( "return" );
  public static final Keyword KW_construct = addReservedWord( "construct" );
  public static final Keyword KW_function = addReservedWord( "function" );
  public static final Keyword KW_property = addReservedWord( "property", false, false );
  public static final Keyword KW_get = addReservedWord( "get", true );
  public static final Keyword KW_set = addReservedWord( "set", true );
  public static final Keyword KW_try = addReservedWord( "try" );
  public static final Keyword KW_catch = addReservedWord( "catch" );
  public static final Keyword KW_finally = addReservedWord( "finally" );
  public static final Keyword KW_this = addReservedWord( "this", true );
  public static final Keyword KW_throw = addReservedWord( "throw" );
  public static final Keyword KW_new = addReservedWord( "new" );
  public static final Keyword KW_switch = addReservedWord( "switch" );
  public static final Keyword KW_case = addReservedWord( "case" );
  public static final Keyword KW_default = addReservedWord( "default" );
  public static final Keyword KW_eval = addReservedWord( "eval" );
  public static final Keyword KW_private = addReservedWord( "private", true );
  public static final Keyword KW_internal = addReservedWord( "internal", true );
  public static final Keyword KW_protected = addReservedWord( "protected", true );
  public static final Keyword KW_public = addReservedWord( "public", true );
  public static final Keyword KW_abstract = addReservedWord( "abstract", true );
  public static final Keyword KW_override = addReservedWord( "override" );
  public static final Keyword KW_hide = addReservedWord( "hide", true );
  public static final Keyword KW_final = addReservedWord( "final", true );
  public static final Keyword KW_static = addReservedWord( "static", true );
  public static final Keyword KW_extends = addReservedWord( "extends" );
  public static final Keyword KW_transient = addReservedWord( "transient", false, false );
  public static final Keyword KW_implements = addReservedWord( "implements" );
  public static final Keyword KW_readonly = addReservedWord( "readonly", true );
  public static final Keyword KW_class = addReservedWord( "class", false, false );
  public static final Keyword KW_interface = addReservedWord( "interface", false, false );
  public static final Keyword KW_enum = addReservedWord( "enum", false, false );
  public static final Keyword KW_super = addReservedWord( "super", true );
  public static final Keyword KW_outer = addReservedWord( "outer", true );
  public static final Keyword KW_execution = addReservedWord( "execution", true );
  public static final Keyword KW_request = addReservedWord( "request", true );
  public static final Keyword KW_session = addReservedWord( "session", true );
  public static final Keyword KW_application = addReservedWord( "application", true );
  public static final Keyword KW_void = addReservedWord( "void", true );
  public static final Keyword KW_block = addReservedWord( "block", true );
  public static final Keyword KW_enhancement = addReservedWord( "enhancement", true );
  public static final Keyword KW_classpath = addReservedWord( "classpath", true );
  public static final Keyword KW_typeloader = addReservedWord( "typeloader", true );
  public static final Keyword KW_using = addReservedWord( "using" );
  //public static final Keyword KW_type = addReservedWord( "Type", true );

  private CaseInsensitiveCharSequence _strCICS;
  private String _strName;
  private boolean _bValue;

  private Keyword( String strWord, boolean bValue )
  {
    _strCICS = CaseInsensitiveCharSequence.get(strWord);
    _strName = strWord;
    _bValue = bValue;
  }

  private static Keyword addReservedWord( String strWord )
  {
    return addReservedWord( strWord, false );
  }
  private static Keyword addReservedWord( String strWord, boolean bValue )
  {
    return addReservedWord( strWord, bValue, true );
  }
  private static Keyword addReservedWord( String strWord, boolean bValue, boolean bCrappyMixedCase )
  {
    String strLower = strWord.toLowerCase();
    if( RESERVED_WORDS.containsKey( strLower ) )
    {
      throw new RuntimeException( strWord + " is already defined as a reserved word." );
    }
    Keyword keyword = new Keyword( strWord, bValue );
    RESERVED_WORDS.put( strWord, keyword );
    if( bCrappyMixedCase && !CommonServices.getEntityAccess().getLanguageLevel().isStandard() )
    {
      RESERVED_WORDS.put( strLower, keyword );
      RESERVED_WORDS.put( strLower.toUpperCase(), keyword );
      RESERVED_WORDS.put( Character.toUpperCase( strLower.charAt( 0 ) ) + strLower.substring( 1 ).toLowerCase(), keyword );
    }
    return keyword;
  }

  public static boolean isReserved( String strWord )
  {
    return strWord != null && RESERVED_WORDS.containsKey( strWord );
  }

  public static boolean isReservedValue( String strWord )
  {
    Keyword keyword = RESERVED_WORDS.get( strWord );
    return keyword != null && keyword.isValue();
  }

  public int length()
  {
    return _strCICS.length();
  }

  public char charAt( int index )
  {
    return _strCICS.charAt( index );
  }

  public CharSequence subSequence( int start, int end )
  {
    return _strCICS.subSequence( start, end );
  }

  public boolean isValue()
  {
    return _bValue;
  }

  public CaseInsensitiveCharSequence getCICS() {
    return _strCICS;
  }

  public String toString()
  {
    return _strCICS.toString();
  }

  public boolean equals( Object o )
  {
    if( this == o )
    {
      return true;
    }
    if( o == null || getClass() != o.getClass() )
    {
      return false;
    }
    Keyword keyword = (Keyword)o;
    return _strCICS.equals( keyword._strCICS );
  }

  public int hashCode()
  {
    return _strCICS.hashCode();
  }

  public boolean equals( String str )
  {
    return _strCICS.equalsIgnoreCase( str );
  }

  public boolean equalsIgnoreCase( String str )
  {
    return _strCICS.equalsIgnoreCase( str );
  }

  public static boolean isExactKeywordMatch( String strValue )
  {
    Keyword keyword = RESERVED_WORDS.get( strValue );
    return keyword.getName().equals( strValue );
  }

  public String getName()
  {
    return _strName;
  }
}
