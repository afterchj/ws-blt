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
//        private static String host = "122.112.229.195";
    private static String host = "127.0.0.1";
    private static int port = 8001;
//    private static final int length = 110;
    private Socket socket;

    public EchoClient() {
    }

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
            while ((msg=localReader.readLine())!=null) {
                String str = br.readLine();
                pw.println(socket.getLocalPort()+":"+str);
                pw.println(msg);
                logger.info("receive message:" + br.readLine());
                if (msg.equals("bye"))
                    break;
            }
            br.close();
            pw.close();
            localReader.close();
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

    public static void main(String args[]) throws Exception {
//        multiClient();
        Socket socket = new Socket(host, port);
        new EchoClient(socket).talk();
    }
}
