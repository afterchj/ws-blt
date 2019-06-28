package com.tpadsz.socket;

import com.tpadsz.utils.BltManager;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.SocketAddress;


/**
 * Created by hongjian.chen on 2019/2/15.
 */

public class MyServerHandler extends ChannelInboundHandlerAdapter {

    private static Logger logger = LoggerFactory.getLogger(MyServerHandler.class);

    public void channelActive(ChannelHandlerContext ctx) {
        logger.info("通道已激活！");
    }

    /**
     * @param buf
     * @return
     * @author hongjian.chen
     * TODO  此处用来处理收到的数据中含有中文的时  出现乱码的问题
     */
    private String getMessage(ByteBuf buf, String ip) {
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
    public void channelRead(ChannelHandlerContext ctx, Object msg){
        // 第一种：接收字符串时的处理
        Channel channel = ctx.channel();
        SocketAddress address = channel.remoteAddress();
        logger.info("[" + address + "] receive:" + msg);
        String str = address.toString();
        String ip = str.substring(1, str.indexOf(":"));
        ByteBuf buf = (ByteBuf) msg;
        String rev = getMessage(buf, ip);
        ctx.writeAndFlush(rev);
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
