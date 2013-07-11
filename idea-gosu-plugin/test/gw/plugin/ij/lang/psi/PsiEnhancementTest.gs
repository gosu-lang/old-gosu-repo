/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi

class PsiEnhancementTest extends GosuPsiTestCase {

  function testIncremental_EmptyClass()
  {
    configureByText("pkg/TestClass.gs",
      "package pkg\n" +
      "class TestClass {\n" +
      "}")
    assertIncremental_PsiTextEqualsSource(
      "pkg/TestClassEnhancement.gsx",
      "package pkg\n" +
      "enhancement TestClassEnhancement : pkg.TestClass {\n" +
      "}" )
  }

}