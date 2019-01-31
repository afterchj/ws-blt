package com.tpadsz.utils;

import java.net.Socket;

/**
 * Created by hongjian.chen on 2018/3/1.
 */
public class Handler implements Runnable {
    private Socket socket;

    public Handler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        new Service().readLine(socket);
    }
}
