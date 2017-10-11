package com.beyond.bpush.publisher.handler;

import com.beyond.bpush.client.RequestMessage;
import com.beyond.bpush.client.TopicMessage;
import com.beyond.bpush.core.JdbcExecutor;
import com.beyond.bpush.core.MessageUtils;
import com.beyond.bpush.core.entity.Topic;
import com.beyond.bpush.core.service.ProductService;
import com.beyond.bpush.core.service.TopicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * Created by yamingd on 8/6/15.
 */
@Component
public class TopicHandler implements InitializingBean, RequestHandler {

    protected static Logger logger = LoggerFactory.getLogger(TopicHandler.class);

    public static TopicHandler instance = null;

    @Autowired
    @Qualifier("appConfig")
    private Properties conf;

    @Autowired
    private TopicService topicService;

    @Autowired
    private ProductService productService;

    @Autowired
    private JdbcExecutor jdbcExecutor;

    @Override
    public void afterPropertiesSet() throws Exception {
        instance = this;
    }

    /**
     *
     * @param request
     */
    @Override
    public void handle(RequestMessage request) throws Exception {
        Integer pid = productService.getProductId(request.getAppkey());
        final TopicMessage data = MessageUtils.asT(TopicMessage.class, request.getData());

        if (logger.isDebugEnabled()){
            logger.debug("TopicMessage: {}", data);
        }

        final Topic topic = new Topic();
        topic.setObjectId(data.objectId);
        topic.setProductId(pid);

        int typeId = request.getTypeId();

        if (typeId == RequestMessage.REQUEST_TYPE_NEW_TOPIC){
            jdbcExecutor.submit(new Runnable() {
                @Override
                public void run() {

                    try {
                        topicService.newTopic(topic, data.clientIds);
                    } catch (DataAccessException e) {
                        logger.error(e.getMessage(), e);
                    }

                }
            });

        }else if(typeId == RequestMessage.REQUEST_TYPE_NEW_TOPIC_CLIENT){

            jdbcExecutor.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        topicService.newTopicClients(topic, data.clientIds);
                    } catch (DataAccessException e) {
                        logger.error(e.getMessage(), e);
                    }
                }
            });


        }else if(typeId == RequestMessage.REQUEST_TYPE_REM_TOPIC){

            topicService.removeTopic(topic);

        }else if(typeId == RequestMessage.REQUEST_TYPE_REM_TOPIC_CLIENT){

            topicService.removeTopicClients(topic, data.clientIds);

        }

    }
}
