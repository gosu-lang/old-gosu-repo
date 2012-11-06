/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.util.cache;

import gw.util.Predicate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class FqnCache<T> implements IFqnCache<T> {
  private static Map<String, String[]> PARTS_CACHE = new ConcurrentHashMap<String, String[]>();
  private FqnCacheNode<T> _root = new FqnCacheNode<T>("root", null);

  public FqnCacheNode<T> getRoot() {
    return _root;
  }

  public FqnCacheNode<T> getNode(String fqn) {
    FqnCacheNode<T> n = _root;
    for (String part : getParts(fqn)) {
      n = n.getChild(part);
      if (n == null) {
        break;
      }
    }
    return n;
  }

  @Override
  public final T get( String fqn ) {
    FqnCacheNode<T> n = getNode( fqn );
    return n == null ? null : n.getUserData();
  }

  @Override
  public final boolean contains( String fqn ) {
    return getNode(fqn) != null;
  }

  @Override
  public final void add( String fqn ) {
    add(fqn, null);
  }

  @Override
  public void add( String fqn, T userData ) {
    FqnCacheNode<T> n = _root;
    for (String part : getParts(fqn)) {
      n = n.getOrCreateChild(part);
    }
    n.setUserData(userData);
  }

  @Override
  public final void remove( String[] fqns ) {
    for (String fqn : fqns) {
      remove(fqn);
    }
  }

  @Override
  public boolean remove( String fqn ) {
    FqnCacheNode<T> n = _root;
    for (String part : getParts(fqn)) {
      n = n.getChild(part);
      if( n == null ) {
        return false;
      }
    }
    n.delete();
    return true;
  }

  @Override
  public final void clear() {
    _root.clear();
  }

  @Override
  public Set<String> getFqns() {
    Set<String> names = new HashSet<String>();
    _root.collectNames(names, "");
    return names;
  }

  public void visitDepthFirst( Predicate<T> visitor ) {
    List<FqnCacheNode<T>> copy = new ArrayList<FqnCacheNode<T>>( _root.getChildren() );
    for( FqnCacheNode<T> child: copy ) {
      if( !child.visitDepthFirst( visitor ) ) {
        return;
      }
    }
  }

  public void visitNodeDepthFirst( Predicate<FqnCacheNode> visitor ) {
    List<FqnCacheNode<T>> copy = new ArrayList<FqnCacheNode<T>>( _root.getChildren() );
    for( FqnCacheNode<T> child: copy ) {
      if( !child.visitNodeDepthFirst( visitor ) ) {
        return;
      }
    }
  }

  public void visitBreadthFirst( Predicate<T> visitor ) {
    List<FqnCacheNode<T>> copy = new ArrayList<FqnCacheNode<T>>( _root.getChildren() );
    for( FqnCacheNode<T> child: copy ) {
      child.visitBreadthFirst( visitor );
    }
  }

  private static String[] split( String fqn ) {
    final int len = fqn.length();
    final List<String> segments = new ArrayList<String>(10);

    int genericBeginIndex = -1;
    int genericEndIndex = -1;
    int genericBalance = 0;

    int arrayCount = 0;

    int beginIndex = 0;
    for (int i = 0; i < len; ++i) {
      final char ch = fqn.charAt(i);
      switch (ch) {
        case '.':
          if (genericBeginIndex == -1) {
            segments.add(fqn.substring(beginIndex, i));
            beginIndex = i + 1;
          }
          break;
        case '<':
          if (genericBalance == 0) {
            genericBeginIndex = i;
          }
          genericBalance++;
          break;
        case '>':
          if (genericBalance == 1) {
            genericEndIndex = i;
          }
          genericBalance--;
          break;
        case '[':
          if (genericEndIndex > 0) {
            arrayCount++;
          }
          break;
      }
    }

    if (genericBeginIndex > 0) {
      segments.add(fqn.substring(beginIndex, genericBeginIndex));
      segments.add(fqn.substring(genericBeginIndex, genericEndIndex + 1));
    } else {
      segments.add(fqn.substring(beginIndex, len - 2 * arrayCount));
    }

    for (int i = 0; i < arrayCount; ++i) {
      segments.add("[]");
    }

    return segments.toArray( new String[segments.size()] );
  }

  public static String[] getParts(String fqn) {
    String[] strings = PARTS_CACHE.get(fqn);
    if (strings == null) {
      strings = split(fqn);
      PARTS_CACHE.put(fqn, strings);
    }
    return strings;
  }

}
