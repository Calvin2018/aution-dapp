package com.aution.dapp.server.utils;


import org.apache.commons.lang3.RandomUtils;

/**
 * 产生唯一id
 *
 */
public class GenerateNoUtil {


    public static String generateTradeNo() {
        return String.valueOf(System.currentTimeMillis()) + String.valueOf(RandomUtils.nextInt(0,100));
    }
    public static String generateGid(String userId) {
        return String.valueOf(System.currentTimeMillis()) + userId;
    }
}
