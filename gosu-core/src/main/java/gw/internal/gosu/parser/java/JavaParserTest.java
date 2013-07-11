/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.java;

import gw.internal.ext.org.antlr.runtime.ANTLRStringStream;
import gw.internal.ext.org.antlr.runtime.CharStream;
import gw.internal.ext.org.antlr.runtime.RecognitionException;
import gw.internal.ext.org.antlr.runtime.TokenRewriteStream;

public class JavaParserTest {

  public static void main(java.lang.String[] args) throws RecognitionException {
    CharStream cs = new ANTLRStringStream(
        "package gw.internal.gosu.parser.java;\n" +
        "import java.util.List;\n" +
        "public class Foo {\n" +
        "int a = 1_2_3;\n" +
        "  public static void main(String[] args) {}\n" +
        "}"
    );
    JavaLexer lexer = new JavaLexer(cs);
    TokenRewriteStream tokens = new TokenRewriteStream(lexer);
    JavaParser parser = new JavaParser(tokens);
    TreeBuilder treeBuilder = new TreeBuilder();
    parser.setTreeBuilder(treeBuilder);
    parser.compilationUnit();
    dumpJavaAST(treeBuilder.getTree());
  }

  public static void dumpJavaAST(IJavaASTNode tree) {
    System.out.println("------------------------------------");
    dumpJavaAST(tree, "");
  }

  private static void dumpJavaAST(IJavaASTNode tree, String indent) {
    System.out.println(indent + tree);
    for (IJavaASTNode child : tree.getChildren()) {
      dumpJavaAST(child, indent + "  ");
    }
  }

}
