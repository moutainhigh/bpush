package com.beyond.bpush.gateway.keeper;

import com.beyond.bpush.core.entity.Client;
import com.beyond.bpush.core.entity.Payload;
import com.beyond.bpush.core.entity.Product;
import com.beyond.bpush.core.entity.PushStatus;
import com.beyond.bpush.core.service.ClientServiceImpl;
import com.beyond.bpush.core.service.PayloadServiceImpl;
import com.beyond.bpush.core.service.ProductService;
import com.google.common.collect.Maps;
import com.relayrides.pushy.apns.*;
import com.relayrides.pushy.apns.util.MalformedTokenStringException;
import com.relayrides.pushy.apns.util.SSLContextUtil;
import com.relayrides.pushy.apns.util.SimpleApnsPushNotification;
import com.relayrides.pushy.apns.util.TokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.*;

/**
 * Created by admin on 14-8-13.
 */
@Component
public class APNSKeeper implements InitializingBean, DisposableBean {

    protected static Logger logger = LoggerFactory.getLogger(APNSKeeper.class);

    private static Map<Integer, PushManager> mapping = Maps.newConcurrentMap();


    @Autowired
    @Qualifier("appConfig")
    private Properties serverConfig;

    @Autowired
    private ProductService productService;

    private List<Product> productList;
    private boolean sandBox = true;

    @Override
    public void destroy() throws Exception {
        logger.info("{}. destroy. mapping={}", APNSKeeper.class.getSimpleName(), mapping);
        Iterator<Integer> iter = mapping.keySet().iterator();
        while (iter.hasNext()) {
            Integer productId = iter.next();
            PushManager<SimpleApnsPushNotification> pm = mapping.get(productId);
            try {
                logger.info("{}. destroy. 准备shutdown PushManager... pm={}", APNSKeeper.class.getSimpleName(), pm);
                long start = System.currentTimeMillis();
                pm.shutdown();
                long end = System.currentTimeMillis();
                logger.info("{}. destroy. 结束shutdown PushManager. pm=={}, duration={}", APNSKeeper.class.getSimpleName(), pm, (end-start));
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    class SimpleApnsPushNotificationWithId extends SimpleApnsPushNotification{

        private long paylodId;
        private String userId;

        public SimpleApnsPushNotificationWithId(byte[] token, String payload) {
            super(token, payload);
        }

        public SimpleApnsPushNotificationWithId(byte[] token, String payload, Date invalidationTime) {
            super(token, payload, invalidationTime);
        }

        public SimpleApnsPushNotificationWithId(byte[] token, String payload, DeliveryPriority priority) {
            super(token, payload, priority);
        }

        public SimpleApnsPushNotificationWithId(byte[] token, String payload, Date invalidationTime, DeliveryPriority priority) {
            super(token, payload, invalidationTime, priority);
        }

        public long getPaylodId() {
            return paylodId;
        }

        public void setPaylodId(long paylodId) {
            this.paylodId = paylodId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer("SimpleApnsPushNotificationWithId{");
            sb.append(super.toString());
            sb.append("paylodId=").append(paylodId);
            sb.append(", userId=").append(userId);
            sb.append('}');
            return sb.toString();
        }
    }

    class PushRejectedNotificationListener implements RejectedNotificationListener<SimpleApnsPushNotification> {

        @Override
        public void handleRejectedNotification(
                final PushManager<? extends SimpleApnsPushNotification> pushManager,
                final SimpleApnsPushNotification notification,
                final RejectedNotificationReason reason) {

            logger.error("[{}] {} was rejected with rejection reason {}\n", pushManager.getName(), notification, reason);

            SimpleApnsPushNotificationWithId item = (SimpleApnsPushNotificationWithId)notification;
            PayloadServiceImpl.instance.updateSendStatus(item.paylodId, item.getUserId(), new PushStatus(PushStatus.APNSTokenInvalid));
        }
    }

    class PushFailedConnectionListener implements FailedConnectionListener<SimpleApnsPushNotification> {

        @Override
        public void handleFailedConnection(
                final PushManager<? extends SimpleApnsPushNotification> pushManager,
                final Throwable cause) {

            logger.error(pushManager.getName() + " failed to connect Apple APNS server. ", cause);

            if (cause instanceof SSLHandshakeException) {
                // This is probably a permanent failure, and we should shut down
                // the PushManager.
            }
        }
    }


    public PushManager get(Product product){

        if (StringUtils.isBlank(product.getDevCertPath())
                || StringUtils.isBlank(product.getDevCertPass())
                || StringUtils.isBlank(product.getCertPath())
                || StringUtils.isBlank(product.getCertPass())){
            logger.error("Product iOS Push Service Miss Cert Path and Password. {}", product);
            return null;
        }

        PushManager service = mapping.get(product.getId());
        if (service == null){

            ApnsEnvironment apnsEnvironment = null;
            SSLContext sslContext = null;

            try {
                if (sandBox){
                    apnsEnvironment = ApnsEnvironment.getSandboxEnvironment();
                    sslContext = SSLContextUtil.createDefaultSSLContext(product.getDevCertPath(), product.getDevCertPass());
                }else{
                    apnsEnvironment = ApnsEnvironment.getProductionEnvironment();
                    sslContext = SSLContextUtil.createDefaultSSLContext(product.getCertPath(), product.getCertPass());
                }
            } catch (KeyStoreException e) {
                logger.error(e.getMessage(), e);
            } catch (NoSuchAlgorithmException e) {
                logger.error(e.getMessage(), e);
            } catch (CertificateException e) {
                logger.error(e.getMessage(), e);
            } catch (UnrecoverableKeyException e) {
                logger.error(e.getMessage(), e);
            } catch (KeyManagementException e) {
                logger.error(e.getMessage(), e);
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }

            PushManagerConfiguration configuration = new PushManagerConfiguration();
            configuration.setConcurrentConnectionCount(1);

            final PushManager<SimpleApnsPushNotification> pushManager =
                    new PushManager<SimpleApnsPushNotification>(
                            apnsEnvironment,
                            sslContext,
                            null, // Optional: custom event loop group
                            null, // Optional: custom ExecutorService for calling listeners
                            null, // Optional: custom BlockingQueue implementation
                            configuration,
                            "ApnsPushManager-" + product.getId());

            pushManager.registerRejectedNotificationListener(new PushRejectedNotificationListener());
            pushManager.registerFailedConnectionListener(new PushFailedConnectionListener());

            pushManager.start();

            mapping.put(product.getId(), pushManager);
            service = pushManager;
        }

        return service;
    }

    /**
     *
     * 使用APNS服务推送到苹果
     *
     * @param product
     * @param cc
     * @param message
     *
     */
    public void push(Product product, Client cc, Payload message){
        PushManager<SimpleApnsPushNotification> service = get(product);
        if (service != null){
            try{
                if (StringUtils.isBlank(cc.getDeviceToken()) || "NULL".equalsIgnoreCase(cc.getDeviceToken())){
                    message.setStatus(cc.getUserId(), new PushStatus(PushStatus.NO_DEVICE_TOKEN));
                }else {

                    SimpleApnsPushNotificationWithId e = wrapPayload(cc, message);
                    if (e == null){
                        message.setStatus(cc.getUserId(), new PushStatus(PushStatus.APNSTokenInvalid));
                    }else {
                        service.getQueue().put(e);
                        message.setStatus(cc.getUserId(), new PushStatus(PushStatus.APNSSent));
                        ClientServiceImpl.instance.updateBadge(cc.getUserId(), 1);
                    }
                }
            }catch(Exception e){
                logger.error("Push Failed", e);
                message.setStatus(cc.getUserId(), new PushStatus(PushStatus.iOSPushError, e.getMessage()));
            }
        }else{
            logger.error("iOS Push Service Not Found.");
            message.setStatus(cc.getUserId(), new PushStatus(PushStatus.iOSPushConfigError));
        }
    }

    /**
     *
     * @param cc
     * @param message
     * @return
     */
    private SimpleApnsPushNotificationWithId wrapPayload(Client cc, Payload message){

        final byte[] token;
        try {
            token = TokenUtil.tokenStringToByteArray(cc.getDeviceToken());
        } catch (MalformedTokenStringException e) {
            logger.error("DeviceToken is Invalid. token=" + cc.getDeviceToken());
            return null;
        }

        Date expireDate = new Date(System.currentTimeMillis() + expireTime);
        SimpleApnsPushNotificationWithId notification = new SimpleApnsPushNotificationWithId(token, message.asJson(), expireDate);
        notification.setPaylodId(message.getId());
        notification.setUserId(cc.getUserId());

        return notification;
    }

    /**
     * Message离线过期时间
     */
    private long expireTime = 7 * 86400 * 1000; // 一周内

    @Override
    public void afterPropertiesSet() throws Exception {
        String flag = this.serverConfig.get("apns.sandbox") + "";
        if (flag.equalsIgnoreCase("true")) {
            this.sandBox = true;
        }else{
            this.sandBox = false;
        }

        Object val = this.serverConfig.get("apns.expire");
        if (null != val){
            int days = Integer.parseInt(val + "");
            expireTime = days * 86400 * 1000;
        }

        productList = productService.findAll();
        for (Product product : productList){
            PushManager<SimpleApnsPushNotification> service = get(product);
            if (service != null) {
                logger.info("Init Product APNS service. {} / {}", product, service);
            }
        }

        instance = this;
    }

    public static APNSKeeper instance = null;
}
