/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.internal.gosu.parser.statements.VarStatement;
import gw.lang.parser.CaseInsensitiveCharSequence;
import gw.lang.parser.IBlockClass;
import gw.lang.parser.ICapturedSymbol;
import gw.lang.parser.ISymbol;
import gw.lang.parser.ISymbolTable;
import gw.lang.parser.ITypeUsesMap;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.parser.expressions.ITypeVariableDefinition;
import gw.lang.parser.expressions.IVarStatement;
import gw.lang.reflect.IFunctionType;
import gw.lang.reflect.IType;
import gw.lang.reflect.gs.GosuClassTypeLoader;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.java.IJavaType;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 */
public interface IGosuClassInternal extends IGosuClass, ICompilableTypeInternal
{
  public static final String ANNOTATION_METHODS_FOR_INTERFACE_INNER_CLASS = "_annotation_methods_";

  void copyGenericState( boolean bCopyHierarchy );
  void copyHierarchyInfo();

  void assignTypeUsesMap( GosuParser parser );

  List<IFunctionType> getUnimplementedMethods( List<IFunctionType> unimpled, IGosuClassInternal implClass );

  void setJavaType( IJavaType javaType );

  void forceTypeInfoInitialization();
  
  int getDepth();

  int getTypeInfoChecksum();

  Collection<IGosuClassInternal> getParameterizedTypes();

  void setAnnotations(List<IGosuAnnotation> annotations);

  IType getEnclosingNonBlockType();

  Map<String, List> getRuntimeFeatureAnnotationMap();

  List<? extends IGosuAnnotation> getGosuAnnotations();

  /**
   * When changing the places from which this method is called run pc's
   * gw.smoketest.pc.job.common.effectivetime.VisibleEffectiveTimeTest
   * cause it will break!
   */
  GosuClassParseInfo createNewParseInfo();

  GosuClassParseInfo getParseInfo();

  void setGenericTypeVariables(List<ITypeVariableDefinition> typeVarLiteralList);

  void setModifierInfo( ModifierInfo modifierInfo );

  class Util
  {
    public static IGosuClassInternal getGosuClassFrom( IType type )
    {
      if( type instanceof IGosuClassInternal )
      {
        return (IGosuClassInternal)type;
      }

      if( type instanceof IJavaType )
      {
        IGosuClassInternal adapterClass = ((IJavaTypeInternal)type).getAdapterClass();
        if( adapterClass == null )
        {
          adapterClass = GosuClassProxyFactory.instance().createImmediately( type );
        }
        if( type.isParameterizedType() )
        {
          return (IGosuClassInternal)adapterClass.getParameterizedType( type.getTypeParameters() );
        }
        else
        {
          return adapterClass;
        }
      }

      return null;
    }
  }


  void setInterface( boolean bInterface );

  void setEnum();

  void addInterface( IType type );

 void markStatic();

  boolean isProxy();

  List<DynamicFunctionSymbol> getConstructorFunctions();

  DynamicFunctionSymbol getConstructorFunction( CaseInsensitiveCharSequence name );

  DynamicFunctionSymbol getDefaultConstructor();

  List<DynamicFunctionSymbol> getStaticFunctions();

  public List<DynamicFunctionSymbol> getMemberFunctions( CaseInsensitiveCharSequence names );

  void addInnerClass( IGosuClassInternal innerGsClass );
  void removeInnerClass( IGosuClassInternal innerGsClass );

  DynamicFunctionSymbol getMemberFunction( CaseInsensitiveCharSequence name );

  DynamicPropertySymbol getStaticProperty( CaseInsensitiveCharSequence name );

  List<DynamicPropertySymbol> getStaticProperties();

  @SuppressWarnings({"unchecked"})
  List<DynamicPropertySymbol> getMemberProperties();

  DynamicPropertySymbol getMemberProperty( CaseInsensitiveCharSequence name );

  @SuppressWarnings({"unchecked"})
  List<IVarStatement> getStaticFields();

  VarStatement getStaticField( CaseInsensitiveCharSequence name );

  Map<CharSequence, ISymbol> getMemberFieldIndexByName();

  Symbol getStaticThisSymbol();

  Map<CaseInsensitiveCharSequence, ICapturedSymbol> getCapturedSymbols();

  ICapturedSymbol getCapturedSymbol( CaseInsensitiveCharSequence strName );

  void addCapturedSymbol( ICapturedSymbol sym );

  boolean ensureDefaultConstructor( ISymbolTable symbolTable );

  void setSuperType( IType superType );

  void compileDefinitionsIfNeeded();

  void compileDefinitionsIfNeeded( boolean bForce );

  void compileDeclarationsIfNeeded();

  void compileHeaderIfNeeded();

  void setCompilingHeader( boolean bCompilingHeader );

  void setHeaderCompiled();

  void setCompilingDeclarations( boolean bCompilingDeclarations );

  void setDeclarationsCompiled();

  void setInnerDeclarationsCompiled();

  void setCompilingDefinitions( boolean bCompilingDefinitions );

  void setDefinitionsCompiled();

  VarStatement getMemberField( CaseInsensitiveCharSequence charSequence );

  IGosuClassInternal getSuperClass();

  void putClassMembers( GosuParser owner, ISymbolTable table, IGosuClassInternal gsContextClass, boolean bStatic );
  void putClassMembers( GosuClassTypeLoader loader, GosuParser owner, ISymbolTable table, IGosuClassInternal gsContextClass, boolean bStatic );

  boolean isAccessible( IGosuClassInternal compilingClass, AbstractDynamicSymbol ads );

  void setParseResultsException( ParseResultsException pe );

  boolean shouldResolve();

  void setEditorParser( GosuParser parser );

  void syncGenericAndParameterizedClasses();

  void addDelegateImpls( ISymbolTable symTable, GosuClassParser parser );

  List<IFunctionType> getUnimplementedMethods();

  void setFullDescription( String description );

  String getFullDescription();

  Object dontEverCallThis();

  /**
   * @return True if this type is no longer current or has been unloaded.
   */
  public boolean isStale();

  boolean isCannotCaptureSymbols();

  void setCannotCaptureSymbols( boolean val );
  public List<IGosuClass> getBlocks();

  public int getBlockCount();

  public void addBlock( IBlockClass block );
  public void removeBlock( IBlockClass block );

  @Override
  public GosuClassTypeLoader getTypeLoader();
  boolean isCreateEditorParser();

  public boolean isCompiledToUberModule();
  public void setCompiledToUberModule( boolean bUber );

  void setTypeUsesMap( ITypeUsesMap usesMap );

}
