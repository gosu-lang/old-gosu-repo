/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.test;

import gw.lang.reflect.TypeSystem;
import gw.util.GosuObjectUtil;
import gw.testharness.DoNotRunTest;
import gw.xml.simple.SimpleXmlNode;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ctucker
 */
public class TestMetadata implements ITestMetadata {

  private String _name;
  private HashMap<String, String> _attributes = new HashMap<String, String>();
  private boolean _doNotRunTest;

  public TestMetadata(Annotation testAnnotation) {
    Class<? extends Annotation> annotationType = testAnnotation.annotationType();
    for (Annotation metaAnnotation : annotationType.getAnnotations()) {
      if (metaAnnotation instanceof DoNotRunTest) {
        _doNotRunTest = true;
      }
    }
    for (Method method : TypeSystem.getDeclaredMethods( annotationType )) {
      try {
        addAttribute(method.getName(), GosuObjectUtil.toString(method.invoke(testAnnotation)));
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      } catch (InvocationTargetException e) {
        e.printStackTrace();
      } catch (Exception e) {
        // Happens if these things are out of order...
        throw new RuntimeException("Got weird error fetching attribute '" + method.getName() + "' with annotation '" +
            testAnnotation + "'");
      }
    }
    _name = annotationType.getName();
  }

  private TestMetadata() {
  }

  @Override
  public String getName() {
    return _name;
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof TestMetadata && _name.equals(((TestMetadata) obj).getName());
  }

  public void addAttribute(String name, String value) {
    _attributes.put(name, value);
  }

  @Override
  public HashMap<String, String> getAttributes() {
    return _attributes;
  }

  public boolean shouldNotRunTest() {
    return _doNotRunTest;
  }

  public SimpleXmlNode serializeToXml() {
    SimpleXmlNode root = new SimpleXmlNode("TestMetadata");
    root.getAttributes().put("name", _name);
    root.getAttributes().put("doNotRunTest", "" + _doNotRunTest);
    for (Map.Entry<String, String> entry : _attributes.entrySet()) {
      SimpleXmlNode child = new SimpleXmlNode("Attribute");
      child.getAttributes().put("key", entry.getKey());
      child.getAttributes().put("value", entry.getValue());
      root.getChildren().add(child);
    }
    return root;
  }

  public static TestMetadata deserializeXml(SimpleXmlNode xml) {
    TestMetadata testMD = new TestMetadata();
    testMD._name = xml.getAttributes().get("name");
    testMD._doNotRunTest = Boolean.valueOf(xml.getAttributes().get("doNotRunTest"));
    for (SimpleXmlNode child : xml.getChildren()) {
      testMD._attributes.put(child.getAttributes().get("key"), child.getAttributes().get("value"));
    }
    return testMD;
  }
}
