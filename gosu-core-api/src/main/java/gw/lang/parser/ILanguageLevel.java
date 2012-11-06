/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.lang.parser;

import gw.config.CommonServices;

public interface ILanguageLevel
{
  boolean allowNonLiteralArgsForJavaAnnotations();

  boolean allowNumericIteration();

  boolean allowGlobalNowSymbol();

  boolean allowAllImplicitCoercions();

  boolean allowImplicitBigNumbersWithinExpressions();

  boolean isStandard();

  boolean errorOnStringCoercionInAdditiveRhs();

  boolean richNPEsInMathematicalExpressions();

  boolean supportsNakedCatchStatements();

  boolean allowsFeatureLiterals();

  boolean shouldVerifyPackageRelativeImport( String parsedNameSpace, String actualNameSpace );

  boolean allowPackageRelativeImports();

  boolean supportHistoricalJavaAnnotationConstructors();

  static class Util {
    private static Boolean g_standardGosu = null;
    public static void reset() {
      g_standardGosu = null;
    }
    public static boolean SUPPORT_NAMED_ARGS()
    {
      return STANDARD_GOSU() || true;
    }
    public static boolean STANDARD_GOSU()
    {
      return g_standardGosu == null
             ? g_standardGosu = CommonServices.getEntityAccess().getLanguageLevel().isStandard()
             : g_standardGosu;
    }
    public static boolean equalsIgnoreCase( String s1, String s2 ) {
      return STANDARD_GOSU()
             ? s1.equals( s2 )
             : s1.equalsIgnoreCase( s2 );
    }
  }
}
