package com.tpadsz.socket;

import com.tpadsz.utils.BltManager;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by hongjian.chen on 2019/2/15.
 */

public class MyServerHandler extends ChannelInboundHandlerAdapter {

    private static Logger logger = LoggerFactory.getLogger(MyServerHandler.class);
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info(ctx.channel().localAddress().toString() + " 通道已激活！");
    }
    /**
     * @param buf
     * @return
     * @author after
     * TODO  此处用来处理收到的数据中含有中文的时  出现乱码的问题
     * 2017年8月31日 下午7:57:28
     */
    private String getMessage(ByteBuf buf,String ip) {
        String msg;
        byte[] con = new byte[buf.readableBytes()];
        buf.readBytes(con);
        try {
            msg = new String(con, "UTF-8");
            BltManager.saveMap(msg, ip);
        } catch (UnsupportedEncodingException e) {
            logger.error("UnsupportedEncodingException：" + e.getMessage());
            return null;
        }
        return msg;
    }

    /**
     * 功能：读取服务器发送过来的信息
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 第一种：接收字符串时的处理
        Channel channel = ctx.channel();
        ByteBuf buf = (ByteBuf) msg;
        String rev = getMessage(buf,channel.remoteAddress().toString());
        ctx.writeAndFlush(rev);
        logger.info("receive from client:" + rev);
    }

    /**
     * 功能：服务端发生异常的操作
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        logger.error("异常信息：" + cause.getMessage());
    }
}
