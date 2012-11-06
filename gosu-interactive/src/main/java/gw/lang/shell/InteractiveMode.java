package gw.lang.shell;

import gw.lang.Gosu;
import gw.lang.mode.GosuMode;
import gw.lang.mode.RequiresInit;

/**
 *  Copyright 2012 Guidewire Software, Inc.
 */
@RequiresInit
public class InteractiveMode extends GosuMode {

  @Override
  public int getPriority() {
    return GOSU_MODE_PRIORITY_INTERACTIVE;
  }

  @Override
  public boolean accept() {
    return _argInfo.consumeArg(Gosu.ARGKEY_INTERACTIVE);
  }

  @Override
  public int run() {
    new InteractiveShell(true).run();
    return 0;
  }
}
