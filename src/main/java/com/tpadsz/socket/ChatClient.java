package com.tpadsz.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;


/**
 * Created by hongjian.chen on 2019/2/18.
 */

public class ChatClient {
    private Logger logger = LoggerFactory.getLogger(ChatClient.class);
    private PrintWriter pw;
    private BufferedReader br;
    private Scanner scan;
    private Socket s;

    public ChatClient() throws IOException {
        s = new Socket("127.0.0.1", 8001);
//        s = new Socket("122.112.229.195", 8001);
    }

    public static void main(String[] args) throws IOException {
        ChatClient chatClient = new ChatClient();
        chatClient.startup();
    }

    public void startup() throws IOException {
        pw = new PrintWriter(s.getOutputStream(), true);
        br = new BufferedReader(new InputStreamReader(s.getInputStream()));
        //开启一个线程监听服务端的消息
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        if (s.isClosed()) {
                            System.exit(0);
                        } else {
                            logger.info("from server:" + br.readLine());
                        }
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        logger.info("status:" + e.getMessage());
                    }
                }
            }
        }).start();
        //主线程负责发送消息
        scan = new Scanner(System.in);
        System.out.println("请输入内容：");
        while (true) {
            String read = scan.nextLine();
            pw.println(read);
            if (read.equalsIgnoreCase("quit")) {
                s.close();
            }
        }
    }

    public void receive() throws IOException {
        byte ss[] = new byte[1024];
        int length = s.getInputStream().read(ss);
        System.out.println(new String(ss, 0, length));
    }
}
