package com.aution.dapp.server.core.message;

/**
 *
 * <pre>
 *     消息重复检查器。检查实现{@link MsgIdGenerator}接口的类是否是重复消息。
 *     之后可能有基于Redis的实现,所以写个接口。
 * </pre>
 * 
 *
 */
public interface MessageDuplicateChecker {

    /**
     * 主动将某个消息从“重复消息池”中删除
     *
     * @param message
     *            需要重新判断是否重复的消息
     */
    void removeMsg(MsgIdGenerator message);

    /**
     * 判断消息是否重复(若非重复,会将该消息保存在某处,下次再访问,即认为其是重复消息)
     *
     * @param message
     *            通过{@link MsgIdGenerator}的getMessageId(),可获得messageId,用来标识message
     * @return 如果是重复消息，返回true，否则返回false
     */
    boolean isDuplicate(MsgIdGenerator message);

}
