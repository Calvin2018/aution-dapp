/** */
package com.aution.dapp.server.utils;

import java.util.HashMap;
import java.util.Map;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;

import javax.servlet.http.HttpServletRequest;

/**
 * Http 工具类，用于提供常用的方法.
 *
 * @author yang.tiedang@cesgroup.com.cn
 */
public class HttpUtils {

  /**
   * 将 Http header 转换成字符串类型 - 每个 header 一行.
   *
   * @param headers headers array.
   * @return headers as formatted string.
   */
  public static String toString(Header[] headers) {
    if (headers == null) {
      return null;
    }
    StringBuilder sb = new StringBuilder();
    for (Header header : headers) {
      sb.append(header.getName()).append(":").append(header.getValue()).append("\n");
    }
    return sb.toString();
  }

  /**
   * Get contentType from entity.
   *
   * @param entity the http entity.
   * @return the contentType attribute
   */
  public static ContentType getContentType(final HttpEntity entity) {
    if (entity == null) {
      return null;
    }
    Header typeH = entity.getContentType();
    if (typeH == null) {
      return null;
    }

    String value = typeH.getValue();
    if (value == null) {
      return null;
    }

    return ContentType.parse(value);
  }

  /**
   * Convert the headers array to map structure.
   *
   * @param headers headers array.
   * @return the headers as map object.
   */
  public static Map<String, String> convertHeaders(Header[] headers) {
    Map<String, String> hds = new HashMap<>();
    if (headers != null && headers.length > 0) {
      for (Header header: headers) {
        hds.put(header.getName(),header.getValue());
      }
    }
    return hds;
  }

  /**
   * 获取ip地址
   * @param request
   * @return
   */
  public static String getIpAddress(HttpServletRequest request) {
    String ip = request.getHeader("x-forwarded-for");
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("Proxy-Client-IP");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("WL-Proxy-Client-IP");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("HTTP_CLIENT_IP");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("HTTP_X_FORWARDED_FOR");
    }
    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getRemoteAddr();
    }

    if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
      //X-Real-IP：nginx服务代理
      ip = request.getHeader("X-Real-IP");
    }
    if(ip!=null){
      ip=ip.split(",")[0];
    }
    return ip;
  }
}
