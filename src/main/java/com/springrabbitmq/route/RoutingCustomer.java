package com.springrabbitmq.route;

import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RoutingCustomer {

    @RabbitListener(
            bindings = {
                    @QueueBinding(value = @Queue,
                            exchange = @Exchange(value = "direct",type = "direct"),
                    key = {"error"})

            }
    )
    public void receive(String message) {
        System.out.println(message);
    }

    @RabbitListener(
            bindings = {
                    @QueueBinding(value = @Queue,
                            exchange = @Exchange(value = "direct",type = "direct"),
                            key = {"info"})

            }
    )
    public void receive1(String message) {
        System.out.println(message);
    }
}
