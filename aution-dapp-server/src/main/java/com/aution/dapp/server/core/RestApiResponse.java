package com.aution.dapp.server.core;

import java.util.Map;
import java.util.Objects;

public class RestApiResponse<T> extends ApiResult<T> {
	
  private int statusCode;
  private Map<String, String> headers;

  public RestApiResponse() {}

  public RestApiResponse( Map<String, String> headers) {
    this.headers = headers;
  }

  public RestApiResponse(int statusCode,Map<String, String> headers, T data) {
    this.headers = headers;
    this.data = data;
  }

  public Map<String, String> getHeaders() {
    return headers;
  }

  public void setHeaders(Map<String, String> headers) {
    this.headers = headers;
  }

  public int getStatusCode() {
	return statusCode;
}

public void setStatusCode(int statusCode) {
	this.statusCode = statusCode;
}

@Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("RestApiResponse{");
    sb.append("statusCode=").append(statusCode);
    sb.append(", headers=").append(headers);
    sb.append("code=").append(code);
    sb.append(", msg='").append(msg).append('\'');
    sb.append(", data=").append(data);
    sb.append('}');
    return sb.toString();
  }
  
}
