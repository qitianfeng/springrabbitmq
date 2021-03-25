package com.springrabbitmq.model.topic;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class TopicCustomer {

    @RabbitListener(
            bindings = {
                    @QueueBinding(value = @Queue,
                            exchange = @Exchange(value = "topic",type = "topic"),
                    key = {"user.save","user.*"})
            }
    )
    public void receive(String message) {
        System.out.println(message);
    }
    @RabbitListener(
            bindings = {
                    @QueueBinding(value = @Queue,
                            exchange = @Exchange(value = "topic",type = "topic"),
                            key = {"quser.save","uqser.*"})
            }
    )
    public void receive2(String message) {
        System.out.println(message);
    }
}
