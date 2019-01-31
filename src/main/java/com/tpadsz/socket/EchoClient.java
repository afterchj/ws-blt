package com.tpadsz.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

/**
 * Created by hongjian.chen on 2018/2/28.
 */

public class EchoClient {

    private static Logger logger = LoggerFactory.getLogger(EchoClient.class);
        private static String host = "122.112.229.195";
//    private static String host = "127.0.0.1";
    private static int port = 8000;
    private static final int length = 110;
    private Socket socket;

    public EchoClient() {}

    public EchoClient(Socket socket) throws IOException {
        this.socket = socket;
    }

    private PrintWriter getWriter(Socket socket) throws IOException {
        OutputStream socketOut = socket.getOutputStream();
        return new PrintWriter(socketOut, true);
    }

    private BufferedReader getReader(Socket socket) throws IOException {
        InputStream socketIn = socket.getInputStream();
        return new BufferedReader(new InputStreamReader(socketIn));
    }

    public void talk() throws IOException {
        System.out.println("请输入内容：");
        try {
            BufferedReader br = getReader(socket);
            PrintWriter pw = getWriter(socket);
            BufferedReader localReader = new BufferedReader(new InputStreamReader(System.in));
            String msg;
            while ((msg = localReader.readLine()) != null) {
                pw.println(msg);
                logger.info(br.readLine());
                if (msg.equals("bye"))
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void send(Socket socket, String msg) {
        try {
            BufferedReader br = getReader(socket);
            PrintWriter pw = getWriter(socket);
            pw.println(msg);
            System.out.println(br.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void multiClient() throws Exception {
        EchoClient client = new EchoClient();
        final Socket[] sockets = new Socket[length];
        for (int i = 0; i < length; i++) {  //试图建立100次连接
            sockets[i] = new Socket(host, port);
            client.send(sockets[i], "第" + (i + 1) + "个客户端发送的消息！");
        }
    }

    public static void main(String args[]) throws Exception {
//        multiClient();
        Socket socket = new Socket(host, port);
        new EchoClient(socket).talk();
    }
}
