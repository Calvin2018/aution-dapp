package com.aution.dapp.server.utils;


import org.apache.commons.lang3.RandomUtils;

import java.util.UUID;

/**
 * 产生唯一id
 *
 */
public class GenerateNoUtil {


    public static String generateTradeNo() {
        return UUID.randomUUID().toString();
    }
    public static String generateGid(String userId) {
        return String.valueOf(System.currentTimeMillis()) + userId;
    }
}
