package com.aution.dapp.server.core;

import java.io.IOException;
import java.util.Map;

/**
 * 自定义异常类，用于处理 Rest API 调用异常.
 *
 * 
 */
public class ApiException extends IOException {
  private int code = 0;
  private Map<String, String> responseHeaders = null;
  private String responseBody = null;

  /**
   * Default constructor.
   *
   */
  public ApiException() {}

  /**
   * .
   *
   * @param throwable
   */
  public ApiException(Throwable throwable) {
    super(throwable);
  }

  /**
   * .
   *
   * @param message
   */
  public ApiException(String message) {
    super(message);
  }

  /**
   * .
   *
   * @param message message
   * @param throwable throwable
   * @param statusCode code
   */
  public ApiException(
      String message,
      Throwable throwable,
      int code,
      Map<String, String> responseHeaders,
      String responseBody) {
    super(message, throwable);
    this.code = code;
    this.responseHeaders = responseHeaders;
    this.responseBody = responseBody;
  }

  /**
   * .
   *
   * @param message
   * @param statusCode
   * @param responseHeaders
   * @param responseBody
   */
  public ApiException(
      String message, int code, Map<String, String> responseHeaders, String responseBody) {
    this(message, (Throwable) null, code, responseHeaders, responseBody);
  }

  /**
   * .
   *
   * @param message
   * @param throwable
   * @param statusCode
   * @param responseHeaders
   */
  public ApiException(
      String message, Throwable throwable, int statusCode, Map<String, String> responseHeaders) {
    this(message, throwable, statusCode, responseHeaders, null);
  }

  /**
   * .
   *
   * @param statusCode
   * @param responseHeaders
   * @param responseBody
   */
  public ApiException(int code, Map<String, String> responseHeaders, String responseBody) {
    this((String) null, (Throwable) null, code, responseHeaders, responseBody);
  }

  /**
   * .
   *
   * @param statusCode
   * @param message
   */
  public ApiException(int code, String message) {
    super(message);
    this.code = code;
  }

  /**
   * .
   *
   * @param statusCode
   * @param message
   * @param responseHeaders
   * @param responseBody
   */
  public ApiException(
      int code, String message, Map<String, String> responseHeaders, String responseBody) {
    this(code, message);
    this.responseHeaders = responseHeaders;
    this.responseBody = responseBody;
  }

  /**
   * Get the HTTP status code.
   *
   * @return HTTP status code
   */
  public int getStatusCode() {
    return code;
  }

  /**
   * Get the HTTP response headers.
   *
   * @return A map of list of string
   */
  public Map<String, String> getResponseHeaders() {
    return responseHeaders;
  }

  /**
   * Get the HTTP response body.
   *
   * @return Response body in the form of string
   */
  public String getResponseBody() {
    return responseBody;
  }
}
