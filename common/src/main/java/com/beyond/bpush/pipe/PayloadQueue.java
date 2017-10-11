package com.beyond.bpush.pipe;

import com.beyond.bpush.core.entity.Payload;

import java.util.List;

/**
 * 如何读取Publisher端的消息.
 * Created by admin on 14-8-8.
 */
public interface PayloadQueue {

    /**
     * 初始化.
     */
    void init();
    /**
     * 非广播消息
     * @param cursor
     * @return List
     */
    List<Payload> getNormalItems(PayloadCursor cursor);

    /**
     * 广播消息
     * @param cursor
     * @return List
     */
    List<Payload> getBroadcastItems(PayloadCursor cursor);

    /**
     * 添加项.
     * @param payload
     */
    void add(Payload payload);
}
