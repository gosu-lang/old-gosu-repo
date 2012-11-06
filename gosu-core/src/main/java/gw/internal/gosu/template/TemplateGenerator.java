/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.internal.gosu.template;

import gw.config.CommonServices;
import gw.lang.parser.GosuParserTypes;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.parser.TypelessScriptPartId;
import gw.internal.gosu.parser.expressions.Identifier;
import gw.internal.gosu.parser.expressions.Program;
import gw.internal.gosu.parser.expressions.BlockExpression;
import gw.internal.gosu.parser.*;
import gw.lang.parser.expressions.IProgram;
import gw.lang.parser.template.IEscapesAllContent;
import gw.lang.reflect.FunctionType;
import gw.lang.reflect.TypeSystem;
import gw.lang.parser.GosuParserFactory;
import gw.lang.parser.IFileContext;
import gw.lang.parser.IGosuParser;
import gw.lang.parser.ISymbol;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.ScriptabilityModifiers;
import gw.lang.parser.IParseTree;
import gw.lang.parser.CaseInsensitiveCharSequence;
import gw.lang.parser.ITypeUsesMap;
import gw.lang.parser.IExpression;
import gw.lang.parser.IFunctionSymbol;
import gw.lang.parser.Keyword;
import gw.lang.parser.IScriptPartId;
import gw.lang.parser.ScriptPartId;
import gw.lang.parser.ExternalSymbolMapForMap;
import gw.lang.parser.resources.Res;
import gw.lang.parser.template.ITemplateGenerator;
import gw.lang.parser.template.TemplateParseException;
import gw.lang.parser.template.StringEscaper;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystemShutdownListener;
import gw.lang.reflect.gs.GosuClassTypeLoader;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.gs.IExternalSymbolMap;
import gw.lang.reflect.gs.IGosuEnhancement;
import gw.lang.reflect.gs.ITemplateType;
import gw.util.GosuClassUtil;
import gw.util.Stack;
import gw.util.GosuEscapeUtil;
import gw.util.StreamUtil;
import gw.util.GosuStringUtil;
import gw.util.CaseInsensitiveHashMap;
import gw.util.concurrent.LockingLazyVar;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.Collections;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Collection;
import java.util.Map.Entry;

/**
 * A template generator employing Gosu.
 * <p/>
 * Works much like JSP -- uses <% script %> for scriptlets and <%= expr %> for expressions.
 * Also supports JSP comments like this: <%-- comment here --%>
 * <p/>
 * Templates can be any type e.g., XML, HTML, text, whatever.
 */
public class TemplateGenerator implements ITemplateGenerator
{
  public static final String GS_TEMPLATE = "GsTemplate";
  public static final String GS_TEMPLATE_PARSED = "GsTemplateParsed";
  public static final String SCRIPTLET_BEGIN = "<%";
  public static final String SCRIPTLET_END = "%>";
  public static final char EXPRESSION_SUFFIX = '=';
  public static final char DECLARATION_SUFFIX = '!';
  public static final char DIRECTIVE_SUFFIX = '@';
  public static final String COMMENT_BEGIN = "<%--";
  public static final String COMMENT_END = "--%>";

  public static final String ALTERNATE_EXPRESSION_BEGIN = "${";
  public static final String ALTERNATE_EXPRESSION_END = "}";

  public static final char ESCAPED_SCRIPTLET_MARKER = '\uffe0';
  public static final char ESCAPED_SCRIPTLET_BEGIN_CHAR = '\uffe1';
  public static final char ESCAPED_ALTERNATE_EXPRESSION_BEGIN_CHAR = '\uffe2';

  public static final int SCRIPTLET_BEGIN_LEN = SCRIPTLET_BEGIN.length();
  public static final int SCRIPTLET_END_LEN = SCRIPTLET_END.length();
  public static final int COMMENT_BEGIN_LEN = COMMENT_BEGIN.length();
  public static final int COMMENT_END_LEN = COMMENT_END.length();
  public static final int ALTERNATE_EXPRESSION_BEGIN_LEN = ALTERNATE_EXPRESSION_BEGIN.length();
  public static final int ALTERNATE_EXPRESSION_END_LEN = ALTERNATE_EXPRESSION_END.length();

  public static final LockingLazyVar<ISymbol> PRINT_CONTENT_SYMBOL = new LockingLazyVar<ISymbol>() {
    protected ISymbol init() {
      try {
        return new Symbol( PRINT_METHOD,
                           new FunctionType( PRINT_METHOD, GosuParserTypes.NULL_TYPE(), new IType[]{GosuParserTypes.STRING_TYPE(), GosuParserTypes.BOOLEAN_TYPE()} ),
                           TemplateGenerator.class.getMethod( PRINT_METHOD, String.class, Boolean.TYPE ) );
      } catch (NoSuchMethodException e) {
        throw new RuntimeException( e );
      }
    }
  };
  static
  {
    TypeSystem.addShutdownListener(new TypeSystemShutdownListener() {
      public void shutdown() {
        PRINT_CONTENT_SYMBOL.clear();
      }
    });
  }
    
  private static ThreadLocal<Stack<WriterEscaperPair>> g_writerEscaperPair = new ThreadLocal<Stack<WriterEscaperPair>>();

  private String _fqn;
  private String _scriptStr;
  private List<ISymbol> _params = new ArrayList<ISymbol>();

  private Program _program;
  private ISymbolTable _compileTimeSymbolTable;
  private IType _supertype;
  private boolean _useStudioEditorParser;
  private boolean _disableAlternative;
  private boolean _hasOwnSymbolScope;
  private ContextInferenceManager _ctxInferenceMgr;

  /**
   * Generates a template of any format having embedded Gosu.
   *
   * @param readerTemplate The source of the template.
   * @param writerOut      Where the output should go.
   * @param symTable         The symbol table to use.
   * @throws TemplateParseException on execution exception
   */
  public static void generateTemplate( Reader readerTemplate, Writer writerOut, ISymbolTable symTable)
    throws TemplateParseException
  {
    generateTemplate(readerTemplate, writerOut, symTable, false);
  }

  /**
   * Generates a template of any format having embedded Gosu.
   *
   * @param readerTemplate The source of the template.
   * @param writerOut      Where the output should go.
   * @param symTable         The symbol table to use.
   * @param strict  whether to allow althernative template
   * @throws TemplateParseException on execution exception
   */
  public static void generateTemplate( Reader readerTemplate, Writer writerOut, ISymbolTable symTable, boolean strict)
      throws TemplateParseException {
    TemplateGenerator te = new TemplateGenerator( readerTemplate);
    te.setDisableAlternative(strict);
    te.execute( writerOut, symTable);
  }

  public static TemplateGenerator getTemplate( Reader readerTemplate )
  {
    return new TemplateGenerator( readerTemplate );
  }

  public static TemplateGenerator getTemplate(Reader readerTemplate, String fullyQualifiedName) {
    TemplateGenerator template = getTemplate(readerTemplate);
    template.setFqn(fullyQualifiedName);
    template.setHasOwnSymbolScope(true);
    return template;
  }

  private void setHasOwnSymbolScope(boolean hasSymbolScope) {
    _hasOwnSymbolScope = hasSymbolScope;
  }

  public Program getProgram()
  {
    return _program;
  }

  private static WriterEscaperPair getWriterEscaperPair()
  {
    Stack stack = g_writerEscaperPair.get();
    if( stack != null && stack.size() > 0 )
    {
      return (WriterEscaperPair)stack.peek();
    }
    return null;
  }

  private static void pushWriterEscaperPair( WriterEscaperPair pair )
  {
    Stack<WriterEscaperPair> stack = g_writerEscaperPair.get();
    if( stack == null )
    {
      g_writerEscaperPair.set( stack = new Stack<WriterEscaperPair>() );
    }
    stack.push( pair );
  }

  private static void popWriter()
  {
    Stack<WriterEscaperPair> stack = g_writerEscaperPair.get();
    stack.pop();
  }

  /**
   * WARNING:  This will consume the reader and close it!
   * @param reader the reader containing the template
   */
  private TemplateGenerator( Reader reader )
  {
    try {
      _scriptStr = StreamUtil.getContent(reader);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * @throws TemplateParseException
   */
  public void execute( Writer writer, ISymbolTable symbolTable) throws TemplateParseException {
    execute(writer, null, symbolTable);
  }

  /**
   * @throws TemplateParseException
   */
  public void execute( Writer writer, StringEscaper escaper, ISymbolTable symTable) throws TemplateParseException
  {
    symTable.pushScope();
    String strCompiledSource = null;
    try
    {
      symTable.putSymbol( PRINT_CONTENT_SYMBOL.get() );
      if(_supertype != null) {
        symTable.putSymbol(new Symbol("this", _supertype, null));
      }
      pushWriterEscaperPair( new WriterEscaperPair(writer, escaper) );
      try
      {
        synchronized (this) {
          if( _program == null )
          {
            List<TemplateParseException> exceptions = new ArrayList<TemplateParseException>();
            strCompiledSource = transformTemplate(_scriptStr, exceptions);
            if (!exceptions.isEmpty()) {
              throw exceptions.get(0);
            }
            _program = compile( strCompiledSource, symTable, new HashMap<CaseInsensitiveCharSequence, Set<IFunctionSymbol>>(), null, null, null );
            if( _fqn == null )
            {
              _program.getGosuProgram().setThrowaway( true );
            }
            _compileTimeSymbolTable = symTable.copy();
          }
        }
        _program.evaluate(extractExternalSymbols( _compileTimeSymbolTable, symTable ));
      }
      finally
      {
        popWriter();
      }
    }
    catch( ParseResultsException e )
    {
      throw new TemplateParseException( e, strCompiledSource );
    }
    finally
    {
      symTable.popScope();
    }
  }

  public void compile(ISymbolTable symTable) throws TemplateParseException
  {
    compile(symTable, new HashMap<CaseInsensitiveCharSequence, Set<IFunctionSymbol>>(), null, null, _ctxInferenceMgr);
  }

  public void compile(ISymbolTable symTable, Map<CaseInsensitiveCharSequence, Set<IFunctionSymbol>> dfsDeclByName, ITypeUsesMap typeUsesMap, Stack<BlockExpression> blocks, ContextInferenceManager ctxInferenceMgr) throws TemplateParseException {
    symTable.pushScope();
    if (ctxInferenceMgr != null) {
      ctxInferenceMgr.suspendRefCollection();
    }
    String strCompiledSource = null;
    try
    {
      symTable.putSymbol( PRINT_CONTENT_SYMBOL.get() );

      synchronized (this) {
        if( _program == null )
        {
          List<TemplateParseException> exceptions = new ArrayList<TemplateParseException>();
          strCompiledSource = transformTemplate(_scriptStr, exceptions);
          if (!exceptions.isEmpty()) {
            throw exceptions.get(0);
          }
          for (ISymbol param : _params) {
            Symbol s = new Symbol( param.getName(), param.getType(), null );
            symTable.putSymbol(s);
          }
          _program = compile( strCompiledSource, symTable, dfsDeclByName, typeUsesMap, blocks, ctxInferenceMgr );
          _compileTimeSymbolTable = symTable.copy();
        }
      }

    }
    catch( ParseResultsException e )
    {
      throw new TemplateParseException( e, strCompiledSource );
    }
    finally
    {
      symTable.popScope();
      if (ctxInferenceMgr != null) {
        ctxInferenceMgr.resumeRefCollection();
      }
    }
  }

  /**
   * @return the program to execute
   * @throws gw.lang.parser.exceptions.ParseException
   *
   */
  private Program compile(String strCompiledSource, ISymbolTable symbolTable, Map<CaseInsensitiveCharSequence, Set<IFunctionSymbol>> dfsDeclByName, ITypeUsesMap typeUsesMap, Stack<BlockExpression> blocks, ContextInferenceManager ctxInferenceMgr) throws ParseResultsException
  {
    IGosuParser parser = GosuParserFactory.createParser(symbolTable, ScriptabilityModifiers.SCRIPTABLE);
    parser.setScript( strCompiledSource );
    if(parser instanceof GosuParser) {
      parser.setDfsDeclInSetByName(dfsDeclByName);
      if (ctxInferenceMgr != null) {
        ((GosuParser) parser).setContextInferenceManager(ctxInferenceMgr);
      }
    }
    if (typeUsesMap == null) {
      typeUsesMap = parser.getTypeUsesMap();
    } else {
      parser.setTypeUsesMap(typeUsesMap);
    }
    if(_fqn != null) {
      typeUsesMap.addToTypeUses(GosuClassUtil.getPackage(_fqn) + ".*");
    }
    if( blocks != null )
    {
      ((GosuParser) parser).setBlocks( blocks );
    }
    if(_supertype != null && _supertype instanceof IGosuClassInternal ) {
      IGosuClassInternal supertype = (IGosuClassInternal) _supertype;
      addStaticSymbols(symbolTable, (GosuParser) parser, supertype);

      List<? extends GosuClassTypeLoader> typeLoaders = TypeSystem.getCurrentModule().getTypeLoaders(GosuClassTypeLoader.class);
      for (GosuClassTypeLoader typeLoader : typeLoaders) {
        List<? extends IGosuEnhancement> enhancementsForType = typeLoader.getEnhancementIndex().getEnhancementsForType(supertype);
        for (IGosuEnhancement enhancement : enhancementsForType) {
          if (enhancement instanceof IGosuEnhancementInternal) {
            addStaticSymbols(symbolTable, (GosuParser) parser, (IGosuEnhancementInternal) enhancement);
          }
        }
      }

      // clear out private DFS that may have made their way into the dfsDecldsByName (jove this is ugly)
      for (Entry<CaseInsensitiveCharSequence, Set<IFunctionSymbol>> dfsDecls : parser.getDfsDecls().entrySet()) {
        for (Iterator<IFunctionSymbol> it = dfsDecls.getValue().iterator(); it.hasNext(); ) {
          IFunctionSymbol fs = it.next();
          if (fs instanceof Symbol && ((Symbol) fs).isPrivate()) {
            it.remove();
          }
        }
      }
    }
    IScriptPartId scriptPart;
    ISymbol thisSymbol = symbolTable.getSymbol( Keyword.KW_this.getCICS() );
    if( thisSymbol != null && thisSymbol.getType() instanceof IGosuClass )
    {
      scriptPart = new ScriptPartId( thisSymbol.getType(), GS_TEMPLATE_PARSED );
    }
    else
    {
      scriptPart = new TypelessScriptPartId( GS_TEMPLATE_PARSED );
    }
    IFileContext context =
      _fqn != null
      ? new IFileContext()
        {
          public String getClassName()
          {
            // The base name for the Gosu class we generate
            return _fqn;
          }

          public String getContextString()
          {
            // Suffix to distinguish the Gosu class we generate from the actual Gosu template type
            return "_generated_program_for_template_";
          }

          public String getFilePath()
          {
            // Ensure the bytecode class we generate for this has the file ref for debugging

            ITemplateType templateType = (ITemplateType)TypeSystem.getByFullNameIfValid( _fqn );
            return templateType != null
                   ? templateType.getSourceFileHandle().getFileName()
                   : null;
          }
        }
      : null;
    Program program = (Program)parser.parseProgram( scriptPart, null, null, true );
    program.clearParseTreeInformation();
    return program;
  }

  private void addStaticSymbols(ISymbolTable symbolTable, GosuParser parser, IGosuClassInternal supertype) {
    supertype.putClassMembers((GosuParser) parser, symbolTable, supertype, true);
    for(Object entryObj : symbolTable.getSymbols().entrySet()) {
      @SuppressWarnings({"unchecked"})
      Entry<CharSequence, ISymbol> entry = (Entry<CharSequence, ISymbol>) entryObj;
      if(((Symbol)entry.getValue()).isPrivate()) {
        symbolTable.removeSymbol(entry.getKey());
      }
    }
  }

  public void verify( IGosuParser parser, Map<CaseInsensitiveCharSequence, Set<IFunctionSymbol>> dfsDeclByName, ITypeUsesMap typeUsesMap) throws ParseResultsException
  {
    verify( parser, dfsDeclByName, typeUsesMap, false );
  }
  private IProgram verify( IGosuParser parser, Map<CaseInsensitiveCharSequence, Set<IFunctionSymbol>> dfsDeclByName, ITypeUsesMap typeUsesMap, boolean bDoNotThrowParseResultsException ) throws ParseResultsException
  {
    assert _scriptStr != null : "Cannot verify a template after it has been compiled";
    ISymbolTable symTable = parser.getSymbolTable();
    symTable.pushScope();
    try
    {
      parser.setScript( _scriptStr );
      if (parser instanceof GosuParser) {
        parser.setTokenizerInstructor( new TemplateTokenizerInstructor(((GosuParser)parser).getTokenizer()) );
        parser.setDfsDeclInSetByName(dfsDeclByName);
      }
      if (typeUsesMap == null) {
        typeUsesMap = parser.getTypeUsesMap();
      } else {
        parser.setTypeUsesMap(typeUsesMap);
      }
      if(_fqn != null) {
        typeUsesMap.addToTypeUses(GosuClassUtil.getPackage(_fqn) + ".*");
      }
      parser.setTypeUsesMap(typeUsesMap);
      IScriptPartId scriptPart = parser.getScriptPart() == null
                                 ? new TypelessScriptPartId( GS_TEMPLATE )
                                 : new ScriptPartId( parser.getScriptPart().getContainingType(), GS_TEMPLATE );
      return parser.parseProgram( scriptPart, true, false, null, null, false, bDoNotThrowParseResultsException );
    }
    finally
    {
      symTable.popScope();
      for (IParseTree parseTree : parser.getLocations()) {
        parseTree.setLength(Math.min(parseTree.getLength(), _scriptStr.length() - parseTree.getOffset()));
      }
    }
  }

  public IProgram verifyAndGetProgram( IGosuParser parser )
  {
    try
    {
      return verify( parser, true );
    }
    catch( ParseResultsException e )
    {
      throw new IllegalStateException( "Should not have thrown ParseResultsException: " + e );
    }
  }

  public void verify( IGosuParser parser ) throws ParseResultsException
  {
    verify( parser, false );
  }

  private IProgram verify( IGosuParser parser, boolean bDoNotThrowParseResultException ) throws ParseResultsException
  {
    HashMap<CaseInsensitiveCharSequence, Set<IFunctionSymbol>> dfsMap = new HashMap<CaseInsensitiveCharSequence, Set<IFunctionSymbol>>();
    dfsMap.put(CaseInsensitiveCharSequence.get(PRINT_METHOD), Collections.<IFunctionSymbol>singleton(new DynamicFunctionSymbol(parser.getSymbolTable(),
            PRINT_METHOD,
            new FunctionType(PRINT_METHOD, GosuParserTypes.NULL_TYPE(), new IType[]{GosuParserTypes.STRING_TYPE(), GosuParserTypes.BOOLEAN_TYPE()}),
            Arrays.<ISymbol>asList(new Symbol("content", GosuParserTypes.STRING_TYPE(), null), new Symbol("escape", GosuParserTypes.BOOLEAN_TYPE(), null)),
            (IExpression)null)));
    return verify( parser, dfsMap, null, bDoNotThrowParseResultException );
  }

  public List<TemplateParseException> getTemplateSyntaxProblems() {
    List<TemplateParseException> exceptions = new ArrayList<TemplateParseException>();
    transformTemplate(_scriptStr, exceptions);
    return exceptions;
  }

  @SuppressWarnings("ThrowableInstanceNeverThrown")
  private String transformTemplate(String strSource, List<TemplateParseException> exceptions)
  {
    _params.clear();
    StringBuilder strTarget = new StringBuilder(strSource.length());
    strSource = strip( strSource, COMMENT_BEGIN, COMMENT_END );
    int iIndex = 0;
    while( true )
    {
      int iIndex2 = strSource.indexOf( SCRIPTLET_BEGIN, iIndex );
      int altIndex2 = strSource.indexOf( ALTERNATE_EXPRESSION_BEGIN, iIndex );
      if( iIndex2 >= 0 || altIndex2 >= 0 ) {
        if (iIndex2 >= 0 && (altIndex2 < 0 || iIndex2 < altIndex2)) {
          if (iIndex2 > 0 && strSource.charAt(iIndex2 - 1) == '\\') {
            addText(strTarget, strSource.substring(iIndex, iIndex2 - 1));
            addText(strTarget, SCRIPTLET_BEGIN.substring(0, 1));
            iIndex = iIndex2 + 1;
          } else {
            addText(strTarget, strSource.substring(iIndex, iIndex2));
            iIndex = iIndex2 + SCRIPTLET_BEGIN_LEN;
            if (iIndex < strSource.length()) {
              boolean bExpression = strSource.charAt(iIndex) == EXPRESSION_SUFFIX;
              boolean bDeclaration = strSource.charAt(iIndex) == DECLARATION_SUFFIX;
              boolean bDirective = strSource.charAt(iIndex) == DIRECTIVE_SUFFIX;
              iIndex += ((bExpression || bDeclaration || bDirective) ? 1 : 0);

              iIndex2 = strSource.indexOf(SCRIPTLET_END, iIndex);
              if (iIndex2 < 0) {
                int iLineNumber = GosuStringUtil.getLineNumberForIndex(strSource, iIndex);
                int iColumn = getColumnForIndex(strSource, iIndex);
                exceptions.add(new TemplateParseException(bExpression ? Res.MSG_TEMPLATE_MISSING_END_TAG_EXPRESSION : Res.MSG_TEMPLATE_MISSING_END_TAG_SCRIPTLET, iLineNumber, iColumn, iIndex));
                return strTarget.toString();
              }

              String strScript = strSource.substring(iIndex, iIndex2);
              if (bExpression) {
                addExpression(strTarget, strScript);
              } else if (bDirective) {
                try {
                  int iLineNumber = GosuStringUtil.getLineNumberForIndex(strSource, iIndex);
                  int iColumn = getColumnForIndex(strSource, iIndex);
                  processDirective(strScript, iLineNumber, iColumn, iIndex);
                } catch (TemplateParseException e) {
                  exceptions.add(e);
                }
              } else {
                addScriptlet(strTarget, strScript);
              }
            }

            iIndex = iIndex2 + SCRIPTLET_END_LEN;
          }
        } else if (_disableAlternative && altIndex2 > 0 && strSource.charAt(altIndex2 - 1) != '\\') {
          addText(strTarget, strSource.substring(iIndex, altIndex2 - 1));
          addText(strTarget, "\\" + ALTERNATE_EXPRESSION_BEGIN.substring(0, 1));
          iIndex = altIndex2 + 1;
        } else {
          if (altIndex2 > 0 && strSource.charAt(altIndex2 - 1) == '\\') {
            addText(strTarget, strSource.substring(iIndex, altIndex2 - 1));
            addText(strTarget, ALTERNATE_EXPRESSION_BEGIN.substring(0, 1));
            iIndex = altIndex2 + 1;
          } else {
            addText(strTarget, strSource.substring(iIndex, altIndex2));
            iIndex = altIndex2 + ALTERNATE_EXPRESSION_BEGIN_LEN;

            altIndex2 = strSource.indexOf(ALTERNATE_EXPRESSION_END, iIndex);
            int nextOpen = strSource.indexOf("{", iIndex);
            if (nextOpen != -1 && nextOpen < altIndex2) {
              int numOpen = 2;
              while (numOpen > 1 && altIndex2 != -1) {
                nextOpen = strSource.indexOf("{", Math.min(nextOpen, altIndex2) + 1);
                if (nextOpen != -1 && nextOpen < altIndex2) {
                  numOpen++;
                } else {
                  numOpen--;
                  altIndex2 = strSource.indexOf("}", altIndex2 + ALTERNATE_EXPRESSION_END_LEN);
                }
              }
            }
            if (altIndex2 < 0) {
              int iLineNumber = GosuStringUtil.getLineNumberForIndex(strSource, iIndex);
              int iColumn = getColumnForIndex(strSource, iIndex);
              exceptions.add(new TemplateParseException(Res.MSG_TEMPLATE_MISSING_END_TAG_EXPRESSION_ALT, iLineNumber, iColumn, iIndex));
              return strTarget.toString();
            }

            String strScript = strSource.substring(iIndex, altIndex2);
            addExpression(strTarget, strScript);

            iIndex = altIndex2 + ALTERNATE_EXPRESSION_END_LEN;
          }
        }
      }
      else
      {
        addText( strTarget, strSource.substring( iIndex ) );
        break;
      }
    }

    return strTarget.toString();
  }

  private void processDirective(String strScript, int lineNumber, int column, int offset) throws TemplateParseException {
    strScript = strScript.trim();
    if(strScript.startsWith("params")) {
      if(!_params.isEmpty()) {
        throw new TemplateParseException(Res.MSG_TEMPLATE_MULTIPLE_PARAMS, lineNumber, column, offset);
      }
      int iOpeningParen = strScript.indexOf( "(" );
      int iClosingParen = strScript.lastIndexOf( ")" );
      String strSignature = "";
      if( iOpeningParen > 0 && iClosingParen > 0 ) {
       strSignature = strScript.substring( iOpeningParen + 1, iClosingParen ).trim();
      }
      if (strSignature.length() > 0) {
        // get type uses map first
        GosuParser usesParser = (GosuParser) GosuParserFactory.createParser(_scriptStr);
        usesParser.setTokenizerInstructor( new TemplateTokenizerInstructor(usesParser.getTokenizer()) );
        if( _fqn != null ) {
          ITypeUsesMap typeUsesMap = usesParser.getTypeUsesMap();
          if( typeUsesMap != null ) {
            typeUsesMap.addToTypeUses(GosuClassUtil.getPackage(_fqn) + ".*");
          }
        }
        try {
          usesParser.parseProgram( new TypelessScriptPartId( GS_TEMPLATE ) );
        } catch (ParseResultsException e) {
          // there are going to be errors, ignore them
        }
        // now parse signature
        GosuParser parser = (GosuParser) GosuParserFactory.createParser(strSignature);
        parser.setEditorParser(_useStudioEditorParser);
        parser.setTypeUsesMap(usesParser.getTypeUsesMap());
        parser.getTokenizer().nextToken();
        Identifier pe = new Identifier();
        _params.addAll(parser.parseParameterDeclarationList(pe, false, null));
        if (pe.hasParseExceptions()) {
          throw new TemplateParseException(Res.MSG_TEMPLATE_INVALID_PARAMS, lineNumber, column, offset, strScript + ": " + pe.getParseExceptions().get(0).getConsoleMessage());
        }
      }
    } else if (strScript.startsWith("extends")) {
      String typeName = strScript.substring(strScript.indexOf("extends") + "extends".length()).trim();
      _supertype = TypeLoaderAccess.instance().getByFullNameIfValid(typeName);
      if(_supertype == null) {
        throw new TemplateParseException(Res.MSG_INVALID_TYPE, lineNumber, column, offset, typeName);
      }
    } else {
      throw new TemplateParseException(Res.MSG_TEMPLATE_UNKNOWN_DIRECTIVE, lineNumber, column, offset, strScript);
    }
  }


  private String strip( String strSource, String strBeginDelim, String strEndDelim )
  {
    String strTarget = "";

    int iIndex = 0;
    while( true )
    {
      int iIndex2 = strSource.indexOf( strBeginDelim, iIndex );
      if( iIndex2 >= 0 )
      {
        strTarget += strSource.substring( iIndex, iIndex2 );
        iIndex2 = strSource.indexOf( strEndDelim, iIndex );
        if(iIndex2 == -1) {
          break;
        }
        iIndex = iIndex2 + strEndDelim.length();
      }
      else
      {
        strTarget += strSource.substring( iIndex );
        break;
      }
    }

    return strTarget;
  }

  private void addText( StringBuilder strTarget, String strText ) {
    if (!GosuStringUtil.isEmpty(strText)) {
      strText = escapeForGosuStringLiteral(strText);
      strTarget.append(PRINT_METHOD).append("((\"").append(strText).append("\") as String, false)\r\n");
    }
  }

  private void addExpression( StringBuilder strTarget, String strExpression )
  {
    strTarget.append(PRINT_METHOD).append("((").append(strExpression).append(") as String, true)\r\n");
  }

  private void addScriptlet( StringBuilder strTarget, String strScript )
  {
    strTarget.append(strScript).append("\r\n");
  }

  private String escapeForGosuStringLiteral( String strText )
  {
    if( strText == null )
    {
      return null;
    }

    strText = GosuEscapeUtil.escapeForGosuStringLiteral( strText );
    return strText;
  }

  private int getColumnForIndex( String strSource, int iIndex )
  {
    int lastLineBreak = 0;
    for( int i = 0; i <= iIndex && i < strSource.length(); i++ )
    {
      char c = strSource.charAt( i );
      if( c == '\n' )
      {
        lastLineBreak = i;
      }
    }

    return iIndex - lastLineBreak;
  }

  /**
   * For internal use only!!
   */
  public static void printContent( String strContent , boolean escape )
  {
    try
    {
      WriterEscaperPair pair = getWriterEscaperPair();
      Writer writer = pair._writer;

      if (escape && pair._esc != null) {
        strContent = pair._esc.escape(strContent);
      } else if (pair._esc instanceof IEscapesAllContent) {
        strContent = ((IEscapesAllContent)pair._esc).escapeBody(strContent);
      }

      writer.write( strContent == null ? "null" : strContent );
    }
    catch( IOException e )
    {
      throw new RuntimeException( e );
    }
  }

  public void setDisableAlternative(boolean disableAlternative) {
    _disableAlternative = disableAlternative;
  }

  public void setContextInferenceManager( ContextInferenceManager ctxInferenceMgr )
  {
    _ctxInferenceMgr = ctxInferenceMgr.copy();
  }

  private static class WriterEscaperPair{
    Writer _writer;
    StringEscaper _esc;
    public WriterEscaperPair(Writer writer, StringEscaper esc) {
      _writer = writer;
      _esc = esc;
    }
  }

  public boolean isValid() {
    compileIfNotCompiled();
    return _program != null && !_program.hasParseExceptions();
  }

  private void setFqn(String fullyQualifiedName) {
    _fqn = fullyQualifiedName;
  }

  public String getFullyQualifiedTypeName()
  {
    return _fqn;
  }

  public List<ISymbol> getParameters() {
    compileIfNotCompiled();
    return _params;
  }

  private void compileIfNotCompiled() {
    if(_program == null) {
      if(_hasOwnSymbolScope) {
        CompiledGosuClassSymbolTable.instance().pushCompileTimeSymbolTable();
      }
      try {
        compile(CompiledGosuClassSymbolTable.instance());
      } catch (TemplateParseException e) {
        // ignore?
      } finally {
        if (_hasOwnSymbolScope) {
          CompiledGosuClassSymbolTable.instance().popCompileTimeSymbolTable();
        }
      }
    }
  }
  
  public IType getSuperType() {
    return _supertype;
  }

  public void setUseStudioEditorParser(boolean useStudioEditorParser) {
    _useStudioEditorParser = useStudioEditorParser;
  }

  @Override
  public String toString() {
    if (_scriptStr != null) {
      return _scriptStr;
    } else {
      return _program.toString();
    }
  }

  public String getSource() {
    return _scriptStr;
  }

  private IExternalSymbolMap extractExternalSymbols( ISymbolTable compileTimeSymbolTable, ISymbolTable runtimeSymbolTable ) {
    CaseInsensitiveHashMap<CaseInsensitiveCharSequence, ISymbol> externalSymbolsMap = new CaseInsensitiveHashMap<CaseInsensitiveCharSequence, ISymbol>( 8 );
    putSymbols( compileTimeSymbolTable, externalSymbolsMap );
    putSymbols( runtimeSymbolTable, externalSymbolsMap ); // overwrite compile time symbols with the runtime stuff.  yeesh
    return new ExternalSymbolMapForMap(externalSymbolsMap);
  }

  private void putSymbols( ISymbolTable symTable, CaseInsensitiveHashMap<CaseInsensitiveCharSequence, ISymbol> externalSymbolsMap )
  {
    Map symbols = symTable.getSymbols();
    if( symbols != null )
    {
      //noinspection unchecked
      for( ISymbol sym : (Collection<ISymbol>)symbols.values() )
      {
        if( !(sym instanceof CommonSymbolsScope.LockedDownSymbol) && sym != null )
        {
          externalSymbolsMap.put( sym.getCaseInsensitiveName(), sym );
        }
      }
    }
  }
}
