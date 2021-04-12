package com.springrabbitmq.springbootttl;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class DelayQueue {


    @Bean("directExchange")
    public DirectExchange directExchange() {
        return new DirectExchange("direct.pay.exchange");
    }

    @Bean("queue")
    public Queue queue() {
        Map<String, Object> map = new HashMap<>();
        map.put("x-dead-letter-exchange", "direct.delay.exchange");
        map.put("x-dead-letter-routing-key", "DelayKey");
        map.put("x-message-ttl", 10000);
        return new Queue("direct.pay.queue", true, false, false, map);

    }

    @Bean
    private Binding binding(Queue queue, DirectExchange directExchange) {
        return BindingBuilder.bind(queue).to(directExchange).with("orderPay");
    }

}
