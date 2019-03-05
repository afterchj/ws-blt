package com.tpadsz.utils;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by hongjian.chen on 2019/1/22.
 */
public class SpringUtils {

    private static ApplicationContext ctx = null;

    static {
        ctx = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
    }

    public static SqlSessionTemplate getSqlSession() {
        return (SqlSessionTemplate) ctx.getBean("mySqlSessionTemplate");
    }

//    public static AmqpTemplate getAmqpTemplate() {
//        return (AmqpTemplate) ctx.getBean("amqpTemplate");
//    }

//    public static MessageProducer getProducer() {
//        return ctx.getBean(MessageProducer.class);
//    }

//    public static void main(String[] args) {
//        System.out.println("mySqlSessionTemplate="+getSqlSession());
//    }
}
