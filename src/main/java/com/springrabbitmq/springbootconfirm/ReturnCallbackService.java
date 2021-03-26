package com.springrabbitmq.springbootconfirm;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class ReturnCallbackService implements RabbitTemplate.ReturnCallback {
    @Override
    public void returnedMessage(Message message, int replayCode, String replayText, String exchangeName, String routingKey) {

    }
}
