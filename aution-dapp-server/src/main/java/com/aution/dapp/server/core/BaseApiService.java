package com.aution.dapp.server.core;


import java.io.IOException;
import java.util.Properties;

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
  protected <T> RestApiResponse<T> doPost(HttpPost post, HttpClientContext clientContext, TypeToken<RestApiResponse<T>> typeToken)
      throws ApiException,IOException {
    RestApiResponse<T> response = appContext.getHttpClient().execute(post,
        new RestApiResponseHandler<>(typeToken), clientContext);

    
    if (response.getStatusCode() != HttpStatus.SC_OK) {
        throw new ApiException(response.getStatusCode(),response.getHeaders(),response.getMsg());
    }
    String code = response.getCode();
    dealWithResponseCode(code,response);
    return response;
  }

  protected <T> void dealWithResponseCode(String code,RestApiResponse<T> response) throws ApiException {
	  
	  switch(code) {
	  case ApiConstants.CODE_SUCCESS:
		  break;
	  case ApiConstants.CODE_REQUEST_EROR:
		  throw new ApiException(Integer.parseInt(response.getCode()),response.getHeaders(),response.toString());
	  case ApiConstants.CODE_PARAMS_ERROR:
		throw new ApiException(Integer.parseInt(response.getCode()),response.getHeaders(),response.toString());
	  case ApiConstants.CODE_SERVER_ERROR:
		  throw new ApiException(Integer.parseInt(response.getCode()),response.getHeaders(),response.toString());
	  case ApiConstants.CODE_ACCOUNT_NOT_EXSIT:
		  throw new ApiException(Integer.parseInt(response.getCode()),response.getHeaders(),response.toString());
	  case ApiConstants.CODE_TOKEN_ERROR:
		  throw new ApiException(Integer.parseInt(response.getCode()),response.getMsg(),response.getHeaders(),response.toString());
	  case ApiConstants.CODE_INSUFFICIENT_BALANCE:
		  throw new ApiException(Integer.parseInt(response.getCode()),response.getMsg(),response.getHeaders(),response.toString());
	  case ApiConstants.CODE_SIGN_ERROR:
		  throw new ApiException(Integer.parseInt(response.getCode()),response.getMsg(),response.getHeaders(),response.toString());
	  case ApiConstants.CODE_APPID_ERROR:
		  throw new ApiException(Integer.parseInt(response.getCode()),response.getHeaders(),response.toString());
	  case ApiConstants.CODE_CANT_REFUND:
		  throw new ApiException(Integer.parseInt(response.getCode()),response.getMsg(),response.getHeaders(),response.toString());
	  case ApiConstants.CODE_TRANSACTION_EXCEPTION:
		  throw new ApiException(Integer.parseInt(response.getCode()),response.getMsg(),response.getHeaders(),response.toString());
	  case ApiConstants.CODE_AUTH_INVALID:
		  throw new ApiException(Integer.parseInt(response.getCode()),response.getMsg(),response.getHeaders(),response.toString());
	  case ApiConstants.CODE_TRANSACTION_NOT_EXIST:
		  throw new ApiException(Integer.parseInt(response.getCode()),response.getMsg(),response.getHeaders(),response.toString());
		case ApiConstants.CODE_TIMESTAMP_EXPIRED:
          throw new ApiException(Integer.parseInt(response.getCode()),response.getMsg(),response.getHeaders(),response.toString());
       case ApiConstants.CODE_INSUFFICIENT_BALANCE_1:
              throw new ApiException(Integer.parseInt(response.getCode()),response.getMsg(),response.getHeaders(),response.toString());
       default:
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
    dealWithResponseCode( code,response);

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

    if (response.getStatusCode() != HttpStatus.SC_OK) {
        throw new ApiException(response.getStatusCode(),response.getHeaders(),"");
    }

    String code = response.getCode();
    dealWithResponseCode( code,response);

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

    if (response.getStatusCode() != HttpStatus.SC_OK) {
        throw new ApiException(response.getStatusCode(),response.getHeaders(),"");
    }

    String code = response.getCode();
    dealWithResponseCode( code,response);
    return response.getData();
  }
}
