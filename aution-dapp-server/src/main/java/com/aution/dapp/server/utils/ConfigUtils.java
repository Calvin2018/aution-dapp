package com.aution.dapp.server.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * A config util - used to load config file from classpath.
 *
 * @author yang.tiedang@cesgroup.com.cn
 */
public class ConfigUtils {

  /**
   * Load config file from resource path.
   *
   * @param clazz
   * @param configFile
   * @return
   * @throws IOException
   */
  public static Properties fromResourcePath(Class<?> clazz, String configFile) throws IOException {
    Properties props = new Properties();
    InputStream in = clazz.getResourceAsStream(configFile);
    if (in == null)
      throw new FileNotFoundException(
          String.format("Doesn't find the file %s in classpath.", configFile));
    try {
      props.load(in);
    } finally {
      if (in != null) {
        try {
          in.close();
        } catch (IOException e) {
          // do nothing.
        }
      }
    }
    return props;
  }
}
