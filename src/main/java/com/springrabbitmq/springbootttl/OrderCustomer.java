package com.springrabbitmq.springbootttl;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

//@Component
public class OrderCustomer {

    @RabbitListener(bindings = {
            @QueueBinding(value = @Queue(value = "direct.pay.queue"),
                    exchange = @Exchange(value = "direct.pay.exchange", type = "direct"),
                    arguments = {
                            // 声明死信交换机
                            @Argument(name = "x-dead-letter-exchange", value = "direct.delay.exchange"),
                            //声明死信路由键
                            @Argument(name = "x-dead-letter-routing-key", value = "Delaykey"),
                            //声明队列消息过期时间
                            @Argument(name = "x-message-ttl", value = "100000",type = "java.lang.Integer")
                    },
                    key = {"orderPay"}
            )
    })
    public void receive(byte[] msg, Channel channel, Message message) throws IOException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("--------------消息正常---------------------------" + new String(msg)+simpleDateFormat.format(new Date()));
        //消息确认被消费
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

    }


}
