/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.lang.reflect;

import gw.lang.parser.CaseInsensitiveCharSequence;
import gw.util.CaseInsensitiveHashMap;
import gw.util.DynamicArray;

import java.util.Collection;
import java.util.List;

public class MethodList extends DynamicArray<IMethodInfo> {
  public static final MethodList EMPTY = new MethodList();

  private CaseInsensitiveHashMap<CaseInsensitiveCharSequence, DynamicArray<IMethodInfo>> map = new CaseInsensitiveHashMap<CaseInsensitiveCharSequence, DynamicArray<IMethodInfo>>();

  public MethodList() {
  }

  public MethodList(List<IMethodInfo> methods) {
    addAll(methods);
  }

  public MethodList(int size) {
    super(size);
  }

  public MethodList filterMethods(IRelativeTypeInfo.Accessibility accessibility) {
    MethodList ret = new MethodList();
    for (IMethodInfo method : this) {
      if (FeatureManager.isFeatureAccessible(method, accessibility)) {
        ret.add(method);
      }
    }
    ret.trimToSize();
    return ret;
  }


  @Override
  public boolean add(IMethodInfo method) {
    addToMap(method);
    return super.add(method);
  }

  @Override
  public boolean addAll(Collection<? extends IMethodInfo> c) {
    for (IMethodInfo method : c) {
      addToMap(method);
    }
    return super.addAll(c);
  }

  private void addToMap(IMethodInfo method) {
    CaseInsensitiveCharSequence displayName = CaseInsensitiveCharSequence.get(method.getDisplayName());
    DynamicArray<IMethodInfo> methods = map.get(displayName);
    if (methods == null) {
      methods = new DynamicArray<IMethodInfo>(1);
      map.put(displayName, methods);
    }
    methods.add(method);
  }

  @Override
  public IMethodInfo remove(int index) {
    IMethodInfo oldMethod = get(index);
    CaseInsensitiveCharSequence displayName = CaseInsensitiveCharSequence.get(oldMethod.getDisplayName());
    DynamicArray<IMethodInfo> methods = map.get(displayName);
    int i = methods.indexOf(oldMethod);
    methods.remove(i);

    return super.remove(index);
  }

  @Override
  public IMethodInfo set(int index, IMethodInfo method) {
    IMethodInfo oldMethod = get(index);
    CaseInsensitiveCharSequence displayName = CaseInsensitiveCharSequence.get(method.getDisplayName());
    DynamicArray<IMethodInfo> methods = map.get(displayName);
    int i = methods.indexOf(oldMethod);
    methods.set(i, method);

    return super.set(index, method);
  }


  public DynamicArray<? extends IMethodInfo> getMethods(String name) {
    DynamicArray<IMethodInfo> methodInfoList = map.get(CaseInsensitiveCharSequence.get(name));
    return methodInfoList != null ? methodInfoList : DynamicArray.EMPTY;
  }

  public static MethodList singleton(IMethodInfo theOneMethod) {
    MethodList infos = new MethodList(1);
    infos.add(theOneMethod);
    return infos;
  }

  @Override
  public void add(int index, IMethodInfo method) {
    throw new RuntimeException("Not supported");
  }

  @Override
  public boolean addAll(int index, Collection<? extends IMethodInfo> c) {
    throw new RuntimeException("Not supported");
  }

  @Override
  public boolean remove(Object o) {
    throw new RuntimeException("Not supported");
  }

  @Override
  protected void removeRange(int fromIndex, int toIndex) {
    throw new RuntimeException("Not supported");
  }

  @Override
  public Object clone() {
    return super.clone();
  }

  @Override
  public void clear() {
    super.clear();
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    throw new RuntimeException("Not supported");
  }

  @Override
  public boolean retainAll(Collection<?> c) {
    throw new RuntimeException("Not supported");
  }

}
