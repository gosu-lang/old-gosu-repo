/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.reflect.ITypeRef;
import gw.lang.reflect.gs.IGosuFragment;
import gw.lang.reflect.gs.IGosuProgram;
import gw.util.Predicate;
import gw.util.cache.WeakFqnCache;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class TypeHunterKiller {
  private WeakFqnCache<AbstractTypeRef> _fqnCache;
  private Thread _hunterThread;
  private int _iTypesVisited;
  private boolean _bHunting;
  private int _iTypeCountLimit;
  private int _iAgeLimitInSeconds;
  private long _lLastHuntStarted;
  private List<ITypeRef> _typesToKill;
  private boolean _bLimitlessHunt;

  public TypeHunterKiller( WeakFqnCache<AbstractTypeRef> fqnCache, int iAgeLimitInSeconds, int iTypeCountLimit ) {
    _fqnCache = fqnCache;
    _iAgeLimitInSeconds = iAgeLimitInSeconds;
    _iTypeCountLimit = iTypeCountLimit;
  }

  public List<ITypeRef> hunt( boolean bLimitlessHunt ) {
    synchronized( this ) {
      setLimitlessHunt( bLimitlessHunt );
      setHunting( true );
      _typesToKill = new ArrayList<ITypeRef>();
      if( _hunterThread == null ) {
        createAndStartHunterThread();
      }
      else {
        _lLastHuntStarted = System.nanoTime();
        notifyAll(); // resume hunting
      }
      while( isHunting() ) {
        try {
          wait();
        }
        catch( InterruptedException e ) {
          throw new RuntimeException( e );
        }
      }
      return getTypesToKill();
    }
  }

  private void createAndStartHunterThread() {
    _hunterThread = new Thread( new Runnable() {
      public void run() {
        releaseTheHounds();
      }
    });
    _hunterThread.setDaemon( true );
    _hunterThread.start();
  }

  private void releaseTheHounds() {
    synchronized( this ) {
      _lLastHuntStarted = System.nanoTime();
      //noinspection InfiniteLoopStatement
      while( true ) {
        huntForOldTypes();
        waitForNextHunt();
      }
    }
  }

  private void huntForOldTypes() {
    _fqnCache.visitDepthFirst(
      new Predicate<AbstractTypeRef>() {
        public boolean evaluate( AbstractTypeRef typeRef ) {
          _iTypesVisited++;
          waitIfTypeCountLimit();
          return maybeAddTypeToKill( typeRef );
        }
      } );
  }

  private boolean maybeAddTypeToKill( AbstractTypeRef typeRef ) {
    if( typeRef != null && !typeRef.isStale() && (isLimitlessHunt() || typeRef.isOlderThanSeconds( _iAgeLimitInSeconds )) ) {
      String typeName = typeRef.getName();
      if( !typeName.startsWith( IGosuFragment.FRAGMENT_PACKAGE ) &&
          !typeName.startsWith( IGosuProgram.PACKAGE_PLUS_DOT ) &&
          !typeName.startsWith( "unable.to.find.name" ) &&
          !typeName.equals( "transient" ) &&
          !typeName.contains( "<" ) &&
          !typeName.contains( "[" ) &&
          TypeLoaderAccess.instance().getDefaultType( typeName ) == null ) {
        _typesToKill.add( typeRef );
        //System.out.println( "KILL: " + typeName );
      }
    }
    return true;
  }

  private void resetForNextHunt() {
    _iTypesVisited = 0;
    setHunting( false );
  }

  private void waitIfTypeCountLimit() {
    if( !isLimitlessHunt() && _iTypesVisited > _iTypeCountLimit ) {
      waitForNextHunt();
    }
  }

  private void waitIfTimeOut() {
    if( System.nanoTime() - _lLastHuntStarted > 100000 ) { // 1/10 millisecond
      //System.out.println( "TIMED OUT" );
      waitForNextHunt();
    }
  }

  private void waitForNextHunt() {
    resetForNextHunt();
    try {
      notifyAll();
      wait();
    }
    catch( InterruptedException e ) {
      throw new RuntimeException( e );
    }
  }

  private void setHunting( boolean bHunting ) {
    _bHunting = bHunting;
  }
  public synchronized boolean isHunting() {
    return _bHunting;
  }

  public final int getAgeLimitInSeconds() {
    return _iAgeLimitInSeconds;
  }

  public final int getTypeCountLimit() {
    return _iTypeCountLimit;
  }

  public List<ITypeRef> getTypesToKill() {
    return _typesToKill;
  }

  public boolean isLimitlessHunt() {
    return _bLimitlessHunt;
  }
  public void setLimitlessHunt( boolean bLimitlessHunt ) {
    _bLimitlessHunt = bLimitlessHunt;
  }
}
