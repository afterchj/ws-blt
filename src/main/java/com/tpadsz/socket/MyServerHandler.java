package com.tpadsz.socket;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


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
    private String getMessage(ByteBuf buf) {
        Map map = new HashMap<>();
        map.put("ip", "0.0.0.0");
        String msg;
        byte[] con = new byte[buf.readableBytes()];
        buf.readBytes(con);
        try {
            msg = new String(con, "UTF-8");
            int len = msg.length();
            if (len >= 36 && len <= 40) {
                tempFormat(msg);
            } else if (len > 40) {
                formatStr(msg);
            } else {
                map.put("msg", msg);
            }
            logger.info("send message " + msg);
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
        ByteBuf buf = (ByteBuf) msg;
        String rev = getMessage(buf);
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

    private void tempFormat(String format) {
        String str = format.substring(18, format.length());
        int len = str.length();
        String prefix = str.substring(0, 2).toUpperCase();
        Map map = new ConcurrentHashMap<>();
        map.put("prefix_value", "03");
        switch (prefix) {
            case "52":
                map.put("ctype", prefix);
                map.put("cid", str.substring(len - 6, len - 4));
                break;
            case "C0":
                map.put("ctype", prefix);
                map.put("x", str.substring(4, 6));
                map.put("y", str.substring(6, 8));
                break;
            case "42":
                map.put("ctype", prefix);
                map.put("cid", str.substring(len - 4, len - 2));
                break;
            default:
                if ("C1".equals(prefix) || "C4".equals(prefix) || "71".equals(prefix)) {
                    map.put("ctype", prefix);
                    map.put("x", str.substring(2, 4));
                    map.put("y", str.substring(4, 6));
                    if (!"71".equals(prefix)) {
                        map.put("cid", str.substring(len - 4, len - 2));
                    }
                }
                break;
        }
        logger.info("result=" + map.get("result"));
    }

    private void formatStr(String str) {
        Map map = new ConcurrentHashMap<>();
        String prefix = str.substring(0, 2);
        map.put("prefix_value", prefix);
        map.put("dmac", str.substring(2, 14));
        map.put("mesh_id", str.substring(14, 22));
        map.put("lmac", str.substring(22, 34));
        map.put("GID", str.substring(34, 36));
        switch (prefix) {
            case "01":
                map.put("code", str.substring(34, 38));
                map.put("code_version", str.substring(38, str.length()));
                break;
            case "02":
                map.put("GID", str.substring(34, 36));
                map.put("x", str.substring(36, 38));
                map.put("y", str.substring(38, 40));
                map.put("suffix_value", str.substring(40, str.length()));
                break;
            default:
                if ("03".equals(prefix)) {
                    map.put("ctype", str.substring(34, 36));
                    map.put("cid", str.substring(36, 38));
                    map.put("x", str.substring(38, 40));
                    map.put("y", str.substring(40, str.length()));
                }
                break;
        }
        logger.info("result=" + map.get("result"));
    }
}
