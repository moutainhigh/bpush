/**
 * @author 王亮亮
 * @version V1.0
 * @Description: 推送服务接口
 * @Encode:UTF-8
 * @date 2016-07-14 10:23
 */

package com.beyond.bpush.app;

import java.util.Map;

/**
 * @param <T> 返回结果类型
 */
public interface IPushClient<T> {

    String PLATFORM_ANDRIOD = "android";
    String PLATFORM_IOS = "ios";
    String PLATFORM_ALL = "all";

    IPushClient buildConfig(String masterSecret, String appKey);

    IPushClient buildAlias(String alias);

    IPushClient buildAlias(String[] alias);

    IPushClient buildTags(String tags);

    IPushClient buildTags(String[] tags);

    IPushClient setPlatform(String platform);

    //通知推送
    Object sendPush(String alert);

    Object sendPush(String alert, String title);

    /**
     * @param alert 内容
     * @param title 标题
     * @param extra 健值对
     * @return
     */
    Object sendPush(String alert, String title, Map<String, String> extra);
}
