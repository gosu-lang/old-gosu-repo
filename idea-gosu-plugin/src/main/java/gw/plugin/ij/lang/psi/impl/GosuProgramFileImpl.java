/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.Iconable;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.util.IncorrectOperationException;
import gw.internal.gosu.parser.AbstractTypeRef;
import gw.lang.parser.IGosuParser;
import gw.lang.parser.IGosuValidator;
import gw.lang.parser.IParsedElement;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.ITypeUsesMap;
import gw.lang.parser.ParserOptions;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.parser.exceptions.SymbolNotFoundException;
import gw.lang.parser.statements.IClassFileStatement;
import gw.lang.parser.statements.IClassStatement;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuProgram;
import gw.lang.reflect.module.Dependency;
import gw.lang.reflect.module.IModule;
import gw.plugin.ij.icons.GosuIcons;
import gw.plugin.ij.lang.GosuLanguage;
import gw.plugin.ij.lang.parser.GosuAstTransformer;
import gw.plugin.ij.lang.psi.IGosuPsiElement;
import gw.plugin.ij.util.IDEAUtil;
import gw.plugin.ij.util.InjectedElementEditor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

import static com.google.common.collect.Iterables.filter;

public class GosuProgramFileImpl extends AbstractGosuClassFileImpl {
  public GosuProgramFileImpl(@NotNull FileViewProvider viewProvider) {
    super(viewProvider, GosuLanguage.instance());
  }

  @SuppressWarnings({"CloneDoesntDeclareCloneNotSupportedException"})
  protected GosuProgramFileImpl clone() {
    return (GosuProgramFileImpl) super.clone();
  }

  public ASTNode parse(@NotNull ASTNode chameleon) {
    return GosuAstTransformer.instance().transformProgram(chameleon, (IGosuProgram) parseType(false)).getFirstChildNode();
  }

  @Override
  protected IGosuParser createParser(CharSequence contents) {
    final IGosuParser parser = super.createParser(contents);

    final ITypeUsesMap typeUsesMap = GosuParserConfigurer.getTypeUsesMap(this);
    if (typeUsesMap != null) {
      parser.setTypeUsesMap(typeUsesMap);
    }

    final ISymbolTable symbolTable = GosuParserConfigurer.getSymbolTable(this);
    if (symbolTable != null) {
      parser.setSymbolTable(symbolTable);
    }

    final IGosuValidator validator = GosuParserConfigurer.getValidator(this);
    if (validator != null) {
      parser.setValidator(validator);
    }

    return parser;
  }

  protected ParserOptions createParserOptions() {
    return new ParserOptions();
  }

  public IGosuProgram parseType(String strClassName, String contents, int completionMarkerOffset) {
    final IModule module = getModule();
    TypeSystem.pushModule(module);
    // DP: this line should execute outside the TS lock to avoid deadlock
    final IGosuParser parser = createParser(contents);
    TypeSystem.lock();
    try {
      //refreshTheOldType(strClassName, module);
      IGosuProgram program;
      IClassFileStatement classFileStmt;
      ISymbolTable snapshotSymbols = null;
      IType ctxType = null;
      try {
        if (completionMarkerOffset >= 0) {
          parser.setSnapshotSymbols();
        }
        program = IDEAUtil.parseProgram(parser, createParserOptions(), new ModuleFileContext(module, strClassName), contents);
        classFileStmt = program.getClassStatement().getClassFileStatement();
      } catch (ParseResultsException ex) {
        classFileStmt = (IClassFileStatement) ex.getParsedElement();
        IClassStatement classStatement = classFileStmt.getClassStatement();
        program = classStatement != null ? (IGosuProgram) classStatement.getGosuClass() : null;


        final SymbolNotFoundException issue = getExceptionWithSymbolTable(ex);
        if (issue != null) {
          snapshotSymbols = issue.getSymbolTable();
          ctxType = issue.getExpectedType();
        }
      }

      if (getOriginalFile().getVirtualFile() != null) {
        final GosuClassParseData data = getParseData();
        data.setClassFileStatement(classFileStmt);
        data.setSource(contents);
        if (completionMarkerOffset >= 0) {
          data.setSnapshotSymbols(snapshotSymbols);
          data.setContextType(ctxType);
        }
      }

      if (strClassName.contains(IDEAUtil.MAGIC_INJECTED_SUFFIX)) {
        ((AbstractTypeRef) program).setReloadable(false);
      }

      return program;
    } finally {
      TypeSystem.unlock();
      TypeSystem.popModule(module);
    }
  }

  protected void addDependencies(@NotNull IModule gsModule) {
    final IModule jre = gsModule.getExecutionEnvironment().getJreModule();
    gsModule.addDependency(new Dependency(jre, true));
  }

  @Override
  public void setPackageName(String packageName) throws IncorrectOperationException {
    // do nothing, no package stmt in program
    // TODO: thorw exception here?
  }

  @Override
  public PsiElement getNavigationElement() {
    return InjectedElementEditor.getOriginalFile(this);
  }

  @Override
  protected Icon getElementIcon(@IconFlags int flags) {
    return GosuIcons.FILE_PROGRAM;
  }
}
