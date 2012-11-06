/*
 * Copyright 2012. Guidewire Software, Inc.
 */

package gw.internal.gosu.compiler.protocols.gosuclass.osgi;

import aQute.bnd.annotation.component.Component;
import gw.internal.gosu.compiler.protocols.gosuclass.Handler;
import org.osgi.service.url.URLStreamHandlerService;
import org.osgi.service.url.URLStreamHandlerSetter;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

/**
 * gosuclass URL handler service for OSGi environement.
 */
@Component(properties = {"url.handler.protocol=gosuclassosgi"})
public class OSGiHandler extends Handler implements URLStreamHandlerService {

  private final ThreadLocal<URLStreamHandlerSetter> holder = new ThreadLocal<URLStreamHandlerSetter>();

  @Override
  public void parseURL(URLStreamHandlerSetter setter, URL u, String spec, int start, int limit) {
    holder.set(setter);
    try {
     super.parseURL(u, spec, start, limit);
    } finally {
      holder.remove();
    }
  }

  @Override
  public String toExternalForm(URL u) {
    return super.toExternalForm(u);
  }

  @Override
  public URLConnection openConnection(URL u, Proxy p) throws IOException {
    return super.openConnection(u, p);
  }

  @Override
  public int getDefaultPort() {
    return super.getDefaultPort();
  }

  @Override
  public boolean sameFile(URL u1, URL u2) {
    return super.sameFile(u1, u2);
  }

  @Override
  public InetAddress getHostAddress(URL u) {
    return super.getHostAddress(u);
  }

  @Override
  public boolean hostsEqual(URL u1, URL u2) {
    return super.hostsEqual(u1, u2);
  }

  @Override
  public int hashCode(URL u) {
    return super.hashCode(u);
  }

  @Override
  public boolean equals(URL u1, URL u2) {
    return super.equals(u1, u2);
  }

  @Override
  public URLConnection openConnection(URL u) throws IOException {
    return super.openConnection(u);
  }

  @Override
  protected void setURL(URL u, String protocol, String host, int port, String authority, String userInfo, String path, String query, String ref) {
    URLStreamHandlerSetter setter = holder.get();
    setter.setURL(u, protocol, host, port, authority, userInfo, path, query, ref);
  }

  @Override
  protected void setURL(URL u, String protocol, String host, int port, String file, String ref) {
    URLStreamHandlerSetter setter = holder.get();
    setter.setURL(u, protocol, host, port, file, ref);
  }
}
