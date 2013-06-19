/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.internal.gosu.loader;

import gw.internal.gosu.compiler.GosuClassLoader;
import gw.internal.gosu.ir.TransformingCompiler;
import gw.internal.gosu.parser.GosuClass;
import gw.internal.gosu.parser.IGosuClassInternal;
import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.gs.ICompilableType;
import gw.lang.reflect.java.IJavaBackedType;
import gw.lang.reflect.module.TypeSystemLockHelper;

/**
 * FIXME: Duplicates {@link gw.internal.gosu.compiler.protocols.gosuclass.GosuClassesUrlConnection}
 */
public class Loader {
  private static final String[] JAVA_NAMESPACES_TO_IGNORE = {
          "java.", "javax.", "sun."
  };

  public static Object getBytesOrClass(String strType) {
    boolean bInterfaceAnnotationMethods = false;
    ICompilableType type = null;

    if (!ignoreJavaClass(strType)) {
      strType = strType.replace('$', '.');
      if (strType.endsWith('.' + GosuClass.ANNOTATION_METHODS_FOR_INTERFACE_INNER_CLASS)) {
        bInterfaceAnnotationMethods = true;
        strType = strType.substring(0, strType.lastIndexOf('.'));
      }
      type = maybeAssignGosuType(strType);
    }

    if (type != null) {
      if (bInterfaceAnnotationMethods) {
        return TransformingCompiler.compileInterfaceMethodsClass((IGosuClassInternal) type, false);
      } else {
        return GosuClassLoader.instance().getBytes(type);
      }
    }

    // FIXME: Copied from PluginContainer. Why do we need to treat them specially?
    if (strType.startsWith("com.guidewire.commons.metadata.proxy._generated.iface.")) {
      strType = "entity." + strType.substring(strType.lastIndexOf('.') + 1);
      IType type2 = TypeSystem.getByFullNameIfValid(strType);
      if (type2 instanceof IJavaBackedType) {
        return ((IJavaBackedType) type2).getBackingClass();
      }
    }
    return null;
  }

  private static ICompilableType maybeAssignGosuType(String strType) {
    ClassLoader loader = TypeSystem.getGosuClassLoader().getActualLoader();
    TypeSystemLockHelper.getTypeSystemLockWithMonitor(loader);
    try {
      IType type = TypeSystem.getByFullNameIfValid(strType);
      if (type instanceof ICompilableType) {
        return (ICompilableType) type;
      }
    } finally {
      TypeSystem.unlock();
    }
    return null;
  }

  private static boolean ignoreJavaClass(String strClass) {
    for (String namespace : JAVA_NAMESPACES_TO_IGNORE) {
      if (strClass.startsWith(namespace)) {
        return true;
      }
    }
    return false;
  }
}
