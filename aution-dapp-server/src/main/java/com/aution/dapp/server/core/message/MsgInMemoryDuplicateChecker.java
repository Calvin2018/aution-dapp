package com.aution.dapp.server.core.message;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * <pre>
 *  消息重复检查器,将每个消息id保存在内存里(应只防并发,业务安全应由数据库保证)
 *  <em>注：若使用集群,得换Redis等其他存储消息id的方式</em>
 *  目前的使用场景:第三方发起 "用户向第三方应用的转账"的消息重复检查器
 *  使用案例:{@link com.cesgroup.cashier.merchant.OrderCreatorController#notified}
 * </pre>
 * 
 *
 */
public class MsgInMemoryDuplicateChecker implements MessageDuplicateChecker {

	 private static final Log LOG = LogFactory.getLog(MsgInMemoryDuplicateChecker.class);
    /**
     * 一个消息ID在内存的过期时间：默认180秒.
     */
    private final Long timeToLive;

    /**
     * 每隔多少周期检查消息ID是否过期：默认60秒.
     */
    private final Long clearPeriod;

    /**
     * key -> 消息id, value -> 消息时间毫秒数
     */
    private final ConcurrentHashMap<String, Long> msgId2Timestamp = new ConcurrentHashMap<>();

    /**
     * 无参构造方法.
     * 
     * <pre>
     * 一个消息ID在内存的过期时间：180秒
     * 每隔多少周期检查消息ID是否过期：60秒
     * </pre>
     */
    private MsgInMemoryDuplicateChecker() {
        this.timeToLive = 180 * 1000L;
        this.clearPeriod = 60 * 1000L;
        initBgCleaner();
    }

    /**
     * 初始化消息ID
     */
    private void initBgCleaner() {
        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    while (true) {
                        Thread.sleep(clearPeriod);
                        Long now = System.currentTimeMillis();
                        for (Map.Entry<String, Long> entry : msgId2Timestamp.entrySet()) {
                            if (now - entry.getValue() > timeToLive) {
                                msgId2Timestamp.entrySet().remove(entry);
                            }
                        }
                    }
                } catch (InterruptedException e) {
                	LOG.error("消息重复检查器异常关闭:{}", e);
                }
            }
        });
        t.setDaemon(true);
        t.start();
    }

    /**
     * 判断消息是否重复
     * 
     * @param message
     *            实现{@link MsgIdGenerator}接口的消息类
     * @return 如果是重复消息，返回true，否则返回false
     */
    @Override
    public boolean isDuplicate(MsgIdGenerator message) {
        String messageId = message.getMessageId();
        if (StringUtils.isBlank(messageId)) { //messageId为空,默认不排重
            return false;
        }
        Long timestamp = this.msgId2Timestamp.putIfAbsent(messageId, System.currentTimeMillis());
        return timestamp != null;
    }

    @Override
    public void removeMsg(MsgIdGenerator message) {
        String messageId = message.getMessageId();
        if (messageId == null) {
            return;
        }
        this.msgId2Timestamp.remove(messageId);
    }

    /**
     * Holder单例
     */
    private static class Holder {

        private static final MessageDuplicateChecker CHECKER = new MsgInMemoryDuplicateChecker();
    }

    public static MessageDuplicateChecker getInstance() {
        return Holder.CHECKER;
    }

}
