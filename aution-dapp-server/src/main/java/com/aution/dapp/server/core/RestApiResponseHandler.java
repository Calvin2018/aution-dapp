package com.aution.dapp.server.core;

import com.aution.dapp.server.utils.HttpUtils;
import com.aution.dapp.server.utils.Json;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class RestApiResponseHandler<T> implements ResponseHandler<RestApiResponse<T>> {
  private static final Log LOGGER = LogFactory.getLog(ApiResponseHandler.class);
  protected TypeToken<RestApiResponse<T>> typeToken;

  public RestApiResponseHandler(TypeToken<RestApiResponse<T>> typeToken) {
    this.typeToken = typeToken;
  }

  @Override
  public RestApiResponse<T> handleResponse(HttpResponse response) throws IOException {
    LOGGER.debug("Response Status Line: " + response.getStatusLine().toString());
    LOGGER.debug("Response Headers:\n" + HttpUtils.toString(response.getAllHeaders()));
    HttpEntity entity = response.getEntity();

    if (entity == null) {
      LOGGER.warn("Response entity is empty.");
      RestApiResponse<T> resp =
          new RestApiResponse<>(
              response.getStatusLine().getStatusCode(),
              HttpUtils.convertHeaders(response.getAllHeaders()),
              null);
      return resp;
    }

    // LOGGER.debug("Response Entity:\n" + EntityUtils.toString(entity));
    try {
      // check content type.
      ContentType contentType = HttpUtils.getContentType(entity);
      if (contentType == null
          || !contentType
              .getMimeType()
              .equalsIgnoreCase(ContentType.APPLICATION_JSON.getMimeType())) {
        String message =
            String.format(
                "Invalid contentType %s, expected %s.",
                contentType, ContentType.APPLICATION_JSON.getMimeType());
        LOGGER.error(message);
        throw new IOException(message);
      }

      String data = EntityUtils.toString(entity);
      LOGGER.debug("Response Json:" + data);

      RestApiResponse<T> result = new Json().deserialize(data, typeToken.getType());

      result.setStatusCode(response.getStatusLine().getStatusCode());
      result.setHeaders(HttpUtils.convertHeaders(response.getAllHeaders()));
      return result;
    } finally {
      EntityUtils.consume(entity);
    }
  }
}
