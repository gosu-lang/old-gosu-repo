/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.plugin.ij.core;

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.extensions.ExtensionPoint;
import com.intellij.openapi.extensions.Extensions;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.refactoring.rename.RenameHandler;
import com.intellij.refactoring.rename.inplace.MemberInplaceRenameHandler;
import gw.plugin.ij.lang.parser.GosuCodeParserDefinition;
import gw.plugin.ij.sdk.ISDKCreator;
import gw.plugin.ij.sdk.SDKCreatorExtensionBean;
import gw.plugin.ij.util.GosuBundle;
import gw.plugin.ij.util.IDEAUtil;
import org.jetbrains.annotations.NotNull;

import java.net.URL;

public class GosuAppComponent implements ApplicationComponent {
  public GosuAppComponent() {
    new GosuCodeParserDefinition(); // force loading the class.
  }

  @Override
  public void initComponent() {
    detectAltRtJar();
    takeCareOfSDKs();
    removeJavaMemberInplaceRenameHandler();
  }

  private void removeJavaMemberInplaceRenameHandler() {
    ExtensionPoint<RenameHandler> renameExtPt = Extensions.getRootArea().getExtensionPoint( RenameHandler.EP_NAME );
    for( RenameHandler handler: renameExtPt.getExtensions() ) {
      if( handler.getClass() == MemberInplaceRenameHandler.class ) {
        // PL-20646
        // Don't support in-place refactoring of a Java getter/setter method *from* inside a Gosu file,
        // it's messed up wrt the property name. The dialog-based refactor works. So we register our own
        // (GosuMemberInplaceRenameHandler) in plugin.xml that delegates to the Java one, with the only
        // difference being the prevention of in-place refactor while inside a Gosu file.
        renameExtPt.unregisterExtension( handler );
      }
    }
  }

  private void takeCareOfSDKs() {
    for (SDKCreatorExtensionBean extension : Extensions.getExtensions(SDKCreatorExtensionBean.EP_NAME)) {
      extension.getCreator().createSDK();
    }
  }

  private static void detectAltRtJar() {
    String aClassInAltRtJar = "java/util/HashMap$FrontCache.class";
    URL res = ClassLoader.getSystemResource(aClassInAltRtJar);
    if (res != null) {
      String path = res.getFile();
      int idx = path.indexOf("!/" + aClassInAltRtJar);
      String file = idx >= 0 ? path.substring(0, idx) : path;
      IDEAUtil.showError(GosuBundle.message("error.system_classpath_problem"),
          GosuBundle.message("error.system_classpath_problem.description", file));
    }
  }

  @Override
  public void disposeComponent() {
  }

  @NotNull
  @Override
  public String getComponentName() {
    return "Gosu App Component";
  }
}
