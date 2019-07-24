package com.aution.dapp.server.model;

import com.aution.dapp.server.utils.JsonUtil;
import com.aution.dapp.server.utils.SignUtil;

/**
 * 需要签名的bean对象继承此类
 *
 */
public class BeanSigner {

    /**
     * 生成bean对象的sha1Hex签名
     * 
     * @param secret
     *            签名所需的密钥
     */
    public String createSign(String secret) {
        return SignUtil.createJsonSign(JsonUtil.toSnakeJson(this), secret);
    }

}
