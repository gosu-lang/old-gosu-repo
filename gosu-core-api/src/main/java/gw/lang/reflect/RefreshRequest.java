/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.lang.reflect;

import gw.fs.IFile;
import gw.lang.reflect.module.IModule;
import gw.util.IdentitySet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class RefreshRequest {
  public final IFile file;
  public final IModule module;
  public final RefreshKind kind;
  public final boolean sendEvent;
  public final String[] types;

  public RefreshRequest(IFile file, String[] types, IModule module, RefreshKind kind, boolean sendEvent) {
    this.file = file;
    this.module = module;
    this.kind = kind;
    this.sendEvent = sendEvent;
    this.types = types;
  }

  public RefreshRequest(String[] types, RefreshRequest request) {
    this(request.file, types, request.module, request.kind, request.sendEvent);
  }

  public RefreshRequest(IdentitySet<ITypeRef> allTypes, RefreshRequest request) {
    this(toNames(allTypes, request), request);
  }

  private static String[] toNames(IdentitySet<ITypeRef> allTypes, RefreshRequest request) {
    TreeSet<String> names = new TreeSet<String>();
    Collections.addAll(names, request.types);
    for (ITypeRef type : allTypes) {
      names.add(type.getName());
    }
    return names.toArray( new String[names.size()]);
  }

  @Override
  public String toString() {
    String s = module + " " + kind + " of ";
    for (String type : types) {
      s += type + ", ";
    }
    return s;
  }
}
