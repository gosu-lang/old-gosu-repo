/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.expressions;

import gw.internal.gosu.parser.Expression;
import gw.lang.reflect.IType;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.parser.expressions.ITypeOfExpression;
import gw.internal.gosu.parser.MetaType;
import gw.internal.gosu.parser.CannotExecuteGosuException;


/**
 * Represents a TypeOf expression as defined in the Gosu grammar.
 *
 * @see gw.lang.parser.IGosuParser
 */
public final class TypeOfExpression extends Expression implements ITypeOfExpression
{
  private Expression _expression;

  public TypeOfExpression()
  {
    super();
  }

  @Override
  public IType getTypeImpl()
  {
    // Note the static type of typeof must always be IType, not the metatype of the expression.
    // This is because the metatype of the expression may be incompatible with the runtime type
    // of the expression e.g,.:
    // var a = new Foo() // Foo is a Gosu class
    // var o = a as Object // Object is a Java clas
    // (typeof o). <--- the compile time type here must be just IType because it is the least upper bound of all meta types 
    return _expression == null ? MetaType.get( null ) : JavaTypes.ITYPE();
  }

  public Expression getExpression()
  {
    return _expression;
  }

  public void setExpression( Expression e )
  {
    _expression = e;
    setType( MetaType.get( e.getType() ) );
  }

  public Object evaluate()
  {
    if( !isCompileTimeConstant() )
    {
      return super.evaluate();
    }

    throw new CannotExecuteGosuException();
  }

  @Override
  public String toString()
  {
    return "typeof " + getExpression().toString();
  }

}
