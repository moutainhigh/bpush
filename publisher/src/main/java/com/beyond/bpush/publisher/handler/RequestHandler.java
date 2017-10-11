package com.beyond.bpush.publisher.handler;


import com.beyond.bpush.client.RequestMessage;

/**
 * Created by yamingd on 8/7/15.
 */
public interface RequestHandler {

    /**
     *
     * @param request
     * @throws Exception
     */
    void handle(RequestMessage request) throws Exception;

}
