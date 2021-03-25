package com.springrabbitmq.model.zhilian;

import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queuesToDeclare = @Queue(value = "hello",durable = "false",autoDelete = "false"))
public class RabbitMqConsumer {
    @RabbitHandler
    public void receive(String message) {
        System.out.println(message);
    }
}
