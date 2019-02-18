//package com.tpadsz.utils;
//
//import com.alibaba.fastjson.JSON;
//
//import org.mybatis.spring.SqlSessionTemplate;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
///**
// * Created by hongjian.chen on 2019/2/13.
// */
//
//@Component
//public class TempJob {
//
//    @Autowired
//    private SqlSessionTemplate sqlSessionTemplate;
//
//    private Logger logger = LoggerFactory.getLogger(TempJob.class);
//
//    private void tempFormat(String format) {
//        String str = format.substring(18, format.length());
//        int len = str.length();
//        String prefix = str.substring(0, 2).toUpperCase();
//        Map map = new ConcurrentHashMap<>();
//        map.put("prefix_value", "03");
//        switch (prefix) {
//            case "52":
//                map.put("ctype", prefix);
//                map.put("cid", str.substring(len - 6, len - 4));
//                break;
//            case "C0":
//                map.put("ctype", prefix);
//                map.put("x", str.substring(4, 6));
//                map.put("y", str.substring(6, 8));
//                break;
//            case "42":
//                map.put("ctype", prefix);
//                map.put("cid", str.substring(len - 4, len - 2));
//                break;
//            default:
//                if ("C1".equals(prefix) || "C4".equals(prefix) || "71".equals(prefix)) {
//                    map.put("ctype", prefix);
//                    map.put("x", str.substring(2, 4));
//                    map.put("y", str.substring(4, 6));
//                    if (!"71".equals(prefix)) {
//                        map.put("cid", str.substring(len - 4, len - 2));
//                    }
//                }
//                break;
//        }
//        sqlSessionTemplate.selectOne("light.saveConsole", map);
//        logger.info("result=" + map.get("result"));
//    }
//
//    private void formatStr(String str) {
//        Map map = new ConcurrentHashMap<>();
//        String prefix = str.substring(0, 2);
//        map.put("prefix_value", prefix);
//        map.put("dmac", str.substring(2, 14));
//        map.put("mesh_id", str.substring(14, 22));
//        map.put("lmac", str.substring(22, 34));
//        map.put("GID", str.substring(34, 36));
//        switch (prefix) {
//            case "01":
//                map.put("code", str.substring(34, 38));
//                map.put("code_version", str.substring(38, str.length()));
//                break;
//            case "02":
//                map.put("GID", str.substring(34, 36));
//                map.put("x", str.substring(36, 38));
//                map.put("y", str.substring(38, 40));
//                map.put("suffix_value", str.substring(40, str.length()));
//                break;
//            default:
//                if ("03".equals(prefix)) {
//                    map.put("ctype", str.substring(34, 36));
//                    map.put("cid", str.substring(36, 38));
//                    map.put("x", str.substring(38, 40));
//                    map.put("y", str.substring(40, str.length()));
//                }
//                break;
//        }
//        sqlSessionTemplate.selectOne("light.saveConsole", map);
//        logger.info("result=" + map.get("result"));
//    }
//
//    @Scheduled(cron = "0/30 * * * * *")
//    public void saveDate1() {
//        logger.info("执行任务1:" +new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
//        tempFormat("77040F0227250000007132320000000000000D");
//    }
//
//    @Scheduled(cron = "0 0/1 * * * *")
//    public void saveDate2() {
//        logger.info("执行任务2:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
//        formatStr("03F0ACD700950108080808000000000000c1011010");
//    }
//}
