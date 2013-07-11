/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.lang.parser;

import gw.config.CommonServices;

public interface ILanguageLevel
{
  boolean isStandard();

  static class Util {
    private static Boolean g_standardGosu = null;
    public static void reset() {
      g_standardGosu = null;
    }

    public static boolean STANDARD_GOSU()
    {
      return g_standardGosu == null
             ? g_standardGosu = CommonServices.getEntityAccess().getLanguageLevel().isStandard()
             : g_standardGosu;
    }
  }


  //## todo: These all should be implied by the answer to isStandard() above... better, just stop supporting this crap

  boolean allowNonLiteralArgsForJavaAnnotations();

  boolean allowNumericIteration();

  boolean allowGlobalNowSymbol();

  boolean allowAllImplicitCoercions();

  boolean supportsNakedCatchStatements();

  boolean shouldVerifyPackageRelativeImport( String parsedNameSpace, String actualNameSpace );

  boolean allowPackageRelativeImports();

  boolean supportHistoricalJavaAnnotationConstructors();
}
