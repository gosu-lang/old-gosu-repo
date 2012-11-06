/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.expression;

import gw.internal.gosu.parser.expressions.TypeIsExpression;
import gw.internal.gosu.ir.transform.ExpressionTransformer;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.lang.ir.IRExpression;
import gw.lang.ir.IRSymbol;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;

/**
 */
public class TypeIsTransformer extends AbstractExpressionTransformer<TypeIsExpression>
{
  public static IRExpression compile( TopLevelTransformationContext cc, TypeIsExpression expr )
  {
    TypeIsTransformer gen = new TypeIsTransformer( cc, expr );
    return gen.compile();
  }

  private TypeIsTransformer( TopLevelTransformationContext cc, TypeIsExpression expr )
  {
    super( cc, expr );
  }

  protected IRExpression compile_impl()
  {
    IType lhsType = _expr().getLHS().getType();
    if( lhsType.isPrimitive() )
    {
      // When the lhs is primitive we can determine assignability statically
      return pushConstant( _expr().getRHS().evaluate().isAssignableFrom( lhsType ) );
    }
    else
    {
      //## todo: Consider using the java INSTANCEOF bytecode when it's appropriate.

      // We want to short-circuit to false if the lhs evaluates to null, so we generate code that looks like the following:
      // temp = lhs
      // return (temp == null ? false : rhs.isAssignableFrom(TypeSystem.getFromObject(temp)).booleanValue())
      IRExpression lhs = ExpressionTransformer.compile( _expr().getLHS(), _cc() );
      IRSymbol temp = _cc().makeAndIndexTempSymbol( lhs.getType() );

      IRExpression getTypeCall = callStaticMethod( TypeSystem.class, "getFromObject", new Class[]{Object.class},
              exprList( identifier( temp ) ) );
      IRExpression isAssignableCall = callMethod( IType.class, "isAssignableFrom", new Class[]{IType.class},
              ExpressionTransformer.compile( _expr().getRHS(), _cc() ),
              exprList( getTypeCall ) );

      return buildComposite(
              buildAssignment( temp, lhs ),
              buildNullCheckTernary(
                      identifier( temp ),
                      booleanLiteral( false ),
                      isAssignableCall ) );
    }
  }
}
