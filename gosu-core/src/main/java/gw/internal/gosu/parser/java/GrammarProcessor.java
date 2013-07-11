/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.java;

import gw.internal.ext.org.antlr.runtime.ANTLRFileStream;
import gw.internal.ext.org.antlr.runtime.RecognitionException;
import gw.internal.ext.org.antlr.runtime.TokenRewriteStream;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;

public class GrammarProcessor {
  public static final String LEXER_SECTION = "Lexer Section";

  public static void main(String args[]) throws Exception {
    transform(
        "C:\\depot\\eng\\emerald\\pl\\ready\\active\\studio\\platform\\gosu-core\\src\\gw\\internal\\gosu\\parser\\java\\Java-raw.g",
        "C:\\depot\\eng\\emerald\\pl\\ready\\active\\studio\\platform\\gosu-core\\src\\gw\\internal\\gosu\\parser\\java\\Java.g");
  }

  public static void transform(String inputFile, String outputFile) throws IOException, RecognitionException {
    GrammarProcessorLexer lex = new GrammarProcessorLexer(new ANTLRFileStream(inputFile));
    TokenRewriteStream tokens = new TokenRewriteStream(lex);
    GrammarProcessorParser parser = new GrammarProcessorParser(tokens);
    parser.grammarDef();

    String oldText = readFile(inputFile);
    int index = oldText.indexOf(LEXER_SECTION);
    oldText = oldText.substring(index);

    String newText = tokens.toString();
    index = newText.indexOf(LEXER_SECTION);
    if (index == -1) {
      System.err.println("Lexer section not found");
      return;
    }

    newText = newText.substring(0, index);
    writeFile(outputFile, newText + oldText);
  }

  private static void writeFile(String outputFile, String newText) throws IOException {
    FileWriter writer = new FileWriter(outputFile);
    writer.write(newText);
    writer.close();
  }

  private static String readFile(String inputFile) {
    StringBuilder buf = new StringBuilder();
    try {
      LineNumberReader r = new LineNumberReader(new FileReader(inputFile));
      String line;
      while ((line = r.readLine()) != null) {
        buf.append(line).append('\n');
      }
      r.close();
    } catch (IOException e) {
      throw new RuntimeException("Cannot read input file.", e);
    }
    return buf.toString();
  }

}
