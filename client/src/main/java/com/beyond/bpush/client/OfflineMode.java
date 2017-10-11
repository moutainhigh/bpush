package com.beyond.bpush.client;

public enum OfflineMode {
    /**
     * 忽略消息
     */
    Ignore(0),
    /**
     * 通过 APNS 推送
     */
    APNS(1),
    /**
     * 等上线了再推送
     */
    SendAfterOnline(2);

    private int value;

    OfflineMode(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "OfflineModes{" +
                "value=" + value +
                '}';
    }
}
