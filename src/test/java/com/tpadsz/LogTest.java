package com.tpadsz;


import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by hongjian.chen on 2018/8/1.
 */
@Component
@ConfigurationProperties(prefix = "my.rabbit")
public class LogTest {

    private String queue1;
    private String queue2;

    public void setQueue1(String queue1) {
        this.queue1 = queue1;
    }

    public void setQueue2(String queue2) {
        this.queue2 = queue2;
    }

    private static Logger logger = LoggerFactory.getLogger(LogTest.class);

    public void testLog() {

        // 记录debug级别的信息
        logger.debug("This is debug message.");
        // 记录info级别的信息
        logger.info("This is info message.");
        // 记录error级别的信息
        logger.error("This is error message.");
    }

    public void testKey() {
        logger.info(queue1 + "<-1-2->" + queue2);
    }
}