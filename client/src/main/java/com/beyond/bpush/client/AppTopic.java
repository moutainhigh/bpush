package com.beyond.bpush.client;

import org.msgpack.annotation.MessagePackMessage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@MessagePackMessage
public class AppTopic implements Serializable {

    /**
     * 业务数据库的唯一标示
     */
    public long objectId;
    /**
     * Topic名称
     */
    public String title;

    /**
     * 包含的用户标示
     */
    public List<String> clientIds = new ArrayList<String>();

}
