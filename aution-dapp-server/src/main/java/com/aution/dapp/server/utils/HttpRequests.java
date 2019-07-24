package com.aution.dapp.server.utils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicNameValuePair;

import com.aution.dapp.server.core.ApiConstants;
import com.google.common.base.Strings;

/**
 * 请求工厂类 - 用于方便创建 HTTP Request.
 *
 * @author yang.tiedang@cesgroup.com.cn
 */
public class HttpRequests {
  private static final Log LOGGER = LogFactory.getLog(HttpRequests.class);
  // private static final AtomicLong requestId = new AtomicLong();

  public static HttpPost newHttpPost(String url, Map<String, String> values) {
    return newHttpPost(url, values, false);
  }

  public static HttpPost newHttpPost2(String url, Map<String, Object> values) {
    return newHttpPost2(url, values, true);
  }

  public static HttpPost newHttpPost(String url, Map<String, String> values, boolean useJson) {
    if (url == null || url.isEmpty()) {
      throw new IllegalArgumentException("The argument url is required.");
    }

    HttpPost post = new HttpPost(url);
    if (useJson) {
      String jsonString = "";
      if (values != null) {
        jsonString = new Json().serialize(values);
      }
      return newHttpPost(url,jsonString);
    } else {
      List<NameValuePair> pairs = new ArrayList<>();
      if (values != null) {
        Set<String> keys = values.keySet();
        for (String key : keys) {
          pairs.add(new BasicNameValuePair(key, values.get(key)));
        }
      }
      UrlEncodedFormEntity entity = new UrlEncodedFormEntity(pairs, Consts.UTF_8);
      post.addHeader(ApiConstants.HEADER_CONTENT_TYPE, ContentType.APPLICATION_FORM_URLENCODED.getMimeType());
      post.setEntity(entity);
    }
    post.addHeader(ApiConstants.HEADER_ACCEPT, ContentType.APPLICATION_JSON.getMimeType());
    //Header agentHeader = Header.
    //post.setHeader();
    return post;
  }

  public static HttpPost newHttpPost2(String url, Object value) {
    if (url == null || url.isEmpty()) {
      throw new IllegalArgumentException("The argument url is required.");
    }

    String jsonString = "";
    if (value != null) {
      jsonString = new Json().serialize(value);
    }
    return newHttpPost(url,jsonString);
  }

  public static HttpPost newHttpPost2(String url, Map<String, Object> values, boolean useJson) {
    if (url == null || url.isEmpty()) {
      throw new IllegalArgumentException("The argument url is required.");
    }
    HttpPost post = new HttpPost(url);
    if (useJson) {
      String jsonString = "";
      if (values != null) {
        jsonString = new Json().serialize(values);
      }
      return newHttpPost(url,jsonString);
    } else {
      List<NameValuePair> pairs = new ArrayList<>();
      if (values != null) {
        Set<String> keys = values.keySet();
        for (String key : keys) {
          pairs.add(new BasicNameValuePair(key, values.get(key).toString()));
        }
      }
      UrlEncodedFormEntity entity = new UrlEncodedFormEntity(pairs, Consts.UTF_8);
      post.addHeader(ApiConstants.HEADER_CONTENT_TYPE, ContentType.APPLICATION_FORM_URLENCODED.getMimeType());
      post.setEntity(entity);
    }
    post.addHeader(ApiConstants.HEADER_ACCEPT, ContentType.APPLICATION_JSON.getMimeType());
    //Header agentHeader = Header.
    //post.setHeader();
    return post;
  }

  public static HttpPost newHttpPost(String url, String jsonString) {
    if (url == null || url.isEmpty()) {
      throw new IllegalArgumentException("The argument url is required.");
    }

    HttpPost post = new HttpPost(url);
    EntityBuilder eb = EntityBuilder.create();
    eb.setContentEncoding(Consts.UTF_8.name());
    eb.setContentType(ContentType.APPLICATION_JSON);
    HttpEntity entity = eb.setText(jsonString).build();
    post.setEntity(entity);
    post.addHeader(ApiConstants.HEADER_CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
    post.addHeader(ApiConstants.HEADER_ACCEPT, ContentType.APPLICATION_JSON.getMimeType());
    return post;
  }


  public static HttpDelete newHttpDelete(String url, String id) {
    if (url == null || url.isEmpty() || Strings.isNullOrEmpty(id)) {
      throw new IllegalArgumentException("The arguments url and id are required.");
    }

    HttpDelete delete = new HttpDelete(url + "/" + id);
    EntityBuilder eb = EntityBuilder.create();
    eb.setContentEncoding(Consts.UTF_8.name());
    eb.setContentType(ContentType.APPLICATION_JSON);
    delete.addHeader(ApiConstants.HEADER_CONTENT_TYPE, ContentType.APPLICATION_FORM_URLENCODED.getMimeType());
    delete.addHeader(ApiConstants.HEADER_ACCEPT, ContentType.APPLICATION_JSON.getMimeType());
    return delete;
  }

  public static HttpGet newHttpGet(String url, Map<String, String> values)
      throws IOException {
    if (url == null || url.isEmpty())
      throw new IllegalArgumentException("The argument url is required.");
    if (values == null) values = new HashMap<>();
    try {
      URIBuilder urib = new URIBuilder(url);
      // urib.setPath(path);
      Set<String> keys = values.keySet();
      for (String key : keys) {
        urib.addParameter(key, values.get(key));
      }
      HttpGet httpGet = new HttpGet(urib.build());
      httpGet.addHeader(ApiConstants.HEADER_ACCEPT, ContentType.APPLICATION_JSON.getMimeType());
      //httpGet.addHeader(ApiConstants.HEADER_CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
      return httpGet;
    } catch (URISyntaxException e) {
      LOGGER.error("Failed to create httpRequest.", e);
      throw new IOException(e.getMessage(),e);
    }
  }

  public static HttpPut newHttpPut(String url, Object value) {
    if (url == null || url.isEmpty()) {
      throw new IllegalArgumentException("The argument url is required.");
    }
    HttpPut put = new HttpPut(url);

    String jsonString = "";
    if (value != null) {
      jsonString = new Json().serialize(value);
    }

    EntityBuilder eb = EntityBuilder.create();
    eb.setContentEncoding(Consts.UTF_8.name());
    eb.setContentType(ContentType.APPLICATION_JSON);
    HttpEntity entity = eb.setText(jsonString).build();
    put.setEntity(entity);
    put.addHeader(ApiConstants.HEADER_CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
    put.addHeader(ApiConstants.HEADER_ACCEPT, ContentType.APPLICATION_JSON.getMimeType());

    return put;
  }
}
