package com.aution.dapp.server.core;

import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;

import com.aution.dapp.server.utils.HttpUtils;
import com.aution.dapp.server.utils.Json;
import com.aution.dapp.server.utils.ReflectUtils;

/**
 * 一个抽象的 ResponseHandler， 将 HttpResponse 解析为 Json ApiResponse 类.
 *
 * @param <T> Response Body 类型。
 */
public abstract class ApiResponseHandler<T> implements ResponseHandler<ApiResponse<T>> {
  private static final Log LOGGER = LogFactory.getLog(ApiResponseHandler.class);

  @Override
  public ApiResponse<T> handleResponse(HttpResponse response)
      throws IOException {
    LOGGER.debug("Response Status Line: " + response.getStatusLine().toString());
    LOGGER.debug("Response Headers:\n" + HttpUtils.toString(response.getAllHeaders()));
    HttpEntity entity = response.getEntity();

    if (entity == null) {
      LOGGER.warn("Response entity is empty.");
      ApiResponse<T> resp = new ApiResponse<>(
          response.getStatusLine().getStatusCode(),
          HttpUtils.convertHeaders(response.getAllHeaders()),
          null);
      return resp;
    }

    // LOGGER.debug("Response Entity:\n" + EntityUtils.toString(entity));
    try {
      // check content type.
      ContentType contentType = HttpUtils.getContentType(entity);
      if (contentType == null ||
          !contentType
              .getMimeType()
              .equalsIgnoreCase(ContentType.APPLICATION_JSON.getMimeType())) {
        String message = String.format("Invalid contentType %s, expected %s.",
            contentType, ContentType.APPLICATION_JSON.getMimeType());
        LOGGER.error(message);
        throw new IOException(message);
      }

      String data = EntityUtils.toString(entity);
      LOGGER.debug("Response Json:" + data);
      Class<?> type = ReflectUtils.getTypeArguments(ApiResponseHandler.class, getClass()).get(0);
      LOGGER.debug(String.format("Type: %s", type));
      T result = new Json().deserialize(data, type);
      return new ApiResponse<>(
          response.getStatusLine().getStatusCode(),
          HttpUtils.convertHeaders(response.getAllHeaders()),
          result);
    } finally {
      EntityUtils.consume(entity);
    }
  }
}
