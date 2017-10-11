package com.beyond.bpush.app;

import com.beyond.bpush.gateway.ServerMain;

public class GatewayApp
{

    public static void startGateway(final String[] args){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                ServerMain.main(args);
            }
        });
        thread.start();
    }

    public static void main( String[] args )
    {
        startGateway(args);
    }
}
