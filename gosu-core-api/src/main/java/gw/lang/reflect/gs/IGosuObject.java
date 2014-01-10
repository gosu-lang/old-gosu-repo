/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.lang.reflect.gs;

import gw.lang.Deprecated;
import gw.lang.PublishInGosu;
import gw.lang.reflect.IType;

@PublishInGosu
public interface IGosuObject
{
  @Deprecated(value="Use the 'typeof' operator in Gosu instead. 'obj.IntrinsicType' becomes 'typeof obj'.")
  IType getIntrinsicType();

  //
  // Methods cooresponding with java.lang.Object
  //

  @gw.lang.InternalAPI @java.lang.Deprecated/* for @InternalAPI */
  public String toString();

  @gw.lang.InternalAPI @java.lang.Deprecated/* for @InternalAPI */
  public int hashCode();

  @gw.lang.InternalAPI @java.lang.Deprecated/* for @InternalAPI */
  public boolean equals( Object o );

}
