/*
 * Copyright 2013 Guidewire Software, Inc.
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
  public boolean isStandard()
  {
    return true;
  }

  @Override
  public boolean supportsNakedCatchStatements()
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
