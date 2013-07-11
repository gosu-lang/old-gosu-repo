/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.plugin.ij.lang.psi.impl;

import com.google.common.collect.Maps;
import com.intellij.openapi.vfs.VirtualFile;
import gw.lang.reflect.module.IModule;
import gw.plugin.ij.util.IDEAUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Map;

public class GosuClassParseDataCache {
  private static final Map<IModule, Map<String, GosuClassParseData>> _cache = Collections.synchronizedMap(Maps.<IModule, Map<String, GosuClassParseData>>newHashMap());

  private static boolean isValid(@NotNull GosuClassParseData parseData) {
    return _cache.containsKey(parseData.getModule());
  }

  @Nullable
  public static GosuClassParseData getParseData(@Nullable GosuClassParseData parseData, VirtualFile virtualFile, IModule module) {
    if (parseData == null || !isValid(parseData)) {
      return createParseData(virtualFile, module);
    } else {
      return parseData;
    }
  }

  private static GosuClassParseData createParseData(VirtualFile file, IModule module) {
    Map<String, GosuClassParseData> moduleCache = _cache.get(module);
    if (moduleCache == null) {
      moduleCache = Collections.synchronizedMap(Maps.<String, GosuClassParseData>newHashMap());
      _cache.put(module, moduleCache);
    }

    final String qualifiedName = IDEAUtil.getSourceQualifiedName(file, module);
    GosuClassParseData data = moduleCache.get(qualifiedName);
    if (data == null) {
      data = new GosuClassParseData(module);
      moduleCache.put(qualifiedName, data);
    }

    return data;
  }

  public static void clear() {
    _cache.clear();
  }

  public static void clear(IModule module) {
    _cache.remove(module);
  }
}
