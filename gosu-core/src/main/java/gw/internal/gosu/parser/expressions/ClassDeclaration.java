/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.expressions;

import gw.internal.gosu.parser.Expression;
import gw.internal.gosu.parser.IGosuClassInternal;
import gw.internal.gosu.parser.statements.ClassStatement;

import gw.lang.parser.CaseInsensitiveCharSequence;
import gw.lang.parser.statements.IClassDeclaration;

public class ClassDeclaration extends Expression implements IClassDeclaration
{
  private final CharSequence _className;
  private IGosuClassInternal _gsClass;

  public ClassDeclaration( CharSequence className )
  {
    _className = className instanceof String ? ((String)className).intern() : className;
  }

  public CharSequence getClassName()
  {
    return _className;
  }

  @Override
  public String getName()
  {
    return getClassName().toString();
  }

  public Object evaluate()
  {
    return null; // Nothing to do
  }

  @Override
  public String toString()
  {
    return getClassName().toString();
  }

  @Override
  public int getNameOffset( CaseInsensitiveCharSequence identifierName )
  {
    return getLocation().getOffset();
  }
  @Override
  public void setNameOffset( int iOffset, CaseInsensitiveCharSequence identifierName )
  {
    throw new UnsupportedOperationException();
  }

  public String[] getDeclarations() {
    IGosuClassInternal gsClass = getGSClass();
    return new String[] {gsClass.getRelativeName()};
  }

  public boolean declares( CaseInsensitiveCharSequence identifierName )
  {
    IGosuClassInternal gsClass = getGSClass();
    return identifierName != null &&
           (identifierName.equalsIgnoreCase(gsClass.getName()) ||
            identifierName.equalsIgnoreCase(gsClass.getRelativeName()));
  }

  public IGosuClassInternal getGSClass() {
    if (_gsClass == null) {
      if (!(getParent() instanceof ClassStatement)) {
        throw new RuntimeException("Class declaration's parent must be ClassStatement but was " + getParent());
      }
      _gsClass = ((ClassStatement) getParent()).getGosuClass();
    }
    return _gsClass;
  }

}