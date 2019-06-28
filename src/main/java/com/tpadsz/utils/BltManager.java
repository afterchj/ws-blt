package com.tpadsz.utils;

import com.alibaba.fastjson.JSON;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by hongjian.chen on 2019/2/19.
 */

public class BltManager {

    private static Logger logger = LoggerFactory.getLogger(BltManager.class);
    private static SqlSessionTemplate sqlSessionTemplate = SpringUtils.getSqlSession();

    public static void saveMap(String msg, String ip) {
//        System.out.println("sqlSessionTemplate=" + sqlSessionTemplate);
        msg = msg.trim();
        int len = msg.length();
        if (len >= 22 && len <= 40) {
            tempFormat(msg);
        } else if (len > 40) {
            formatStr(msg);
        } else {
            Map map = new HashMap();
            map.put("ip", ip);
            map.put("msg", msg);
            sqlSessionTemplate.insert("light.insertLog", map);
        }
    }

    public static void tempFormat(String format) {
        String str = format.substring(18);
        int len = str.length();
        logger.info("str=" + str + ",len=" + len);
        String prefix = str.substring(0, 2).toUpperCase();
        String tmp = str.substring(2, 4);
        String cid=str.substring(len - 4, len - 2);
        Map map = new ConcurrentHashMap<>();
        map.put("prefix_value", "03");
        switch (prefix) {
            case "52"://52表示遥控器控制命令，01,02字段固定，01表示开，02表示关
                map.put("ctype", prefix);
                map.put("cid", str.substring(len - 6, len - 4));
                break;
            case "C0"://pad或手机，C0代表全控，37 37字段是x、y值
                map.put("ctype", prefix);
                map.put("x", str.substring(4, 6));
                map.put("y", str.substring(6, 8));
                break;
            case "42": //42代表场景控制，02字段是场景ID
                map.put("ctype", prefix);
                map.put("cid", cid);
                break;
            case "CA":
                //门磁,77 04 0E 02 20 9D 01 00 00 CA 00  关门,77 04 0E 02 20 9D 01 00 00 CA 01   开门
                map.put("ctype", prefix);
                map.put("cid", prefix);
                map.put("x", tmp);
                break;
            case "CB":
//                人感 ,77 04 0E 02 20 9D 01 00 00 CB 00  无人,77 04 0E 02 20 9D 01 00 00 CB 01  有人
                map.put("ctype", prefix);
                map.put("cid", "CB");
                map.put("x", tmp);
                break;
            case "CC":
//                温湿度 77 04 0E 02 20 9D 01 00 00 CC, 温度 00 00 湿度  00 00
                map.put("ctype", prefix);
                map.put("cid", tmp);
                map.put("x", str.substring(4, 8));
                map.put("y", str.substring(8, 12));
                break;
            default:
                //C1代表组控，32 32字段是x、y值, 02字段是组ID
//                RGB组控77 04 10 02 20 95 00 00 00 C4 5F 02 00 00 00 00 00 00 02 4F
//                灯状态信息77 04 0F 02 27 35 00 00 00 71 00 13 00 00 00 00 00 00 0E
                if ("C1".equals(prefix) || "C4".equals(prefix) || "71".equals(prefix)) {
                    map.put("ctype", prefix);
                    map.put("x", str.substring(2, 4));
                    map.put("y", str.substring(4, 6));
                    map.put("cid", cid);
                }
                break;
        }
//        amqpTemplate.convertAndSend(ROUTING_KEY, JSON.toJSONString(map));
        sqlSessionTemplate.selectOne("light.saveConsole", map);
        logger.info("result=" + map.get("result"));
    }

    private static void formatStr(String str) {
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
//        sqlSessionTemplate.selectOne("light.saveConsole", map);
//        logger.info("result=" + map.get("result"));
        logger.info("result=" + JSON.toJSONString(map));
    }

    public static void main(String[] args) {
        formatStr("03F0ACD700950108080808000000000000c1011010");
    }
}
