/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.lang.reflect;

import gw.config.BaseService;
import gw.fs.IDirectory;
import gw.fs.IFile;
import gw.lang.Scriptable;
import gw.lang.annotation.ScriptabilityModifier;
import gw.lang.reflect.gs.TypeName;
import gw.lang.reflect.module.IModule;
import gw.util.GosuClassUtil;

import java.io.File;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class TypeLoaderBase extends BaseService implements ITypeLoader, ITypeLoaderListener
{
  protected IModule _module;

  /** @deprecated use TypeLoaderBase( IModule ) */
  protected TypeLoaderBase()
  {
    this( TypeSystem.getExecutionEnvironment().getCurrentModule() );
    System.out.println( "WARNING: " + getClass().getName() + " was constructed without specifying a module!" );
  }

  protected TypeLoaderBase( IModule module )
  {
    _module = module;
  }
  
  @Override
  public IModule getModule()
  {
    return _module;
  }

  @Override
  public boolean isCaseSensitive()
  {
    return false;
  }

  @Override
  public boolean handlesFile(IFile file) {
    return false;
  }

  @Override
  public boolean handlesDirectory(IDirectory dir) {
    return false;
  }

  @Override
  public String getNamespaceForDirectory(IDirectory dir) {
    return null;
  }

  @Override
  public String[] getTypesForFile(IFile file) {
    return NO_TYPES;
  }

  @Override
  public void refreshedFile(IFile file, String[] types, RefreshKind kind) {
  }

  @Override
  @Scriptable(ScriptabilityModifier.ALL)
  public URL getResource(String name) {
    return null;
  }

  @Override
  public File getResourceFile(String name) {
    URL resource = getResource(name);
    if (resource != null) {
      File parent = TypeSystem.getResourceFileResolver().resolveURLToFile(name, resource);
      return parent != null ? new File(parent, name) : null;
    } else {
      return null;
    }
  }

  @Override
  public void refresh(boolean clearCachedTypes)
  {
  }

  @Override
  public void refreshedTypes(RefreshRequest request)
  {
  }

  @Override
  public void refreshed()
  {
  }

  public String toString() {
    return this.getClass().getSimpleName() + " for module " + getModule().getName();  
  }

  @Override
  public Set<TypeName> getTypeNames(String namespace) {
    if (hasNamespace(namespace)) {
      return TypeLoaderBase.getTypeNames(namespace, this);
    } else {
      return Collections.emptySet();
    }
  }

  public static Set<TypeName> getTypeNames( String parentNamespace, ITypeLoader loader ) {
    Set<TypeName> typeNames = new HashSet<TypeName>();
    for ( CharSequence typeNameCS : loader.getAllTypeNames() ) {
      String typeName = typeNameCS.toString();
      String packageName = GosuClassUtil.getPackage( typeName );
      if ( packageName.equals( parentNamespace ) ) {
        typeNames.add( new TypeName( typeName, loader, TypeName.Kind.TYPE, TypeName.Visibility.PUBLIC ) );
      }
    }
    for ( CharSequence namespaceCs : loader.getAllNamespaces() ) {
      String namespace = namespaceCs.toString();
      String containingPackageName = GosuClassUtil.getPackage( namespace );
      if ( containingPackageName.equals( parentNamespace ) ) {
        typeNames.add( new TypeName( GosuClassUtil.getNameNoPackage( namespace ), loader, TypeName.Kind.NAMESPACE, TypeName.Visibility.PUBLIC ) );
      }
    }
    return typeNames;
  }

  @Override
  public boolean showTypeNamesInIDE() {
    return true;
  }
}
