package com.aution.dapp.server.core;

import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;

import com.aution.dapp.server.core.internal.DBaseApiService;
import com.aution.dapp.server.utils.ConfigUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * AppClient 是整个客户端的入口. 其职责在于： - 创建和维护的 HttpClient 对象； - 为每个登录用户创建和管理 HttpContext 对象； -
 * 创建和获取必要的客户端服务；
 *
 */
public class AppClient {
  private static final Log LOGGER = LogFactory.getLog(AppClient.class);
  private static AppClient instance = new AppClient();
  private final AppContext appContext;
  private Properties configuration;
  private DBaseApiService dBaseApiService;
  private String accessToken;
  
  /*
   * Private constructor.
   */
  private AppClient() {
    // build the httpClient object.
    HttpClient httpClient = HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy()).build();
    Protocol.registerProtocol("https",
        new Protocol("https", new MySSLProtocolSocketFactory(), 443));
    appContext = new AppContext(httpClient);
    try {
      // load the configuration files.
      configuration = ConfigUtils.fromResourcePath(getClass(), "/dapp.properties");
      dBaseApiService = new DBaseApiService(appContext, configuration);
      accessToken = dBaseApiService.accessToken();
    } catch (IOException ioe) {
      LOGGER.error("Failed to load the configuration file.", ioe);
      throw new RuntimeException("Failed to load the configuration file.", ioe);
    }
  }

  /** Do setup. - */
  public void start() {
    // do nothing for now.
  }

  /** Do cleanup. - close the httpClient - clean up the context */
  public void close() {
    appContext.close();
  }

  /**
   * Get the instance of the AppClient singleton object.
   *
   * @return the instance of AppClient.
   */
  public static AppClient getInstance() {
    return instance;
  }

  /**
   * Get the initialized HttpClient object.
   *
   * @return the httpClient object.
   */
  public HttpClient getHttpClient() {
    return appContext.getHttpClient();
  }

  public DBaseApiService getdBaseApiService() {
	return dBaseApiService;
  }

  public Properties getConfiguration() {
    return this.configuration;
  }

  public String getAccessToken() {
	return accessToken;
  }

  
}
