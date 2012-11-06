/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.parser.ILanguageLevel;

public class StandardLanguageLevel implements ILanguageLevel
{
  @Override
  public boolean allowNonLiteralArgsForJavaAnnotations()
  {
    return false;
  }

  @Override
  public boolean allowNumericIteration()
  {
    return false;
  }

  @Override
  public boolean allowGlobalNowSymbol()
  {
    return false;
  }

  @Override
  public boolean allowAllImplicitCoercions()
  {
    return false;
  }

  @Override
  public boolean allowImplicitBigNumbersWithinExpressions()
  {
    return false;
  }

  @Override
  public boolean isStandard()
  {
    return true;
  }

  @Override
  public boolean errorOnStringCoercionInAdditiveRhs()
  {
    return false;
  }

  @Override
  public boolean richNPEsInMathematicalExpressions()
  {
    return false;
  }

  @Override
  public boolean supportsNakedCatchStatements()
  {
    return true;
  }

  @Override
  public boolean allowsFeatureLiterals()
  {
    return true;
  }

  @Override
  public boolean shouldVerifyPackageRelativeImport( String parsedNameSpace, String actualNameSpace )
  {
    return true;
  }

  @Override
  public boolean allowPackageRelativeImports()
  {
    return false;
  }

  @Override
  public boolean supportHistoricalJavaAnnotationConstructors() {
    return true;
  }
}
