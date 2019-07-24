/**
 * <p>Copyright:Copyright(c) 2017</p>
 * <p>Company:上海中信信息发展股份有限公司</p>
 * <p>包名:com.cesgroup.walking.walking.service</p>
 * <p>文件名:RedisService.java</p>
 * <p>创建时间:2017-10-20 15:40</p>
 * <p>作者:huz</p>
 */

package com.aution.dapp.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisService {

    @Autowired
    private RedisTemplate<String, String> stringTemplate;

    /**
     * 设置字符串
     *
     * @param key
     *            键
     * @param str
     *            字符串
     */
    public void setString(final String key, final String str) {
        stringTemplate.opsForValue().set(key, str);
    }

    /**
     * 获取字符串
     *
     * @param key
     *            键
     * @return 字符串
     */
    public String getString(final String key) {
        return stringTemplate.opsForValue().get(key);
    }

    /**
     * 删除字符串
     *
     * @param key
     *            键
     */
    public void removeString(final String key) {
        stringTemplate.delete(key);
    }

    public void setExpireString(String key, String value, int seconds) {
        stringTemplate.opsForValue().set(key, value);
        stringTemplate.expire(key, seconds, TimeUnit.SECONDS);
    }

}
