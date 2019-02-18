package com.tpadsz.socket;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by hongjian.chen on 2019/2/15.
 */

public class MySocketClient {

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        Socket client = new Socket("127.0.0.1", 8000);
        PrintWriter pw = new PrintWriter(client.getOutputStream());
        BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
        System.out.println("from server:" + reader.readLine());
        pw.write("hello");
//        pw.flush();
//        pw.close();
//        br.close();
    }
}