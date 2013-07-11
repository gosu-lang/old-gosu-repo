/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.plugin.ij.sdk;

import com.intellij.openapi.projectRoots.Sdk;

public class DefaultGosuSDKCreator implements ISDKCreator {

  @Override
  public Sdk createSDK() {
    return GosuSdkUtils.initDefaultGosuSDK();
  }

}
