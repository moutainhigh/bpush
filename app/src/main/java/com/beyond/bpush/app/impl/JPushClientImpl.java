package com.beyond.bpush.app.impl;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;

import com.beyond.bpush.app.AbsPushClient;
import com.beyond.bpush.app.IPushClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @Description:
 * Created by ll_wang on 16/7/14.
 */
public class JPushClientImpl extends AbsPushClient {

    private IPushClient instance = null;

    protected static final Logger LOG = LoggerFactory.getLogger(JPushClientImpl.class);

    JPushClient jpushClient = null;
    Notification.Builder notification = null;
    PushPayload.Builder pushPayload = null;
    Message.Builder message = null;

    public JPushClientImpl() {
        jpushClient = new JPushClient(this.getMasterSecret(), this.getAppKey());
        notification = Notification.newBuilder();
        pushPayload = PushPayload.newBuilder();
        pushPayload.setAudience(Audience.all());
        message = Message.newBuilder();
    }

    @Override
    public IPushClient buildAlias(String alias) {
        String[] aliasArr = alias.split(",");
        return this.buildAlias(aliasArr);
    }

    @Override
    public IPushClient buildAlias(String[] alias) {
        pushPayload.setAudience(Audience.alias(alias));
        return this;
    }

    @Override
    public IPushClient setPlatform(String platform) {
        //设置需要推送的平台
        if (JPushClientImpl.PLATFORM_ALL.equals(platform)) {
            platform = JPushClientImpl.PLATFORM_ALL;
            pushPayload.setPlatform(Platform.android_ios());
        } else if (JPushClientImpl.PLATFORM_ANDRIOD.equals(platform)) {
            platform = JPushClientImpl.PLATFORM_ANDRIOD;
            pushPayload.setPlatform(Platform.android());
        } else if (JPushClientImpl.PLATFORM_IOS.equals(platform)) {
            platform = JPushClientImpl.PLATFORM_IOS;
            pushPayload.setPlatform(Platform.ios());
        } else {
            pushPayload.setPlatform(Platform.android_ios());
            LOG.warn("SetPlatform:", "非法类型参数");
        }
        return this;
    }

    @Override
    public IPushClient buildTags(String tags) {
        String[] tagsArr = tags.split(",");
        return buildTags(tagsArr);
    }

    @Override
    public IPushClient buildTags(String[] tags) {
        pushPayload.setAudience(Audience.tag_and(tags));
        return this;
    }

    @Override
    public Object sendPush(String alert) {
        PushResult result = null;
        if (JPushClientImpl.PLATFORM_ALL.equals(platform)) {
            notification.addPlatformNotification(AndroidNotification.newBuilder().setAlert(alert).build());
        } else if (JPushClientImpl.PLATFORM_ANDRIOD.equals(platform)) {
            notification.addPlatformNotification(AndroidNotification.newBuilder().setAlert(alert).build());
        } else if (JPushClientImpl.PLATFORM_IOS.equals(platform)) {
            notification.addPlatformNotification(AndroidNotification.newBuilder().setAlert(alert).build());
        }

        pushPayload.setNotification(notification.build());

//        PushPayload pushPayload = PushPayload.alertAll(alert);
        try {
            result = jpushClient.sendPush(pushPayload.build());
        } catch (APIConnectionException e) {
            e.printStackTrace();
        } catch (APIRequestException e) {
            e.printStackTrace();
        }

        System.out.println(result.toString());

        return result;
    }

    @Override
    public Object sendPush(String alert, String title) {
        return sendPush(alert, title, null);
    }

    @Override
    public Object sendPush(String alert, String title, Map extra) {
        PushResult result = null;
        if (JPushClientImpl.PLATFORM_ALL.equals(platform)) {
            notification.addPlatformNotification(AndroidNotification.newBuilder().
                    setAlert(alert).setTitle(title).addExtras(extra).build());
            notification.addPlatformNotification(IosNotification.newBuilder().
                    setAlert(alert).setCategory(title).addExtras(extra).build());
        } else if (JPushClientImpl.PLATFORM_ANDRIOD.equals(platform)) {
            notification.addPlatformNotification(AndroidNotification.newBuilder().
                    setAlert(alert).setTitle(title).addExtras(extra).build());
        } else if (JPushClientImpl.PLATFORM_IOS.equals(platform)) {
            notification.addPlatformNotification(IosNotification.newBuilder().
                    setAlert(alert).setCategory(title).addExtras(extra).build());
        }

        pushPayload.setNotification(notification.build());

        try {
            result = jpushClient.sendPush(pushPayload.build());
        } catch (APIConnectionException e) {
            LOG.error("Connection error. Should retry later. ", e);
        } catch (APIRequestException e) {
            LOG.error("Error response from JPush server. Should review and fix it. ", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Code: " + e.getErrorCode());
            LOG.info("Error Message: " + e.getErrorMessage());
            System.out.println("Error Code: " + e.getErrorCode());
            System.out.println("Error Message: " + e.getErrorMessage());
            LOG.info("Msg ID: " + e.getMsgId());
        }

        return result;
    }

    public Notification.Builder getNotification() {
        return notification;
    }

    public Message.Builder getMessage() {
        return message;
    }

    public PushPayload.Builder getPushPayload() {
        return pushPayload;
    }

}
