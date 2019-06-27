package com.tpadsz.active;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;

import javax.jms.ConnectionFactory;
import javax.jms.Queue;
import javax.jms.Topic;


/**
 * Created by hongjian.chen on 2018/12/28.
 */

@Configuration
public class ActiveConfig {

    /**
     * 自定义了4个Destination,两个queue,两个topic
     */
    @Bean
    public Queue queue1() {
        return new ActiveMQQueue("queue-1");
    }

    @Bean
    public Topic topic1() {
        return new ActiveMQTopic("topic-1");
    }

    @Bean
    public ConnectionFactory connectionFactory() {
//        System.out.println("spring.activemq.broker-url=" + url);
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
        connectionFactory.setBrokerURL("failover://tcp://119.3.49.192:61616");
        connectionFactory.setUserName("tpad");
        connectionFactory.setPassword("Tp123456");
        connectionFactory.setTrustAllPackages(true);
        return connectionFactory;
    }
    // topic模式的ListenerContainer
    @Bean
    public JmsListenerContainerFactory<?> jmsListenerContainerTopic(ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory bean = new DefaultJmsListenerContainerFactory();
        //开启topic支持
        bean.setPubSubDomain(true);
        bean.setConnectionFactory(connectionFactory);
        return bean;
    }
    // queue模式的ListenerContainer
    @Bean
    public JmsListenerContainerFactory<?> jmsListenerContainerQueue(ConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory bean = new DefaultJmsListenerContainerFactory();
        bean.setConnectionFactory(connectionFactory);
        return bean;
    }


//    @Bean
//    public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory) {
//        System.out.println("初始化jsmTemplate。。。");
//        JmsTemplate jmsTemplate = new JmsTemplate();
//        jmsTemplate.setDeliveryMode(2);//设置持久化，1 非持久， 2 持久化
//        jmsTemplate.setMessageConverter(new InnerMessageConverter());
//        jmsTemplate.setConnectionFactory(connectionFactory);
//        return jmsTemplate;
//    }


}
