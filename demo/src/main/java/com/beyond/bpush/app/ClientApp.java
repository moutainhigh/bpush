package com.beyond.bpush.app;

import com.beyond.bpush.client.AppPayload;
import com.beyond.bpush.client.BPushClient;
import com.google.common.collect.Lists;

import java.io.IOException;
import java.util.HashMap;

public class ClientApp
{

    public static void main( String[] args )
    {

        BPushClient.connect();

        int i = 0;
        while (i < 10 * 1000){
            send(i);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            i++;
        }

        try {
            Thread.sleep(3600 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void send(int seq) {
        AppPayload payload = new AppPayload();
        payload.title = "this is title " + seq;
        payload.badge = 10;
        payload.broadcast = true;
        payload.sound = "default";
        payload.clients = Lists.newArrayList();
        payload.clients.add("1");
        payload.clients.add("2");
        payload.ext = new HashMap<String, String>();
        payload.ext.put("a", "1");
        payload.ext.put("b", "2");

        try {
            System.out.println("send message. ");
            BPushClient.sendPayload("da6ae142e0e009149e4a365d", payload);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
