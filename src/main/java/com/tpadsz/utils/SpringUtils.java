//package com.tpadsz.utils;
//
//import com.tpadsz.WsBltApplication;
//import org.mybatis.spring.SqlSessionTemplate;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.annotation.AnnotationConfigApplicationContext;
//
///**
// * Created by hongjian.chen on 2019/2/15.
// */
//public class SpringUtils {
//
//    private static ApplicationContext ctx;
//
//    static {
//        ctx = new AnnotationConfigApplicationContext(WsBltApplication.class);
//    }
//
//    public static SqlSessionTemplate getSqlSessionTemplate() {
//        return (SqlSessionTemplate) ctx.getBean("mySqlSessionTemplate");
//    }
//}
