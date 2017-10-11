package com.beyond.bpush.app;

import com.beyond.bpush.app.impl.BPushClientImpl;
import com.beyond.bpush.app.impl.JPushClientImpl;

/**
 * @Description:
 * Created by ll_wang on 16/8/15.
 */
public class PushClientFactroy {
    private static PushClientFactroy instance = null;
    public final static Class BPUSH = BPushClientImpl.class;
    public final static Class JPUSH = JPushClientImpl.class;

    private static Class crruent = BPUSH;

    private PushClientFactroy() {}

    public synchronized static PushClientFactroy getInstance() {
        if (instance == null) {
            instance = new PushClientFactroy();
        }
        return instance;
    }

    public static IPushClient build() {
        IPushClient that = null;
        try {
            that = (IPushClient)crruent.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return that;
    }

}
