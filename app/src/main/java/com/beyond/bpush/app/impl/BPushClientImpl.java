package com.beyond.bpush.app.impl;

import com.beyond.bpush.app.AbsPushClient;
import com.beyond.bpush.app.IPushClient;
import com.beyond.bpush.client.AppPayload;
import com.beyond.bpush.client.BPushClient;
import com.beyond.bpush.client.OfflineMode;
import com.google.common.collect.Lists;

import java.io.IOException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description:
 * Created by ll_wang on 16/8/15.
 */
public class BPushClientImpl extends AbsPushClient {

    private IPushClient instance = null;

    private static final Logger LOG = LoggerFactory.getLogger(BPushClientImpl.class);
    private AppPayload payload;

    public BPushClientImpl() {
        super();
        BPushClient.connect();
        payload = new AppPayload();
        payload.offlineMode = Integer.valueOf(OfflineMode.SendAfterOnline.ordinal());
        payload.clients = Lists.newArrayList();
        payload.badge = 10;
        payload.broadcast = true;
        payload.sound = "default";
        payload.broadcast = false;
    }

    @Override
    public IPushClient buildAlias(String alias) {
        this.buildAlias(new String[] {alias});
        return this;
    }

    @Override
    public IPushClient buildAlias(String[] alias) {
        for (int i = 0; i < alias.length; i ++) {
            payload.clients.add(alias[i]);
        }
        return this;
    }

    @Override
    public IPushClient buildTags(String tags) {
        this.buildAlias(new String[] {tags});
        return this;
    }

    @Override
    public IPushClient buildTags(String[] tags) {
        for (int i = 0; i < tags.length; i ++) {
            payload.clients.add(tags[i]);
        }
        return this;
    }

    @Override
    public IPushClient setPlatform(String platform) {
        return this;
    }

    @Override
    public Object sendPush(String alert) {
        return null;
    }

    @Override
    public Object sendPush(String alert, String title) {
        return null;
    }

    @Override
    public Object sendPush(String alert, String title, Map extra) {
        boolean result = false;
        payload.title = title;
        payload.title = alert;
        payload.ext = extra;
        try {
            BPushClient.sendPayload(this.getAppKey(), payload);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


}
