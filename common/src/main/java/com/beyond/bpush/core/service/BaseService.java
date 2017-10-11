package com.beyond.bpush.core.service;

import com.beyond.bpush.core.JdbcExecutor;
import com.beyond.bpush.core.MessageUtils;
import com.beyond.bpush.core.RedisBucket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import redis.clients.jedis.BinaryJedis;

import java.io.IOException;
import java.util.Properties;

/**
 * Created by admin on 14-8-8.
 */
public abstract class BaseService implements InitializingBean, DisposableBean {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    @Qualifier("mainJdbc")
    protected JdbcTemplate mainJdbc;

    @Autowired
    @Qualifier("mainJdbcNamed")
    protected NamedParameterJdbcTemplate mainJdbcNamed;

    @Autowired
    @Qualifier("appConfig")
    protected Properties appConfigs;

    @Autowired
    @Qualifier("jdbcConfig")
    protected Properties jdbcConfig;

    @Autowired
    protected RedisBucket redisBucket;

    @Autowired
    protected JdbcExecutor jdbcExecutor;

    @Override
    public void destroy() throws Exception {

    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    public <T> T getItemFromRedis(String key, Class<T> itemClass){
        BinaryJedis jedis =  redisBucket.getResource();
        try {
            byte[] t = jedis.get(key.getBytes());
            if (t==null || t.length == 0){

            }else{
                try {
                    T item = MessageUtils.asT(itemClass, t);
                    return item;
                } catch (IOException e) {
                    logger.error("解析消息错误", e);
                }
            }

        }catch (Exception e) {
            logger.error("读取产品信息错误", e);
            redisBucket.returnBrokenResource(jedis);
        }finally {
            redisBucket.returnResource(jedis);
        }

        return null;
    }

    public <T> void putItemToRedis(String key, T item){
        BinaryJedis jedis =  redisBucket.getResource();
        try {

            byte[] bytes = key.getBytes();
            String ret = jedis.set(bytes, MessageUtils.asBytes(item));
            jedis.expire(bytes, 3600);

        }catch (Exception e) {
            logger.error("读取产品信息错误", e);
            redisBucket.returnBrokenResource(jedis);
        }finally {
            redisBucket.returnResource(jedis);
        }
    }

    public void delCache(String key){
        BinaryJedis jedis =  redisBucket.getResource();
        try {
            jedis.del(key.getBytes());
        }catch (Exception e) {
            logger.error("读取产品信息错误", e);
            redisBucket.returnBrokenResource(jedis);
        }finally {
            redisBucket.returnResource(jedis);
        }
    }
}
