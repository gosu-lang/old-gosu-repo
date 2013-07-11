/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.expression;

import gw.internal.gosu.parser.expressions.UnaryNotPlusMinusExpression;
import gw.lang.ir.IRExpression;
import gw.lang.ir.expression.IRNotExpression;
import gw.internal.gosu.ir.transform.ExpressionTransformer;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.IType;
import gw.lang.reflect.java.JavaTypes;

/**
 */
public class UnaryNotPlusMinusExpressionTransformer extends AbstractExpressionTransformer<UnaryNotPlusMinusExpression>
{
  public static IRExpression compile( TopLevelTransformationContext cc, UnaryNotPlusMinusExpression expr )
  {
    UnaryNotPlusMinusExpressionTransformer gen = new UnaryNotPlusMinusExpressionTransformer( cc, expr );
    return gen.compile();
  }

  private UnaryNotPlusMinusExpressionTransformer( TopLevelTransformationContext cc, UnaryNotPlusMinusExpression expr )
  {
    super( cc, expr );
  }

  protected IRExpression compile_impl()
  {
    IRExpression root = ExpressionTransformer.compile( _expr().getExpression(), _cc() );

    if( _expr().isNot() )
    {
      if( _expr().getExpression().getType() != JavaTypes.pBOOLEAN() )
      {
        throw new IllegalStateException(
          "Logical not operator '!' requires boolean operand. Found: " +
          _expr().getExpression().getType() );
      }
    }
    else if( _expr().isBitNot() )
    {
      final IType type = _expr().getExpression().getType();
      if( type != JavaTypes.pINT() && type != JavaTypes.pLONG())
      {
        throw new IllegalStateException(
          "Bitwise not operator '~' requires int or long operand. Found: " +
          _expr().getExpression().getType()  );
      }
    }

    return new IRNotExpression( root );
  }
}
