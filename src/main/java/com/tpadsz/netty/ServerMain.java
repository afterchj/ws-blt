package com.tpadsz.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 多人聊天例子服务器
 *
 * @author lkj41110
 * @version time：2017年1月16日 下午9:54:55
 */
public class ServerMain {


    private static Logger logger = LoggerFactory.getLogger(ServerMain.class);

    public static void main(String[] args) {
        new ServerMain().run(8001);
    }

    public void run(int port) {
        EventLoopGroup acceptor = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();
        final ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.option(ChannelOption.SO_BACKLOG, 1024); //设置TCP相关信息
        bootstrap.option(ChannelOption.ALLOW_HALF_CLOSURE, true);
        bootstrap.group(acceptor, worker);//设置循环线程组，前者用于处理客户端连接事件，后者用于处理网络IO
        bootstrap.channel(NioServerSocketChannel.class);//用于构造socketchannel工厂
        bootstrap.handler(new ChatServerHandler());
        bootstrap.childHandler(new ServerIniterHandler());//为处理accept客户端的channel中的pipeline添加自定义处理函数
        try {
            // 服务器绑定端口监听
            final ChannelFuture bind = bind(bootstrap, port);
            Channel channel = bind.sync().channel();
            logger.info("server start running in port:" + port);
            // 监听服务器关闭监听
            //channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 退出
            //acceptor.shutdownGracefully();
            //worker.shutdownGracefully();
        }
    }

    private static ChannelFuture bind(final ServerBootstrap serverBootstrap, final int port) {
        return serverBootstrap.bind(port).addListener(future -> {
            if (future.isSuccess()) {
                logger.info("端口[" + port + "]绑定成功!");
            } else {
               logger.info("端口[" + port + "]绑定失败!");
                bind(serverBootstrap, port + 1);
            }
        });
    }
}
