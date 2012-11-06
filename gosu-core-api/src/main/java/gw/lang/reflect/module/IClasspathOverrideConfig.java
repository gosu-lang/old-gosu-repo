/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.lang.reflect.module;

import gw.fs.IDirectory;
import gw.lang.PublishInGosu;
import gw.lang.Scriptable;

@PublishInGosu
public interface IClasspathOverrideConfig
{

  /**
   * Returns a context token for the given file that the config will receive when it is
   * asked if another version of this file should override this version.
   */
  @Scriptable
  public String getConfigModule(IDirectory path);

  /**
   * @param possibleOverride
   * @param contextToOverride
   * @return true if a file represented by the possibleOverride token should override the token represented by
   *         contextToOverride (the tokens are modules in the default Guidewire implementation)
   */
  @Scriptable
  public boolean shouldOverride( String possibleOverride, String contextToOverride );

}

