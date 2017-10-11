package com.beyond.bpush.gateway.dispatch;

import com.beyond.bpush.core.entity.Payload;
import com.beyond.bpush.core.entity.Product;
import com.beyond.bpush.core.service.PayloadServiceImpl;
import com.beyond.bpush.gateway.Connection;
import com.beyond.bpush.gateway.keeper.ConnectionKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.Callable;

/**
 *
 * 1对1或1对多的离线推送
 *
 * Created by admin on 14-8-8.
 */
public class OfflineSendThread implements Callable<Integer> {

    protected static Logger logger = LoggerFactory.getLogger(OfflineSendThread.class);

    private String userId;
    private Product product;

    public OfflineSendThread(Product product, String userId) {
        super();
        this.userId = userId;
        this.product = product;
    }

    @Override
    public Integer call() throws Exception {
        List<Long> ids = PayloadServiceImpl.instance.findLatestToOfflineClients(product.getId(), userId, 0);
        logger.info("Found Offline Message. userId={}, productId={}, total={}", userId, product, ids.size());
        if(ids == null || ids.size() == 0){
            return 0;
        }

        List<Payload> list = PayloadServiceImpl.instance.getSimpleList(ids);
        for (Payload message : list){
            this.doSendMessage(message);
        }

        return list.size();
    }

    private Integer doSendMessage(Payload message){
        if(message != null){
            Connection c = ConnectionKeeper.get(product.getAppKey(), this.userId);
            if(c != null) {
                c.send(message);
                return 1;
            }
        }

        return 0;
    }
}
