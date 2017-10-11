package com.beyond.bpush.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @Description:
 * Created by ll_wang on 16/7/14.
 */
public abstract class AbsPushClient<T> implements IPushClient {
    protected String platform = IPushClient.PLATFORM_ALL;
    private String masterSecret;
    private String appKey;
    private InputStream propInputStream;
    private Properties prop;
    protected static final Logger LOG = LoggerFactory.getLogger(AbsPushClient.class);

    {
        propInputStream = this.getClass().getClassLoader().getResourceAsStream("beyond/bpush_client.properties");
        prop = new Properties();
    }

    public AbsPushClient() {
        try {
            prop.load(propInputStream);
        } catch (IOException e) {
            LOG.error("PropertyFileLoadd:", e);
        }
        this.masterSecret = this.readProp("push.masterSecret");
        this.appKey = this.readProp("push.appKey");
    }

    @Override
    public IPushClient buildConfig(String masterSecret, String appKey) {
        this.masterSecret = masterSecret;
        this.appKey = appKey;

        return this;
    }

    /**
     * 获取配置文件中所对应的值
     * @param key
     * @return
     */
    private String readProp(String key) {
        String value = null;
        if(this.prop.containsKey(key)) {
            value = this.prop.getProperty(key);
        }
        System.out.println("readProp:key  ");
        System.out.println(this.prop.containsKey(key));

        return value;
    }

    protected String getAppKey() {
        return appKey;
    }

    protected void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    protected String getMasterSecret() {
        return masterSecret;
    }

    protected void setMasterSecret(String masterSecret) {
        this.masterSecret = masterSecret;
    }
}
