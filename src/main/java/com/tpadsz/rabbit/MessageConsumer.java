//package com.tpadsz.rabbit;
//
//import com.alibaba.fastjson.JSON;
//import org.mybatis.spring.SqlSessionTemplate;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.Map;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
////@Component
//public class MessageConsumer {
//    @Autowired
//    private SqlSessionTemplate sqlSessionTemplate;
//
//    private static final int POOL_SIZE = 20;  //单个CPU时线程池中工作线程的数目
//    private static ExecutorService executorService = Executors.newFixedThreadPool(POOL_SIZE);  //线程池
//    private Logger logger = LoggerFactory.getLogger(MessageConsumer.class);
//
//    @RabbitListener(queues = "tpad-blt-console-queue")
//    public void remoteMsg(String message) throws InterruptedException {
//        logger.info("f_light_command:" + message);
//        try {
//            Map map = JSON.parseObject(message, Map.class);
//            executorService.submit(new MsgHandler(map, "console"));
//        } catch (Exception e) {
//            logger.error("消息格式错误：" + e.getMessage());
//        }
//    }
//
//    @RabbitListener(queues = "tpad-blt-log-queue")
//    public void processMsg(String message) throws InterruptedException {
//        logger.info("f_light_console:" + message);
//        try {
//            Map map = JSON.parseObject(message, Map.class);
//            executorService.submit(new MsgHandler(map, "log"));
//        } catch (Exception e) {
//            logger.error("消息格式错误：" + e.getMessage());
//        }
//    }
//
//    private class MsgHandler implements Runnable {
//        private Map map;
//        private String flag;
//
//        public MsgHandler(Map<String, String> map, String flag) {
//            this.map = map;
//            this.flag = flag;
//        }
//
//        @Override
//        public void run() {
//            try {
//                if ("console".equals(flag)) {
//                    sqlSessionTemplate.selectOne("light.saveConsole", map);
//                    logger.info("result=" + map.get("result"));
//                } else {
//                    sqlSessionTemplate.insert("light.insertLog", map);
//                }
//            } catch (Exception e) {
//                logger.error("数据插入异常：" + e.getMessage());
//            }
//        }
//    }
//}
