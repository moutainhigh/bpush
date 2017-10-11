package com.beyond.bpush.core.entity;

/**
 * 客户端状态
 * Created by admin on 14-8-6.
 */
public interface ClientStatus {

    int Offline = 0;

    int Online = 1;

    int Sleep = 2;

    int Lost = 3;
}
