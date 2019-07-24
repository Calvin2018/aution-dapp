package com.aution.dapp.server.core.message;

/**
 *
 * 生成交易id,以便进行消息排重 消息排查接口:{@link MessageDuplicateChecker}
 */
public interface MsgIdGenerator {

    /**
     * 生成交易id,以便进行消息排重
     * 
     * @return 消息ID
     */
    String getMessageId();

}
