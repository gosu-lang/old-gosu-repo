/*
 * Copyright 2013 Guidewire Software, Inc.
 */

package gw.lang.reflect.interval;

import java.util.Iterator;

/**
 */
public abstract class AbstractLongIterator implements Iterator<Long>
{
  abstract public long nextLong();
}
