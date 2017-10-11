package com.beyond.bpush.app;

import com.beyond.bpush.publisher.ServerMain;

public class PublisherApp {

    public static void startPublisher(final String[] args){
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
        startPublisher(args);
    }
}
