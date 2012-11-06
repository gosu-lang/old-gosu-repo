/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.expressions;

import gw.internal.gosu.parser.Expression;
import gw.internal.gosu.parser.GosuClassCompilingStack;
import gw.internal.gosu.parser.MetaType;
import gw.internal.gosu.parser.TypeLord;


import gw.lang.parser.exceptions.ParseWarningForDeprecatedMember;
import gw.lang.parser.expressions.ITypeLiteralExpression;
import gw.lang.reflect.IType;
import gw.lang.reflect.ITypeInfo;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuClass;

/**
 * Represents a Type literal expression as defined in the Gosu grammar.
 *
 * @see gw.lang.parser.IGosuParser
 */
public class TypeLiteral extends Literal implements ITypeLiteralExpression
{
  private static final ThreadLocal<Boolean> _isComputingIsDeprecated  = new ThreadLocal<Boolean>();
  private Expression _packageExpr;
  private boolean _ignoreTypeDeprecation;
  private int[] _relativeTypeLocation;

  public TypeLiteral( IType type, boolean ignoreTypeDeprecation )
  {
    _ignoreTypeDeprecation = ignoreTypeDeprecation;
    setType( type );
  }

  public TypeLiteral( IType type )
  {
    setType( type );
  }

  public TypeLiteral()
  {
  }

  /**
   * This expression is of NamespaceType. It will be either an Identifier or a MemberAccess.
   */
  public Expression getPackageExpression()
  {
    return _packageExpr;
  }
  public void setPackageExpression( Expression packageExpr )
  {
    _packageExpr = packageExpr;
  }

  @Override
  public void setType( IType type )
  {
    if( type instanceof MetaType)
    {
      if( !((MetaType)type).isLiteral() )
      {
        type = MetaType.getLiteral( ((MetaType)type).getType() );
      }

      super.setType( type );
    }
    else
    {
      super.setType( MetaType.getLiteral( type ) );
    }
    
    IType gosuClass = GosuClassCompilingStack.getCurrentCompilingType();

    if (!_ignoreTypeDeprecation &&
            (gosuClass == null || !(gosuClass instanceof IGosuClass) || ((IGosuClass) gosuClass).isCompilingDefinitions())) {
      Boolean isComputing = _isComputingIsDeprecated.get();
      if ((isComputing != Boolean.TRUE) &&
          (getType() != null)) {
        IType intrinsicType = getType().getType();
        if (intrinsicType != null) {
          try {
            _isComputingIsDeprecated.set(Boolean.TRUE);
            ITypeInfo typeInfo = intrinsicType.getTypeInfo();
            if ((typeInfo != null) && (typeInfo.isDeprecated())) {
              //noinspection ThrowableInstanceNeverThrown
              addParseWarning(new ParseWarningForDeprecatedMember(null,
                                                                  intrinsicType.getDisplayName(),
                                                                  intrinsicType.getDisplayName()));
            }
          } finally {
            _isComputingIsDeprecated.set(Boolean.FALSE);
          }
        }
      }
    }
  }

  /**
   * @param types If this is a parameterized type, these are the parameter types.
   */
  public void setParameterTypes( IType[] types )
  {
    if( types.length == 0 )
    {
      return;
    }

    IType parameterizedType = getType().getType().getParameterizedType( types );
    setType( MetaType.getLiteral( parameterizedType ) );
  }

  @Override
  public int getRelativeTypeStart()
  {
    return _relativeTypeLocation == null ? getLocation().getOffset() : _relativeTypeLocation[0];
  }
  @Override
  public int getRelativeTypeEnd()
  {
    return _relativeTypeLocation == null ? getLocation().getExtent() : _relativeTypeLocation[1];
  }
  public void setRelativeTypeLocation( int iTokenStart, int iTokenEnd )
  {
    if( _relativeTypeLocation == null )
    {
      _relativeTypeLocation = new int[2];
    }
    _relativeTypeLocation[0] = iTokenStart;
    _relativeTypeLocation[1] = iTokenEnd;
  }


  @SuppressWarnings({"CloneDoesntCallSuperClone", "CloneDoesntDeclareCloneNotSupportedException"})
  @Override
  public Object clone()
  {
    TypeLiteral clone = new TypeLiteral( getType() );
    clone.setLocation( getLocation() );
    return clone;
  }

  public boolean isCompileTimeConstant()
  {
    return true;
  }

  public IType evaluate()
  {
    return getType().getType();
  }

  @Override
  public String toString()
  {
    return TypeLord.getPureGenericType( getType().getType() ).getName();
  }

  public MetaType getType()
  {
    MetaType type = getTypeImpl();
    if( type != null && TypeSystem.isDeleted( type.getType() ) ) {
      type = (MetaType) TypeSystem.getErrorType().getMetaType();
    }
    return type;
  }

  @Override
  protected MetaType getTypeImpl() {
    return (MetaType)super.getTypeImpl();
  }

}
