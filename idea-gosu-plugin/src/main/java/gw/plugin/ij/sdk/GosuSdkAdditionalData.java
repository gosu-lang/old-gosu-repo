/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.plugin.ij.sdk;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.projectRoots.ProjectJdkTable;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.projectRoots.SdkAdditionalData;
import com.intellij.openapi.projectRoots.SdkModel;
import gw.lang.GosuVersion;
import org.jdom.Element;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GosuSdkAdditionalData implements SdkAdditionalData {
  @NonNls
  private static final String JDK = "jdk";

  @NonNls
  private static final String GOSU_VERSION = "gosuVersion";

  @Nullable
  private String _javaSdkName;
  @Nullable
  private Sdk _javaSdk;
  private final GosuVersion _gosuVersion;

  public GosuSdkAdditionalData(@NotNull Sdk gosuSdk, Sdk javaSdk, @NotNull GosuVersion gosuVersion) {
    this._javaSdk = javaSdk;
    this._gosuVersion = gosuVersion;
  }

  public GosuSdkAdditionalData(@NotNull Sdk gosuSdk, @NotNull Element element) {
    this._javaSdkName = element.getAttributeValue(JDK);
    String versionAttr = element.getAttributeValue(GOSU_VERSION);
    this._gosuVersion = versionAttr != null && versionAttr.length() > 0 ? GosuVersion.parse(versionAttr) : null;
  }

  @NotNull
  public Object clone() throws CloneNotSupportedException {
    GosuSdkAdditionalData data = (GosuSdkAdditionalData) super.clone();
    return data;
  }

  @Nullable
  public Sdk getJavaSdk() {
    final ProjectJdkTable table = ProjectJdkTable.getInstance();
    if (_javaSdk == null) {
      if (_javaSdkName != null) {
        _javaSdk = table.findJdk(_javaSdkName);
        _javaSdkName = null;
      } else {
        for (Sdk jdk : table.getAllJdks()) {
          if (GosuSdkUtils.isApplicableJdk(jdk)) {
            _javaSdk = jdk;
            break;
          }
        }
      }
    }
    return _javaSdk;
  }

  public void setJavaSdk(final Sdk javaSdk) {
    _javaSdk = javaSdk;
  }

  public void save(@NotNull Element element) {
    final Sdk sdk = getJavaSdk();
    if (sdk != null) {
      element.setAttribute(JDK, sdk.getName());
    }
    if (_gosuVersion != null) {
      element.setAttribute(GOSU_VERSION, _gosuVersion.toString());
    }
  }

  public GosuVersion getGosuVersion() {
    return _gosuVersion;
  }

  @NotNull
  public String getVersion() {
    return _gosuVersion.toString() + " (" + (_javaSdk == null ? "" : _javaSdk.getVersionString()) + ")";
  }
}
