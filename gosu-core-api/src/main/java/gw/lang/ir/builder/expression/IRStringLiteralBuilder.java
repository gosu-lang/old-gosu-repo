package gw.lang.ir.builder.expression;

import gw.lang.UnstableAPI;
import gw.lang.ir.IRExpression;
import gw.lang.ir.builder.IRBuilderContext;
import gw.lang.ir.builder.IRExpressionBuilder;
import gw.lang.ir.expression.IRNumericLiteral;
import gw.lang.ir.expression.IRStringLiteralExpression;

/**
 * NOTE:  This class is currently not a fixed part of the API and may change in future releases.
 *
 *  Copyright 2010 Guidewire Software, Inc.
 */
@UnstableAPI
public class IRStringLiteralBuilder extends IRExpressionBuilder {
  private String _value;

  public IRStringLiteralBuilder(String value) {
    _value = value;
  }

  @Override
  protected IRExpression buildImpl(IRBuilderContext context) {
    return new IRStringLiteralExpression(_value);
  }
}
