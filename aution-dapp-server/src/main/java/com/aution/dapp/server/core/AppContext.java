package com.aution.dapp.server.core;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.HttpClient;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;

import com.google.common.base.Strings;

/**
 * .
 * 应用上下文类组合 HttpClient 和 HttpContext 实例对象。
 *
 */
public class AppContext {
  private static final Log LOGGER = LogFactory.getLog(AppContext.class);
  private final HttpClient httpClient;
  private final Map<String, HttpClientContext> contextMap;

  public AppContext() {
    this(null);
  }

  public AppContext(HttpClient httpClient) {
    if (httpClient != null) {
      this.httpClient = httpClient;
    } else {
      this.httpClient =
          HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy()).build();
    }
    contextMap = new ConcurrentHashMap<>();
  }

  /**
   * Get the initialized HttpClient object.
   *
   * @return the httpClient object.
   */
  public HttpClient getHttpClient() {
    return httpClient;
  }

  /**
   * 根据上下文标示获取 HttpClientContext 对象.
   *
   * @param key the key
   * @return
   */
  public HttpClientContext getHttpContext(String key) {
    return contextMap.get(key);
  }

  /**
   * 添加新的 HttpContext.
   *
   * @param key
   * @param context
   */
  public void putIfAbsentHttpContext(String key, HttpClientContext context) {
    if (Strings.isNullOrEmpty(key) || context == null) {
      throw new IllegalArgumentException("Arguments token and context are required.");
    }
    contextMap.putIfAbsent(key, context);
  }

  /**
   * Remove the httpContext object.
   *
   * @param key
   */
  public void removeContext(String key) {
    if (!Strings.isNullOrEmpty(key)) {
      contextMap.remove(key);
    }
  }

  /**
   * Close the HttpClient and cleanup the context.
   */
  public void close() {
    try {
      if (httpClient instanceof CloseableHttpClient) {
        ((CloseableHttpClient)httpClient).close();
      }
    } catch (IOException ioe) {
      LOGGER.warn("Failed to close the client.", ioe);
    }
    contextMap.clear();
  }

}
