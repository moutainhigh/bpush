package com.beyond.bpush.publisher.handler;

import com.beyond.bpush.client.PayloadMessage;
import com.beyond.bpush.client.RequestMessage;
import com.beyond.bpush.core.MessageUtils;
import com.beyond.bpush.core.entity.Payload;
import com.beyond.bpush.core.service.ProductService;
import com.beyond.bpush.pipe.PayloadQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * Created by admin on 2014/9/25.
 */
@Component
public class PayloadHandler implements InitializingBean, ApplicationContextAware, RequestHandler {

    protected static Logger logger = LoggerFactory.getLogger(PayloadHandler.class);

    private ApplicationContext applicationContext;
    public static PayloadHandler instance = null;

    @Autowired
    @Qualifier("payloadMysqlQueue")
    private PayloadQueue defaultQueue;

    @Autowired
    @Qualifier("appConfig")
    private Properties conf;

    @Autowired
    private ProductService productService;

    private PayloadQueue queue;

    @Override
    public void afterPropertiesSet() throws Exception {

        String beanName = conf.getProperty("payloadQueue", "payloadMysqlQueue");
        queue = this.applicationContext.getBean(beanName, PayloadQueue.class);

        instance = this;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 保存Payload到 Queue.
     * Queue 根据配置项payloadQueue决定
     *
     * @param request
     */
    @Override
    public void handle(RequestMessage request) throws Exception {
        Integer pid = productService.getProductId(request.getAppkey());
        PayloadMessage data = MessageUtils.asT(PayloadMessage.class, request.getData());
        if (logger.isDebugEnabled()){
            logger.debug("PayloadMessage: {}", data);
        }
        Payload payload = new Payload(data);
        payload.setProductId(pid);
        queue.add(payload);
    }
}
