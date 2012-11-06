/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.internal.gosu.ir.transform.expression;

import gw.internal.gosu.parser.expressions.QueryExpression;
import gw.internal.gosu.ir.transform.TopLevelTransformationContext;
import gw.lang.ir.IRExpression;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.parser.EvaluationException;
import gw.lang.parser.expressions.IQueryExpressionEvaluator;
import gw.lang.parser.statements.IFunctionStatement;
import gw.config.CommonServices;

import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

/**
 */
public class QueryExpressionTransformer extends EvalBasedTransformer<QueryExpression>
{
  public static final Map<String, QueryExpression> QUERY_EXPRESSIONS = Collections.synchronizedMap( new HashMap<String, QueryExpression>() );
  private static final Class[] PARAM_TYPES = new Class[]{Object.class, Object[].class, IType[].class, IType.class, String.class};

  public static IRExpression compile( TopLevelTransformationContext cc, QueryExpression expr )
  {
    QueryExpressionTransformer compiler = new QueryExpressionTransformer( cc, expr );
    return compiler.compile();
  }

  private QueryExpressionTransformer( TopLevelTransformationContext cc, QueryExpression expr )
  {
    super( cc, expr );
  }

  protected IRExpression compile_impl()
  {
    String iQueryExprId;
    synchronized( QUERY_EXPRESSIONS )
    {
      iQueryExprId = makeId( _expr() );
      QUERY_EXPRESSIONS.put( iQueryExprId, _expr() );
    }

    IFunctionStatement fs = _expr().getLocation().getEnclosingFunctionStatement();
    IRExpression compileAndRunEvalSource = callStaticMethod( QueryExpressionTransformer.class, "compileAndRunQuery",
                                                             PARAM_TYPES,
                                                             exprList(
                                                               pushEnclosingContext(),
                                                               pushCapturedSymbols( getGosuClass(), _expr().getCapturedForBytecode() ),
                                                               pushEnclosingFunctionTypeParamsInArray( _expr() ),
                                                               pushType( getGosuClass() ),
                                                               pushConstant( iQueryExprId )
                                                             ) );
    return checkCast( TypeSystem.getByFullName( "gw.api.database.IQueryBeanResult", TypeSystem.getGlobalModule() ), compileAndRunEvalSource );
  }

  private String makeId( QueryExpression queryExpression )
  {
    return queryExpression.getLocation().getScriptPartId().getContainingType().getName() + ":" + queryExpression.getLocation().getOffset();
  }

  public static Object compileAndRunQuery( Object outer, Object[] capturedValues,
                                           IType[] immediateFuncTypeParams, IType enclosingClass, String iQueryExprId )
  {
    QueryExpression queryExpr = QUERY_EXPRESSIONS.get( iQueryExprId );

    IQueryExpressionEvaluator evaluator = CommonServices.getEntityAccess().getQueryExpressionEvaluator( queryExpr );
    if( evaluator != null )
    {
      return evaluator.evaluate( new Object[] {outer, capturedValues, immediateFuncTypeParams, enclosingClass} );
    }

    throw new EvaluationException( "No query expression evaluator is defined." );
  }

  public static void clearQueryExpressions() {
    QUERY_EXPRESSIONS.clear();
  }
}
