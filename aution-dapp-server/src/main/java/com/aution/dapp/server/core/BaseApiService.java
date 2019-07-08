package com.aution.dapp.server.core;


import java.io.IOException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.protocol.HttpClientContext;

import com.google.gson.reflect.TypeToken;

/**
 * 客户端 HTTP 服务基础类.
 *
 * @author yang.tiedang@cesgroup.com.cn
 * @since 1.0
 */
public class BaseApiService {
  private static final Log LOGGER = LogFactory.getLog(BaseApiService.class);
  protected AppContext appContext;
  protected Properties configuration;

  public BaseApiService(AppContext appContext) {
    this.appContext = appContext;
  }

  public BaseApiService(AppContext appContext, Properties configuration) {
    this(appContext);
    this.configuration = configuration;
  }

  public void setAppContext(AppContext appContext) {
    this.appContext = appContext;
  }

  public AppContext getAppContext() {
    return appContext;
  }

  public Properties getConfiguration() {
    return this.configuration;
  }

  public void setConfiguration(Properties configuration) {
    this.configuration = configuration;
  }

  /**
   * Get configuration value by key.
   *
   * @param key
   * @return
   */
  public String getConfiguration(String key) {
    if (this.configuration != null) {
      return configuration.getProperty(key);
    }
    return null;
  }

  /**
   *
   * @param post
   * @param clientContext
   * @param typeToken
   * @param <T>
   * @return
   * @throws IOException
   */
  protected <T> T doPost(HttpPost post, HttpClientContext clientContext, TypeToken<RestApiResponse<T>> typeToken)
      throws IOException {
    RestApiResponse<T> response = appContext.getHttpClient().execute(post,
        new RestApiResponseHandler<>(typeToken), clientContext);

    String code = response.getCode();
    dealWithResponseCode(code);
    return response.getData();
  }

  protected void dealWithResponseCode(String code) {
	  
	  switch(code) {
	  case ApiConstants.CODE_SUCCESS:
		  break;
	  case ApiConstants.CODE_REQUEST_EROR:
		  break;
	  case ApiConstants.CODE_PARAM_INVALID:
		  break;
	  case ApiConstants.CODE_PARAM_EROR:
		  break;
	  case ApiConstants.CODE_TIMESTAMP_EXPIRED:
		  break;
	  case ApiConstants.CODE_TOKEN_ERROR:
		  break;
	  case ApiConstants.CODE_ORDER_REPEAT:
		  break;
	  case ApiConstants.CODE_SIGN_ERROR:
		  break;
	  case ApiConstants.CODE_APPID_ERROR:
		  break;
	  case ApiConstants.CODE_USERID_NULL:
		  break;
	  case ApiConstants.CODE_TRANSACTION_EXCEPTION:
		  break;
	  case ApiConstants.CODE_CHECK_FAILED:
		  break;
	  case ApiConstants.CODE_INSUFFICIENT_BALANCE:
		  break;
	  case ApiConstants.CODE_TRANSACTION_NOT_EXIST:
		  break;
	  case ApiConstants.CODE_REFUND_FAILED:
		  break;
	  case ApiConstants.CODE_FEE_FAILED:
		  break;
	  case ApiConstants.CODE_CORRECT_FAILED:
		  break;
	  case ApiConstants.CODE_OPERATE_FREQUENTLY:
		  break;
	  case ApiConstants.CODE_TRY_AGAIN_LATER:
		  break;
	  case ApiConstants.CODE_ORDER_COMPLETED:
		  break;  
	  
	  }
	  
	  
  }
  /**
   *
   * @param get
   * @param clientContext
   * @param typeToken
   * @param <T>
   * @return
   * @throws IOException
   */
  protected <T> T doGet(HttpGet get, HttpClientContext clientContext, TypeToken<RestApiResponse<T>> typeToken) throws IOException{
    RestApiResponse<T> response = appContext.getHttpClient().execute(get,
        new RestApiResponseHandler<>(typeToken), clientContext);
    String code = response.getCode();
    dealWithResponseCode( code);

    return response.getData();
  }

  /**
   *
   * @param delete
   * @param clientContext
   * @param typeToken
   * @param <T>
   * @return
   * @throws IOException
   */
  protected <T> T doDelete(HttpDelete delete, HttpClientContext clientContext, TypeToken<RestApiResponse<T>> typeToken) throws IOException{
    RestApiResponse<T> response = appContext.getHttpClient().execute(delete,
        new RestApiResponseHandler<>(typeToken), clientContext);

    String code = response.getCode();
    dealWithResponseCode( code);

    return response.getData();
  }




  /**
   *
   * @param put
   * @param clientContext
   * @param typeToken
   * @param <T>
   * @return
   * @throws IOException
   */
  protected <T> T doPut(HttpPut put, HttpClientContext clientContext, TypeToken<RestApiResponse<T>> typeToken)
      throws IOException {
    RestApiResponse<T> response = appContext.getHttpClient().execute(put,
        new RestApiResponseHandler<>(typeToken), clientContext);

    String code = response.getCode();
    dealWithResponseCode( code);
    return response.getData();
  }
}
