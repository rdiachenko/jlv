package com.rdiachenko.jlv;

public class SocketLogServerRunner {

    public static void main(String[] args) throws Exception {
        SocketLogServer server = new SocketLogServer(7777);
        server.start();
        Thread.sleep(20000);
        server.stop();
    }
}
