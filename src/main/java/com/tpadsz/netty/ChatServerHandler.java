package com.tpadsz.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;

/**
 * 服务器主要的业务逻辑
 */
@Component
@ChannelHandler.Sharable
public class ChatServerHandler extends SimpleChannelInboundHandler<String> {


    private static Logger logger = LoggerFactory.getLogger(ChatServerHandler.class);
    //保存所有活动的用户
    public static final ChannelGroup group = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext arg0, String arg1) throws Exception {
        Channel channel = arg0.channel();
//        //当有用户发送消息的时候，对其他用户发送信息
        for (Channel ch : group) {
            ch.writeAndFlush(arg1);
//            if (ch == channel) {
//                ch.writeAndFlush("[you]:" + arg1);
//            }else {
//                channel.writeAndFlush("[weChat]:" + arg1);
//            }
        }
        logger.info("[" + channel.remoteAddress() + "]: " + arg1);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
//        for (Channel ch : group) {
//            ch.writeAndFlush("[" + channel.remoteAddress() + "] " + "is coming");
//        }
        group.add(channel);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
//        for (Channel ch : group) {
//            ch.writeAndFlush("[" + channel.remoteAddress() + "] " + "is remove");
//        }
        group.remove(channel);
    }

    //在建立链接时发送信息
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        logger.info("[" + channel.remoteAddress() + "] " + "online");
    }

    //退出链接
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        logger.info("[" + channel.remoteAddress() + "] " + "offline");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.info("[" + ctx.channel().remoteAddress() + "]" + "exit the room");
        ctx.close().sync();
    }

    //queue模式
//    @JmsListener(destination = "tpad-blt-console-queue", containerFactory = "jmsListenerContainerQueue")
//    public void receive(String text) throws JMSException {
//        //当有用户发送消息的时候，对其他用户发送信息
//        for (Channel ch : group) {
//            ch.writeAndFlush("[weChat]:" + text);
//        }
//        logger.info("receive from tpad-blt-console-queue: " + text);
//    }
}
