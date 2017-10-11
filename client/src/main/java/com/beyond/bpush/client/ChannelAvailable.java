package com.beyond.bpush.client;

public interface ChannelAvailable {

    /**
     *
     * @param ctx 链接对象
     */
    void execute(final ClientConnection ctx);

    /**
     *@param e 异常对象
     */
    void error(Exception e);
}
