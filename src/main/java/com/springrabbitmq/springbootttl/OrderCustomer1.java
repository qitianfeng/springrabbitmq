package com.springrabbitmq.springbootttl;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class OrderCustomer1 {

    @RabbitListener(bindings = {
            @QueueBinding(value = @Queue(value = "direct.delay.queue"),
                    exchange = @Exchange(value = "direct.delay.exchange", type = "direct"),
                    key = {"DelayKey"}
            )
    })
    public void receive(byte[] msg, Channel channel, Message message) throws IOException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
            System.out.println("-----------------------------------------" + new String(msg) + simpleDateFormat.format(new Date()));
            //消息确认被消费
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);


    }


}
