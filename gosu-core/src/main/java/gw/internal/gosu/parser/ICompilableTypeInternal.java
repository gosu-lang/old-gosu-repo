/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.reflect.IType;
import gw.lang.reflect.gs.GosuClassTypeLoader;
import gw.lang.reflect.gs.ICompilableType;
import gw.lang.parser.expressions.IVarStatement;
import gw.lang.parser.CaseInsensitiveCharSequence;
import gw.lang.parser.ICapturedSymbol;
import gw.lang.parser.IBlockClass;
import gw.lang.parser.IDynamicFunctionSymbol;
import gw.lang.parser.IDynamicPropertySymbol;
import gw.lang.parser.ISymbol;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.statements.IClassStatement;
import gw.internal.gosu.parser.statements.StatementList;

import java.util.Map;
import java.util.List;

public interface ICompilableTypeInternal extends ICompilableType {

  ICompilableTypeInternal getEnclosingType();

  Map<CaseInsensitiveCharSequence, ICapturedSymbol> getCapturedSymbols();

  IVarStatement getMemberField( CaseInsensitiveCharSequence charSequence );

  void addBlock(IBlockClass blockClass);

  int getBlockCount();

  void addCapturedSymbol(ICapturedSymbol capturedSymbol);

  ICapturedSymbol getCapturedSymbol( CaseInsensitiveCharSequence strName );

  List<? extends IDynamicFunctionSymbol> getMemberFunctions( CaseInsensitiveCharSequence names );

  IDynamicPropertySymbol getMemberProperty(CaseInsensitiveCharSequence caseInsensitiveCharSequence);

  IType getEnclosingNonBlockType();

  DynamicPropertySymbol getStaticProperty(CaseInsensitiveCharSequence strPropertyName);

  int getDepth();

  void compileDeclarationsIfNeeded();

  void compileDefinitionsIfNeeded( boolean bForce );

  void compileHeaderIfNeeded();

  void putClassMembers(GosuParser owner, ISymbolTable table, IGosuClassInternal gsContextClass, boolean bStatic);  
  void putClassMembers(GosuClassTypeLoader loader, GosuParser owner, ISymbolTable table, IGosuClassInternal gsContextClass, boolean bStatic);

  void assignTypeUsesMap(GosuParser parser);

  boolean isCreateEditorParser();
    
  int getAnonymousInnerClassCount();
    
  String getInterfaceMethodsClassName();

  List<StatementList> getAnnotationInitialization();

  List<? extends IGosuAnnotation> getGosuAnnotations();

  boolean shouldFullyCompileAnnotations();

  List<? extends IVarStatement> getMemberFields();

  List<IVarStatement> getStaticFields();

  String getSource();

  GosuClassParseInfo getParseInfo();
}