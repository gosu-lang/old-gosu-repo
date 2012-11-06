/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.internal.gosu.module;

import gw.config.CommonServices;
import gw.fs.IDirectory;
import gw.internal.gosu.parser.ExecutionEnvironment;
import gw.lang.reflect.module.IExecutionEnvironment;
import gw.lang.reflect.module.IJreModule;

import java.io.File;
import java.net.MalformedURLException;
import java.util.List;

/**
 */
public class JreModule extends Module implements IJreModule
{
  private Object _nativeSDK;

  public JreModule( ExecutionEnvironment execEnv, boolean includesGosuCoreAPI )
  {
    super( execEnv, JRE_MODULE_NAME, includesGosuCoreAPI );
  }

  @Override
  public Object getNativeSDK() {
    return _nativeSDK;
  }

  public void setNativeSDK(Object nativeSDK) {
    _nativeSDK = nativeSDK;
  }

  @Override
  public void setSourcePath( List<IDirectory> path ) {
    super.setSourcePath( path );
    maybeAddTestSourcePathFromVmClasspath( _explicitSourceRoots );
  }

  @Override
  public void setJavaClasspathFromFiles( List<String> paths ) {
    super.setJavaClasspathFromFiles( paths );
    maybeAddTestClasspathFromVmClasspath();

  }

  private void maybeAddTestClasspathFromVmClasspath() {
    IExecutionEnvironment execEnv = getExecutionEnvironment();
    if( execEnv.getProject().isHeadless() ) {
      String classpath = System.getProperty( "java.class.path", "" );
      for( String path: classpath.split( File.pathSeparator ) ) {
        if( !_classpath.contains( path ) ) {
          _classpath.add( path );
        }
      }
    }
  }

  private void maybeAddTestSourcePathFromVmClasspath( List<IDirectory> src ) {
    IExecutionEnvironment execEnv = getExecutionEnvironment();
    if( execEnv.getProject().isHeadless() ) {
      String classpath = System.getProperty( "java.class.path", "" );
      for( String path: classpath.split( File.pathSeparator ) ) {
        try {
          IDirectory srcPath = CommonServices.getFileSystem().getIDirectory( new File( path ).toURI().toURL() );
          if( !src.contains( srcPath ) ) {
            src.add( srcPath );
          }
        }
        catch( MalformedURLException e ) {
          throw new RuntimeException( e );
        }
      }
    }
  }

}
