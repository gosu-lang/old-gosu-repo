/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.expression;

import gw.internal.gosu.parser.expressions.UnaryExpression;
import gw.internal.gosu.parser.expressions.UnsupportedNumberTypeException;
import gw.internal.gosu.parser.BeanAccess;
import gw.internal.gosu.ir.transform.ExpressionTransformer;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.lang.ir.IRExpression;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.IDimension;
import gw.config.CommonServices;

/**
 */
public class UnaryExpressionTransformer extends AbstractExpressionTransformer<UnaryExpression>
{
  public static IRExpression compile( TopLevelTransformationContext cc, UnaryExpression expr )
  {
    UnaryExpressionTransformer gen = new UnaryExpressionTransformer( cc, expr );
    return gen.compile();
  }

  private UnaryExpressionTransformer( TopLevelTransformationContext cc, UnaryExpression expr )
  {
    super( cc, expr );
  }

  protected IRExpression compile_impl()
  {
    IRExpression root = ExpressionTransformer.compile( _expr().getExpression(), _cc() );

    if( _expr().getType().isPrimitive() && BeanAccess.isNumericType( _expr().getType() ) )
    {
      return negateSimple( root );
    }
    else
    {
      return negateComplex(root );
    }
  }

  private IRExpression negateSimple( IRExpression root )
  {
    if (_expr().isNegated()) {
      return buildNegation( root );
    } else {
      // Nothing to do if it's not a negation
      return root;
    }
  }

  private IRExpression negateComplex( IRExpression root )
  {
    // Call into Gosu's runtime for the answer
    IRExpression negateCall = callStaticMethod( getClass(), "negateComplex", new Class[]{Object.class, IType.class, boolean.class},
            exprList( boxValue( _expr().getExpression().getType(), root ),
                      pushType( _expr().getType() ),
                      pushConstant( _expr().isNegated() ) ) );

    // Ensure value is unboxed if type is primitive
    return unboxValueToType( _expr().getType(), negateCall );
  }

  public static Object negateComplex( Object value, IType type, boolean bNegated )
  {
    if( value == null )
    {
      return null;
    }

    if( bNegated )
    {
      IDimension dimension = null;
      if( JavaTypes.IDIMENSION().isAssignableFrom( type ) )
      {
        dimension = (IDimension)value;
        type = TypeSystem.get( ((IDimension)value).numberType() );
        value = ((IDimension)value).toNumber();
      }

      if( type == JavaTypes.BIG_DECIMAL() )
      {
        value = CommonServices.getCoercionManager().makeBigDecimalFrom( value ).negate();
      }
      else if( type == JavaTypes.BIG_INTEGER() )
      {
        value = CommonServices.getCoercionManager().makeBigIntegerFrom( value ).negate();
      }
      else if( type == JavaTypes.INTEGER() || type == JavaTypes.pINT() )
      {
        value = -CommonServices.getCoercionManager().makeIntegerFrom( value );
      }
      else if( type == JavaTypes.LONG() || type == JavaTypes.pLONG() )
      {
        value = -CommonServices.getCoercionManager().makeLongFrom( value );
      }
      else if( type == JavaTypes.DOUBLE() || type == JavaTypes.pDOUBLE() )
      {
        value = -CommonServices.getCoercionManager().makeDoubleFrom( value );
      }
      else if( type == JavaTypes.FLOAT() || type == JavaTypes.pFLOAT() )
      {
        value = -CommonServices.getCoercionManager().makeFloatFrom( value );
      }
      else if( type == JavaTypes.SHORT() || type == JavaTypes.pSHORT() )
      {
        value = (short)-CommonServices.getCoercionManager().makeIntegerFrom( value ).shortValue();
      }
      else if( type == JavaTypes.BYTE() || type == JavaTypes.pBYTE() )
      {
        value = (byte)-CommonServices.getCoercionManager().makeIntegerFrom( value ).byteValue();
      }
      else
      {
        throw new UnsupportedNumberTypeException( type );
      }

      if( dimension != null )
      {
        //noinspection unchecked
        value = dimension.fromNumber( (Number)value );
      }
    }
    return value;
  }

}
