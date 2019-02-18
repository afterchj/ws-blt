package com.tpadsz.socket;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by hongjian.chen on 2019/21/1.
 */
public class EchoServer {

    private static Logger logger = LoggerFactory.getLogger(EchoServer.class);

    private static ServerSocket serverSocket = null;
    private static ExecutorService executorService;  //线程池
    private static final int POOL_SIZE = 20;  //单个CPU时线程池中工作线程的数目

    List<SocketHandler> clients = new ArrayList<>();


    static {
        try {
            serverSocket = new ServerSocket(8001);
            logger.info("服务器已启动...");
        } catch (IOException e) {
            logger.error("serverSocket启动异常：" + e.getMessage());
            try {
                serverSocket.close();
            } catch (IOException e1) {
                logger.error("serverSocket status：" + serverSocket);
            }
        }
        executorService = Executors.newFixedThreadPool(POOL_SIZE);
    }

    private void service() {
        while (true) {
            Socket socket;
            try {
                socket = serverSocket.accept();
                executorService.execute(new SocketHandler(socket));
            } catch (IOException e) {
                logger.error("socket连接失败：" + e.getMessage());
            }
        }
    }

    private class SocketHandler implements Runnable {
        private Socket socket;
        private BufferedReader br;
        private PrintWriter pw;
        private String name;
        private Boolean flag = true;

        public SocketHandler(Socket socket) throws IOException {
            this.socket = socket;
            br = getReader(socket);
            pw = getWriter(socket);
            name = "[" + socket.getRemoteSocketAddress().toString() + "]";
            logger.info(name + " 加入该聊天室");
            clients.add(this);
            send(name + " 加入该聊天室");
        }

        private void send(String message) {
            for (SocketHandler socketHandler : clients) {
                logger.info("-->已向线程" + socketHandler.name + "发送消息");
                socketHandler.pw.println(message);
                socketHandler.pw.flush();
            }
        }

        private PrintWriter getWriter(Socket socket) {
            OutputStream socketOut;
            try {
                socketOut = socket.getOutputStream();
                return new PrintWriter(new OutputStreamWriter(socketOut, "UTF-8"), true);
            } catch (IOException e) {
                logger.error("获取输出流异常！" + e.getMessage());
            }
            return null;
        }

        private BufferedReader getReader(Socket socket) {
            InputStream socketIn;
            try {
                socketIn = socket.getInputStream();
                return new BufferedReader(new InputStreamReader(socketIn, "UTF-8"));
            } catch (IOException e) {
                logger.error("获取输入流异常！" + e.getMessage());
            }
            return null;
        }

        public void run() {
            String ip = socket.getInetAddress().getHostAddress();
            int port = socket.getPort();
            logger.info("New connection accepted " + ip + ":" + port);
            BufferedReader br = getReader(socket);
            String msg;
            try {
                while ((msg = br.readLine()) != null) {
                    logger.info("send message " + msg);
                    send(msg);
                }
            } catch (IOException e) {
                logger.info("status:" + e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        new EchoServer().service();
    }
}
