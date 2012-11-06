package gw.test;

import gw.lang.UnstableAPI;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.IType;
import gw.lang.reflect.Modifier;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.java.IJavaBackedType;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@UnstableAPI
public class TestSpec implements Comparable<TestSpec> {
  private String _testType;
  private String[] _methods;

  public TestSpec(String type, String... methods) {
    _testType = type;
    _methods = methods;
  }

  public int compareTo(TestSpec o) {
    return this._testType.compareTo(o._testType);
  }

  public boolean runAllMethods() {
    return _methods == null || _methods.length == 0;
  }

  public String[] getMethods() {
    if (runAllMethods()) {
      return extractTestMethods(getTestType());
    } else {
      return _methods;
    }
  }

  public IType getTestType() {
    return TypeSystem.getByFullName(_testType);
  }

  public String getTestTypeName() {
    return _testType;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    TestSpec testSpec = (TestSpec) o;

    if (!Arrays.equals(_methods, testSpec._methods)) {
      return false;
    }
    if (_testType != null ? !_testType.equals(testSpec._testType) : testSpec._testType != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int result;
    result = (_testType != null ? _testType.hashCode() : 0);
    result = 31 * result + (_methods != null ? Arrays.hashCode(_methods) : 0);
    return result;
  }

  public static String[] extractTestMethods(IType testType) {
    ArrayList<String> methodNames = new ArrayList<String>();
    if (testType instanceof IJavaBackedType) {
      Method[] methods = ((IJavaBackedType) testType).getBackingClass().getMethods();
      for (Method methodInfo : methods) {
        // TODO - AHK
        if (isTestMethod(methodInfo) /*&& shouldIncludeTestMethod(methodInfo)*/) {
          methodNames.add(methodInfo.getName());
        }
      }
    } else {
      List<? extends IMethodInfo> methodInfos = testType.getTypeInfo().getMethods();
      for (IMethodInfo methodInfo : methodInfos) {
        if (isTestMethod(methodInfo)) {
          methodNames.add(methodInfo.getDisplayName());
        }
      }
    }
    return methodNames.toArray(new String[methodNames.size()]);
  }

  private static boolean isTestMethod(Method method) {
    return Modifier.isPublic(method.getModifiers()) && method.getParameterTypes().length == 0 &&
        !Modifier.isStatic(method.getModifiers()) && method.getName().startsWith("test");
  }

  private static boolean isTestMethod(IMethodInfo methodInfo) {
    return methodInfo.isPublic() && methodInfo.getParameters().length == 0 &&
        !methodInfo.isStatic() && methodInfo.getName().startsWith("test");
  }
}