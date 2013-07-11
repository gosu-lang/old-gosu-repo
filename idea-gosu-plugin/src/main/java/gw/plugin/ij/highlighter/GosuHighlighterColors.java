/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.plugin.ij.highlighter;

import com.intellij.openapi.editor.HighlighterColors;
import com.intellij.openapi.editor.SyntaxHighlighterColors;
import com.intellij.openapi.editor.colors.CodeInsightColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.markup.EffectType;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.ui.Gray;
import org.jetbrains.annotations.NonNls;

import java.awt.*;

public class GosuHighlighterColors {
  @NonNls
  static final String WORD_ID = "GOSU_WORD";

  @NonNls
  static final String UNRESOLVED_ACCESS_ID = "GOSU_UNRESOLVED_REF";

  public static final TextAttributes WORD_ATTRIBUTES = SyntaxHighlighterColors.KEYWORD.getDefaultAttributes().clone();

  static {
    WORD_ATTRIBUTES.setForegroundColor(Gray._0);
    WORD_ATTRIBUTES.setFontType(Font.PLAIN);
  }

  public static final TextAttributesKey WORD = TextAttributesKey.createTextAttributesKey(WORD_ID, WORD_ATTRIBUTES);

  // Syntactic colors inferred from java
  public static final TextAttributesKey LINE_COMMENT = SyntaxHighlighterColors.LINE_COMMENT;
  public static final TextAttributesKey BLOCK_COMMENT = SyntaxHighlighterColors.JAVA_BLOCK_COMMENT;
  public static final TextAttributesKey DOC_COMMENT_CONTENT = SyntaxHighlighterColors.DOC_COMMENT;
  public static final TextAttributesKey DOC_COMMENT_TAG = SyntaxHighlighterColors.DOC_COMMENT_TAG;
  public static final TextAttributesKey KEYWORD = SyntaxHighlighterColors.KEYWORD;
  public static final TextAttributesKey NUMBER = SyntaxHighlighterColors.NUMBER;
  public static final TextAttributesKey STRING = SyntaxHighlighterColors.STRING;
  public static final TextAttributesKey PARENTHS = SyntaxHighlighterColors.PARENTHS;
  public static final TextAttributesKey BRACKETS = SyntaxHighlighterColors.BRACKETS;
  public static final TextAttributesKey BRACES = SyntaxHighlighterColors.BRACES;
  public static final TextAttributesKey OPERATOR = SyntaxHighlighterColors.OPERATION_SIGN;
  public static final TextAttributesKey COMMA = SyntaxHighlighterColors.COMMA;
  public static final TextAttributesKey SEMICOLON = SyntaxHighlighterColors.JAVA_SEMICOLON;
  public static final TextAttributesKey DOT = SyntaxHighlighterColors.DOT;
  public static final TextAttributesKey BAD_CHARACTER = CodeInsightColors.UNMATCHED_BRACE_ATTRIBUTES;

  // Semantic colors inferred from Java
  public static final TextAttributesKey LOCAL_VARIABLE_ATTRKEY = CodeInsightColors.LOCAL_VARIABLE_ATTRIBUTES;
  public static final TextAttributesKey REASSIGNED_LOCAL_VARIABLE_ATTRKEY = CodeInsightColors.REASSIGNED_LOCAL_VARIABLE_ATTRIBUTES;
  public static final TextAttributesKey REASSIGNED_PARAMETER_ATTRKEY = CodeInsightColors.REASSIGNED_PARAMETER_ATTRIBUTES;
  public static final TextAttributesKey IMPLICIT_ANONYMOUS_PARAMETER_ATTRKEY = CodeInsightColors.IMPLICIT_ANONYMOUS_CLASS_PARAMETER_ATTRIBUTES;
  public static final TextAttributesKey INSTANCE_FIELD_ATTRKEY = CodeInsightColors.INSTANCE_FIELD_ATTRIBUTES;
  public static final TextAttributesKey STATIC_FIELD_ATTRKEY = CodeInsightColors.STATIC_FIELD_ATTRIBUTES;
  public static final TextAttributesKey PARAMETER_NAME_ATTRKEY = CodeInsightColors.TYPE_PARAMETER_NAME_ATTRIBUTES;
  public static final TextAttributesKey PARAMETER_ATTRKEY = CodeInsightColors.PARAMETER_ATTRIBUTES;
  public static final TextAttributesKey CLASS_NAME_ATTRKEY = CodeInsightColors.CLASS_NAME_ATTRIBUTES;
  public static final TextAttributesKey INTERFACE_NAME_ATTRKEY = CodeInsightColors.INTERFACE_NAME_ATTRIBUTES;
  public static final TextAttributesKey ABSTRACT_CLASS_NAME_ATTRKEY = CodeInsightColors.ABSTRACT_CLASS_NAME_ATTRIBUTES;
  public static final TextAttributesKey TYPE_VARIABLE_ATTRKEY = CodeInsightColors.TYPE_PARAMETER_NAME_ATTRIBUTES;
  public static final TextAttributesKey METHOD_CALL_ATTRKEY = CodeInsightColors.METHOD_CALL_ATTRIBUTES;
  public static final TextAttributesKey METHOD_DECLARATION_ATTRKEY = CodeInsightColors.METHOD_DECLARATION_ATTRIBUTES;
  public static final TextAttributesKey STATIC_METHOD_ATTRKEY = CodeInsightColors.STATIC_METHOD_ATTRIBUTES;
  public static final TextAttributesKey CONSTRUCTOR_CALL_ATTRKEY = CodeInsightColors.CONSTRUCTOR_CALL_ATTRIBUTES;
  public static final TextAttributesKey CONSTRUCTOR_DECLARATION_ATTRKEY = CodeInsightColors.CONSTRUCTOR_DECLARATION_ATTRIBUTES;
  public static final TextAttributesKey ANNOTATION_NAME_ATTRKEY = CodeInsightColors.ANNOTATION_NAME_ATTRIBUTES;
  public static final TextAttributesKey ANNOTATION_ATTRIBUTE_NAME_ATTRKEY = CodeInsightColors.ANNOTATION_ATTRIBUTE_NAME_ATTRIBUTES;
  public static final TextAttributesKey ANNOTATION_ATTRIBUTE_VALUE_ATTRKEY = CodeInsightColors.ANNOTATION_ATTRIBUTE_VALUE_ATTRIBUTES;

  public static final TextAttributesKey DEFAULT_ATTRKEY = new TextAttributesKey();
  public static final TextAttributesKey ENUM_NAME_ATTRKEY = CLASS_NAME_ATTRKEY;
  public static final TextAttributesKey PACKAGE_QUALIFIER_ATTRKEY = CLASS_NAME_ATTRKEY;
  public static final TextAttributesKey UNHANDLED_ATTRKEY = CodeInsightColors.PARAMETER_ATTRIBUTES;


  public static final TextAttributes UNRESOLVED_ACCESS_ATTRIBUTES = HighlighterColors.TEXT.getDefaultAttributes().clone();

  static {
    UNRESOLVED_ACCESS_ATTRIBUTES.setForegroundColor(Color.BLACK);
    UNRESOLVED_ACCESS_ATTRIBUTES.setEffectColor(Color.GRAY);
    UNRESOLVED_ACCESS_ATTRIBUTES.setEffectType(EffectType.LINE_UNDERSCORE);
  }

  public static final TextAttributesKey UNRESOLVED_ACCESS = TextAttributesKey.createTextAttributesKey(UNRESOLVED_ACCESS_ID, UNRESOLVED_ACCESS_ATTRIBUTES);

  private GosuHighlighterColors() {
  }
}