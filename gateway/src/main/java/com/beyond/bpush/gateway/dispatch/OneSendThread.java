package com.beyond.bpush.gateway.dispatch;

import com.beyond.bpush.core.entity.*;
import com.beyond.bpush.core.service.ClientServiceImpl;
import com.beyond.bpush.core.service.PayloadServiceImpl;
import com.beyond.bpush.gateway.Connection;
import com.beyond.bpush.gateway.keeper.APNSKeeper;
import com.beyond.bpush.gateway.keeper.ConnectionKeeper;
import com.beyond.bpush.protobuf.PBAPNSMessage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.Callable;

/**
 *
 * 1对1或1对多的推送
 *
 * Created by admin on 14-8-8.
 */
public class OneSendThread implements Callable<Integer> {

    public static final String NULL = "NULL";
    protected static Logger logger = LoggerFactory.getLogger(OneSendThread.class);

    private Payload message;
    private Product product;

    public OneSendThread(final Product product, final Payload message) {
        super();
        this.message = message;
        this.product = product;
    }

    @Override
    public Integer call() throws Exception {
        long ts0 = System.currentTimeMillis();
        int ret = 0;
        try {
            ret = doSend();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        long duration = System.currentTimeMillis() - ts0;
        logger.info("Send Duration={} ms. total={}, ", duration, ret);
        return ret;
    }

    private Integer doSend() throws Exception {

        if(message == null){
            return 0;
        }

        List<String> clients = message.getClients();
        if (clients == null || clients.size() == 0){
            logger.error("Message Clients is Empty. {}", message);
            if (message.getStatusId().intValue() == PayloadStatus.Pending0) {
                message.setStatusId(PayloadStatus.Failed);
                message.setTotalUsers(0);
                PayloadServiceImpl.instance.add(message);
            }else{
                message.setStatusId(PayloadStatus.Failed);
                message.setTotalUsers(0);
                PayloadServiceImpl.instance.updateSendStatus(message);
            }
            return 0;
        }

        if (message.getStatusId().intValue() == PayloadStatus.Pending0) {
            message.setStatusId(PayloadStatus.Pending);
            message.setTotalUsers(0);
            PayloadServiceImpl.instance.add(message);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("OneSendThread. Message:{}, Client Total: {}", message, clients.size());
        }
        for (int i = 0; i < clients.size(); i++) {
            String client = clients.get(i);
            Client cc = ClientServiceImpl.instance.findByUserId(client);
            if (cc == null){
                //离线
                if (logger.isDebugEnabled()) {
                    logger.debug("Client not found. Message:{}, client={}", message.getId(), client);
                }
                if (message.getOfflineMode().intValue() == PBAPNSMessage.OfflineModes.SendAfterOnline_VALUE){
                    message.setStatus(client, new PushStatus(PushStatus.WaitOnline));
                }else{
                    message.setStatus(client, new PushStatus(PushStatus.NoClient));
                }

                continue;
            }

            //显示在客户端
//            if (0 == cc.getStatusId()){
//                message.setBadge(0);
//            }else {
//                message.setBadge(cc.getBadge() + 1);
//            }

            Connection c = ConnectionKeeper.get(product.getAppKey(), client);
            if(c != null) {
                if (c.getStatusId() == ClientStatus.Online){
                    c.send(message);
                    message.setStatus(client, new PushStatus(PushStatus.TcpSent));
                }else{
                    sendMessageToOfflineClient(client, cc);
                }
            }else{
                sendMessageToOfflineClient(client, cc);
            }
        }

        PayloadServiceImpl.instance.updateSendStatus(message);

        return clients.size();

    }

    private void sendMessageToOfflineClient(String client, Client cc) {
        int offlineMode = message.getOfflineMode().intValue();

        if (!cc.supportAPNS()){
            //不是iOS, 可以不继续跑
            logger.error("Client is not iOS. client={}, ", client);
            if (offlineMode == PBAPNSMessage.OfflineModes.SendAfterOnline_VALUE){
                message.setStatus(client, new PushStatus(PushStatus.WaitOnline));
            }else{
                message.setStatus(client, new PushStatus(PushStatus.NoConnections));
            }
            return;
        }

        if (StringUtils.isBlank(cc.getDeviceToken()) || NULL.equalsIgnoreCase(cc.getDeviceToken())){
            logger.error("Client's deviceToken not found. client={},", client);

            if (offlineMode == PBAPNSMessage.OfflineModes.SendAfterOnline_VALUE){
                message.setStatus(cc.getUserId(), new PushStatus(PushStatus.WaitOnline, "Wait online"));
            }else{
                message.setStatus(cc.getUserId(), new PushStatus(PushStatus.NO_DEVICE_TOKEN, "Offline and No DeviceToken"));
            }

            return;
        }

        if (PBAPNSMessage.APNSModes.All_VALUE == message.getToMode()){
            if (offlineMode == PBAPNSMessage.OfflineModes.APNS_VALUE) {
                //离线时通过APNS发送
                if (PBAPNSMessage.APNSModes.Signined_VALUE == message.getApnsMode()){
                    //需要APNS发送时，仅发送给没注销的用户
                    if (ClientStatus.Offline == cc.getStatusId()){
                        // 已注销登录，则等待上线
                        message.setStatus(cc.getUserId(), new PushStatus(PushStatus.WaitOnline, "Wait online"));
                    }else{
                        APNSKeeper.instance.push(this.product, cc, message);
                    }
                }else {
                    APNSKeeper.instance.push(this.product, cc, message);
                }
            }else if (offlineMode == PBAPNSMessage.OfflineModes.SendAfterOnline_VALUE){
                //离线时等待上线后再推送
                message.setStatus(cc.getUserId(), new PushStatus(PushStatus.WaitOnline, "Wait online"));
            }else{
                //直接忽略
                message.setStatus(cc.getUserId(), new PushStatus(PushStatus.Ignore, "Client says ignore."));
            }
        }else{
            //直接忽略
            message.setStatus(cc.getUserId(), new PushStatus(PushStatus.Ignore, "Message to online user only"));
        }
    }

}
