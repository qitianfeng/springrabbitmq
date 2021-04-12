package com.springrabbitmq.springbootttl;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

//@Component
public class OrderDeadCustomer {

    @RabbitListener(bindings = {
            @QueueBinding( value = @Queue(value = "direct.delay.queue"),
                    exchange = @Exchange(value = "direct.delay.exchange", type = "direct"),
                    key = {"Delaykey"}
            )
    })
    public void receive(byte[] msg, Channel channel, Message message) throws IOException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");

        System.out.println("---------------------------------------死信队列--" + new String(msg));

        //消息确认被消费
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);

    }


}
