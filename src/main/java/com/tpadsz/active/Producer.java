package com.tpadsz.active;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.jms.Queue;
import javax.jms.Topic;

/**
 * Created by hongjian.chen on 2018/12/28.
 */

@Component
@EnableScheduling
public class Producer {

    @Autowired
    private JmsTemplate jmsTemplate;

    @Resource(name = "queue1")
    private Queue queue;

    @Resource(name = "topic1")
    private Topic topic;


//    @Scheduled(fixedDelay = 3000)    // 每3s执行1次
//    public void send() {
//        jmsTemplate.convertAndSend(queue, "after");
//    }
//
//    @Scheduled(fixedDelay = 2000)    // 每2s执行1次
//    public void sendTopic() {
//        jmsTemplate.convertAndSend(topic, "admin");
//    }
}
