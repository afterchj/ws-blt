package com.tpadsz.socket;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created by hongjian.chen on 2019/2/15.
 */
public class MyClient {

    public static void main(String[] args) throws Exception {
        // 客户端只有一个事件循环组
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            // 客户端启动器
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class).handler(new MyClientInitializer());

            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8000).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            eventLoopGroup.shutdownGracefully();
        }
    }
}
