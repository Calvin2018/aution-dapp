package com.aution.dapp.server.core;

import java.util.Map;

/**
 * API response returned by API call.
 *
 * @param <T> The type of data that is deserialized from response body
 */
public class ApiResponse<T> {
  private final int code;
  private final Map<String, String> headers;
  private final T data;

  /**
   * .
   *
   * @param statusCode The status code of HTTP response
   * @param headers    The headers of HTTP response
   */
  public ApiResponse(int code, Map<String, String> headers) {
    this(code, headers, null);
  }

  /**
   * .
   *
   * @param statusCode The status code of HTTP response
   * @param headers    The headers of HTTP response
   * @param data       The object deserialized from response body
   */
  public ApiResponse(int code, Map<String, String> headers, T data) {
    this.code = code;
    this.headers = headers;
    this.data = data;
  }

  /**
   * Get the http status code.
   *
   * @return the http status code
   */
  public int getStatusCode() {
    return code;
  }

  /**
   * Get http headers.
   *
   * @return the headers
   */
  public Map<String, String> getHeaders() {
    return headers;
  }

  /**
   * 获取响应中 Json 结构中的 data 数据 (已经转换领域模型类型 T).
   *
   * @return the data field of the json structure with type T.
   */
  public T getData() {
    return data;
  }
}
