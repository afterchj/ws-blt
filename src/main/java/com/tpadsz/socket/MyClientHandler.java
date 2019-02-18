package com.tpadsz.socket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Scanner;


/**
 * Created by hongjian.chen on 2019/2/15.
 */
public class MyClientHandler extends SimpleChannelInboundHandler<String> {

    private Logger logger = LoggerFactory.getLogger(MyClientHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        logger.info("ip:" + ctx.channel().remoteAddress() + ",client output: " + msg);
        Scanner sc = new Scanner(System.in);
        String str = sc.nextLine();
        ctx.writeAndFlush("from client: " + str);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    /**
     * channel处于活动状态后的回调（链接建立好了过后）
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.channel().writeAndFlush("连接已建立！");
    }
}
