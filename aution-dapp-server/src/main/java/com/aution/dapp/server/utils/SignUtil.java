package com.aution.dapp.server.utils;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 生成签名工具类 createTime: 2019-03-27 17:21
 * 
 * @author zack
 */
public class SignUtil {

    /**
     * 生成sha1Hex的签名 value为空的字段不会加入签名
     * 
     * @param params
     *            需要签名的参数Map("sign"和"key"为保留字段名,它们不会加入签名)
     * @param signKey
     *            签名所需的密钥
     * @param ignoredParams
     *            签名时需要忽略的特殊参数
     * @return 签名
     */
    public static String createSecretSign(Map<String, Object> params, String signKey, String[] ignoredParams) {
        SortedMap<String, Object> sortedMap = new TreeMap<>(params);

        StringBuilder toSign = new StringBuilder();
        for (String key : sortedMap.keySet()) {
            String value = params.get(key).toString();
            boolean shouldSign = false;
            // value不为空且不是需要忽略的参数,并且不是保留字段,即加入签名
            if (StringUtils.isNotEmpty(value) && !ArrayUtils.contains(ignoredParams, key)
                && !Lists.newArrayList("SIGN", "KEY", "SECRET").contains(key.toUpperCase())) {
                shouldSign = true;
            }
            if (shouldSign) {
                toSign.append(key).append("=").append(value).append("&");
            }
        }
        toSign.append("secret=").append(signKey); //加入密钥进行签名
        return DigestUtils.sha1Hex(toSign.toString());
    }

    /**
     * 密钥已经包含在了paramMap中,直接对所有参数进行sha1-hash散列
     */
    public static String createCommonSign(Map<String, String> paramMap) {
        TreeMap<String, String> params = new TreeMap<>(paramMap);
        params.remove("sign");
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (StringUtils.isBlank(entry.getValue())) {
                continue;
            }
            stringBuilder.append(entry.getKey().trim());
            stringBuilder.append("=");
            stringBuilder.append(entry.getValue());
            stringBuilder.append("&");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return DigestUtils.sha1Hex(stringBuilder.toString());
    }

    /**
     * 将对方传的整个JSON字符串参数进行签名 (对接关爱通时,各个接口所用的签名)
     */
    @SuppressWarnings("unchecked")
    public static String createJsonSign(String jsonString, String accessToken, String appId, String appSecret,
        String timestamp) {
        Map params = ImmutableMap.builder().put("_body", jsonString).put("access_token", accessToken)
            .put("appid", appId).put("appsecret", appSecret).put("timestamp", timestamp).build();
        return SignUtil.createCommonSign(params);
    }

    /**
     * 我方发起的json签名,无需accessToken、appid、timestamp这3个参数
     */
    public static String createJsonSign(String jsonString, String appSecret) {
        Map params = ImmutableMap.builder().put("_body", jsonString).put("appsecret", appSecret).build();
        return SignUtil.createCommonSign(params);
    }

    
    public static String authSign(Map<String, Object> paramMap) {
    	Collection<String> keyset = paramMap.keySet();
    	List<String> list = new ArrayList<String>(keyset);
    	//对key键值按字典升序排序
		Collections.sort(list);
		String paramStr = "";
		for(String key : list){
			paramStr += "&" + key + "=" + paramMap.get(key);
		}
		paramStr = paramStr.substring(1);
        String sign = DigestUtils.sha1Hex(paramStr);
        return sign;
    }
}
