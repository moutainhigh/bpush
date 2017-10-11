package com.beyond.bpush.gateway.handler;

import com.beyond.bpush.core.entity.Client;
import com.beyond.bpush.core.entity.Product;
import com.beyond.bpush.core.service.ClientServiceImpl;
import com.beyond.bpush.core.service.ProductServiceImpl;
import com.beyond.bpush.gateway.dispatch.Dispatcher;
import com.beyond.bpush.gateway.dispatch.DispatcherRunner;
import com.beyond.bpush.protobuf.PBAPNSEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

/**
 * 触发新增客户端.
 * Created by admin on 14-8-11.
 */
public class OnNewlyAddThread implements Callable<Boolean> {

    protected static Logger logger = LoggerFactory.getLogger(OnNewlyAddThread.class);

    private PBAPNSEvent cc;
    public OnNewlyAddThread(PBAPNSEvent cc){
        this.cc = cc;
    }

    @Override
    public Boolean call() throws Exception {
        Client client = ClientServiceImpl.instance.findByUserId(cc.getUserId());
        boolean isNew = false;
        if (client == null){
            client = new Client();
            Product product = ProductServiceImpl.instance.findByKey(cc.getAppKey());
            if (product != null) {
                client.setProductId(product.getId());
                client.setUserId(cc.getUserId());
                client.setTypeId(cc.getTypeId()); //
                client.setDeviceToken(cc.getToken());
                client.setDeviceId(cc.getDeviceId());
                try {
                    ClientServiceImpl.instance.add(client);
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
                isNew = true;
            } else {
                logger.error("ProductServiceImpl:未找到平台");
            }


        }

        //如不是旧客户端. 则推送旧消息
        if (!isNew) {
            client.setDeviceToken(cc.getToken());
            client.setTypeId(cc.getTypeId());

            Dispatcher dispatcher = DispatcherRunner.instance.get(cc.getAppKey());
            if (dispatcher != null) {
                dispatcher.pushOfflinePayload(cc.getUserId());
            }
            try {
                ClientServiceImpl.instance.updateOnlineTs(client);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }

        return true;
    }
}
