package com.springrabbitmq.work;

import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class WorkConsumer {

    @RabbitListener(queuesToDeclare = @Queue(value = "work"))
    public void receive1(String message) {
        System.out.println("1  "+message);
    }
    @RabbitListener(queuesToDeclare = @Queue(value = "work"))
    public void receive(String message) {
        System.out.println("2    "+message);
    }


}
