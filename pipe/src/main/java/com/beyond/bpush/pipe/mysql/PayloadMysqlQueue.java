package com.beyond.bpush.pipe.mysql;


import com.beyond.bpush.core.entity.Payload;
import com.beyond.bpush.core.entity.PayloadStatus;
import com.beyond.bpush.core.service.PayloadService;
import com.beyond.bpush.pipe.PayloadCursor;
import com.beyond.bpush.pipe.PayloadQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 * 使用Mysql数据库存储.
 *
 * Created by admin on 14-8-8.
 */
@Deprecated
@Component("payloadMysqlQueue")
public class PayloadMysqlQueue implements PayloadQueue {

    @Autowired
    private PayloadService payloadService;

    @Override
    public void init() {

    }

    @Override
    public List<Payload> getNormalItems(PayloadCursor cursor) {
        return this.payloadService.findNormalList(cursor.getProduct().getId(), cursor.getStartId(), cursor.getPage(), cursor.getLimit());
    }

    @Override
    public List<Payload> getBroadcastItems(PayloadCursor cursor) {
        return this.payloadService.findBrodcastList(cursor.getProduct().getId(), cursor.getStartId(), cursor.getPage(), cursor.getLimit());
    }

    @Override
    public void add(Payload payload) {
        payload.setStatusId(PayloadStatus.Pending);
        payload.setTotalUsers(0);
        payloadService.add(payload);
    }
}
