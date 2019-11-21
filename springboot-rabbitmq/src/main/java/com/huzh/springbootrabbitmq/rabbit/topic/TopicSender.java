package com.huzh.springbootrabbitmq.rabbit.topic;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName TopicSender
 * @Description TODO
 * @Date 2019/11/20 16:55
 * @Author huzh
 * @Version 1.0
 */
@Component
public class TopicSender {

    @Autowired
    AmqpTemplate amqpTemplate;

    public void send1() {
        String context = "hi, i am message 1";
        System.out.println("Sender : " + context);
        amqpTemplate.convertAndSend("topicExchange", "topic.message", context);
    }

    public void send2() {
        String context = "hi, i am messages 2";
        System.out.println("Sender : " + context);
        amqpTemplate.convertAndSend("topicExchange", "topic.messages", context);
    }
}