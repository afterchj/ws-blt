package com.tpadsz.socket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.codec.string.StringEncoder;
import org.slf4j.Logger;

import java.nio.charset.Charset;

/**
 * Created by hongjian.chen on 2019/2/15.
 */
public class NettyServer {

    private static Logger logger = org.slf4j.LoggerFactory.getLogger(NettyServer.class);

    public static void start(int port) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            ServerBootstrap sb = new ServerBootstrap();
            sb.option(ChannelOption.SO_BACKLOG, 1024);
            sb.group(group, bossGroup) // 绑定线程池
                    .channel(NioServerSocketChannel.class) // 指定使用的channel
                    .localAddress(port)// 绑定监听端口
                    .childHandler(new ChannelInitializer<SocketChannel>() { // 绑定客户端连接时候触发操作

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            logger.info("新客户端链接IP:" + ch.localAddress().getHostName() + ",Port:" + ch.localAddress().getPort());
                            ch.pipeline().addLast(new StringEncoder(Charset.forName("GBK")));
                            ch.pipeline().addLast(new MyServerHandler()); // 客户端触发操作
                            ch.pipeline().addLast(new ByteArrayEncoder());
                        }
                    });
            ChannelFuture cf = sb.bind().sync(); // 服务器异步创建绑定
            cf.channel().closeFuture().sync(); // 关闭服务器通道
        } catch (Exception e) {
            logger.error("异常：" + e.getMessage());
        } finally {
            try {
                group.shutdownGracefully().sync(); // 释放线程池资源
                bossGroup.shutdownGracefully().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

//    public static void main(String[] args) throws InterruptedException {
//        start(8000);
//        start(8001);
//    }
}
