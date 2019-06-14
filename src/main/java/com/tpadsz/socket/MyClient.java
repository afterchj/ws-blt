package com.tpadsz.socket;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

/**
 * Created by hongjian.chen on 2019/2/15.
 */
public class MyClient {

    private static Logger logger= LoggerFactory.getLogger(MyClient.class);
    public static void main(String[] args) throws Exception {
        // 客户端只有一个事件循环组
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            // 客户端启动器
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class).handler(new MyClientInitializer());
            Channel channel = bootstrap.connect("127.0.0.1", 8000).sync().channel();
            logger.warn("请输入指令：");
            Scanner sc = new Scanner(System.in);
            while (true) {
                String input = sc.nextLine();
                if (input != null) {
                    if ("quit".equals(input)) {
                        System.exit(0);
                    }
                    channel.writeAndFlush(input);
                }
            }
        } finally {
//            eventLoopGroup.shutdownGracefully();
        }
    }
}
