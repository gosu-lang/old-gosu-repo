/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.internal.gosu.properties;

import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.IGosuObject;
import gw.util.CaseInsensitiveSet;
import gw.util.GosuClassUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * A node in a tree representation of an underlying {@link PropertySet}. Any compound names, such
 * as a.b.c and a.b.d, in the keys of the property set are split into a tree representation. In the
 * a.b.c/a.b.d example there would be a property node for a, with a child node b with two further
 * leaf children c and d.
 */
public class PropertyNode implements IGosuObject {

  private final PropertySet _propertySet;
  private PropertyNode _parent;
  private final List<PropertyNode> _children;
  private final String _path;
  
  public static PropertyNode buildTree(PropertySet propertySet) {
    return new PropertyNode(propertySet, "");
  }

  private PropertyNode(PropertySet propertySet, String propertyPath) {
    _propertySet = propertySet;
    _children = findChildren(propertySet, propertyPath);
    _path = propertyPath;
  }

  /**
   * The full property name, for example a.b
   * @return a non null name, which must be one or more valid Gosu identifiers separated by periods
   */
  public String getFullName() {
    return join(_propertySet.getName(), _path);
  }

  /**
   * The last part of the property name, for example b if the full name is a.b
   * @return a non null name, which must be a valid Gosu identifier
   */
  public String getRelativeName() {
    return GosuClassUtil.getShortClassName(getFullName());
  }
  
  /**
   * Return the name that should be used for the type based on this property node
   * @return a non null type name
   */
  public String getTypeName() {
    return isRoot() ? getFullName() : getFullName() + "$Type";
  }

  /**
   * Return the intrinsic type based on this property node
   */
  @Override
  public IType getIntrinsicType() {
    return TypeSystem.getByFullName(getTypeName());
  }

  /**
   * Does this property node have a value in the underlying {@link PropertySet}
   * @return true if the node has an underlying value, false otherwise
   */
  public boolean hasValue() {
    return _propertySet.getKeys().contains(_path);
  }

  /**
   * Return the value for this property as given by the underlying {@link PropertySet}
   * @return the property value, or null if it doesn't have one
   */
  public String getValue() {
    return hasValue() ? _propertySet.getValue(_path) : null;
  }

  /**
   * Is this a leaf node - that is, does it have no children?
   * @return true if this node has no children, false otherwise
   */
  public boolean isLeaf() {
    return getChildren().isEmpty();
  }
  
  /**
   * Is this the root of a property node tree?
   * @return true if this is the root, false otherwise
   */
  public boolean isRoot() {
    return _path.isEmpty();
  }

  /**
   * The direct children of this property node
   * @return a non null, though possibly empty, list of children
   */
  public List<PropertyNode> getChildren() {
    return _children;
  }

  /**
   * Return the value for the named child property; this is just like doing lookup on the underlying
   * {@link PropertySet} except that the name is prefixed with the full name of this property. For
   * example if this property is a then getting the child value b.c will return the value of a.b.c
   * in the original property set
   * @param name non null name of child property
   * @return the child property value, or null if there is no such child property
   */
  public String getChildValue(String name) {
    return _propertySet.getValue(join(_path, name));
  }

  /**
   * If this node has a property value, returns the value of that property. Otherwise returns
   * a string describing the property name.
   */
  @Override
  public String toString() {
    return hasValue() ? getValue() : String.format("Property <%s>", _path);
  }

  private List<PropertyNode> findChildren(PropertySet propertySet, String path) {
    List<PropertyNode> result = new ArrayList<PropertyNode>();
    String prefix = path.isEmpty() ? "" : path + ".";
    Set<String> alreadyAdded = new CaseInsensitiveSet<String>();
    for (String name : propertySet.getKeys()) {
      if (name.startsWith(prefix)) {
        String namePart = getFirstNamePart(prefix, name);
        if (isGosuIdentifier(namePart) && !alreadyAdded.contains(namePart)) {
          PropertyNode child = new PropertyNode(propertySet, join(path, namePart));
          if (!child.isUseless()) {
            result.add(child);
            child._parent = this;
          }
          alreadyAdded.add(namePart);
        }
      }
    }
    return Collections.unmodifiableList(result);
  }

  private String getFirstNamePart(String prefix, String fullName) {
    String relativeName = fullName.substring(prefix.length());
    int separatorIndex = relativeName.indexOf('.');
    return separatorIndex >= 0 ? relativeName.substring(0, separatorIndex) : relativeName;
  }

  private boolean isUseless() {
    return getChildren().size() == 0 && !hasValue();
  }

  static boolean isGosuIdentifier(String name) {
    boolean result = name.length() > 0 && isGosuIdentifierStart(name.charAt(0));
    for (int i = 1; i < name.length() && result; i++) {
      if (!isGosuIdentifierPart(name.charAt(i))) {
        result = false;
      }
    }
    return result;
  }

  private static boolean isGosuIdentifierStart(char ch) {
    return Character.isJavaIdentifierStart(ch) && ch != '$';
  }
  
  private static boolean isGosuIdentifierPart(char ch) {
    return Character.isJavaIdentifierPart(ch) && ch != '$';
  }
  
  private static String join(String head, String tail) {
    if (head.isEmpty()) {
      return tail;
    } else if (tail.isEmpty()) {
      return head;
    } else {
      return head + "." + tail;
    }
  }

  public String getPath() {
    return _path;
  }

  public PropertyNode getParent()
  {
    return _parent;
  }
}
