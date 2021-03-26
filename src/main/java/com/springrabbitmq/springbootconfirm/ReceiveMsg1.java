package com.springrabbitmq.springbootconfirm;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ReceiveMsg1 {
    @RabbitListener(bindings = {
            @QueueBinding(value = @Queue(value = "confirmqueue"),
                    exchange = @Exchange(value = "confirmExchange", type = "topic"),
                    key = {"info"}
            )
    })
    public void receive(byte[] msg, Channel channel, Message message) {
        try {
            System.out.println("2222222222222222-----------------------------------------" + new String(msg));
            //消息确认被消费
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

        } catch (Exception e) {
            if (message.getMessageProperties().getRedelivered()) {
                try {
                    System.out.println("--------消息已经重复处理失败，拒绝再次接收--------------");
                    //消息已经重复处理失败，拒绝再次接收
                    channel.basicReject(message.getMessageProperties().getDeliveryTag(), false); //拒绝消息
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            } else {
                try {
                    System.out.println("--------消息即将再次返回处理 --------------");
                    //消息即将再次返回处理
                    // 参数1：消息投递序号，参数2：是否批量处理，参数3：true，消息重新进入队列
                    channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
//                try {
//                    //消息进行重发
//                    channel.basicPublish(message.getMessageProperties().getReceivedExchange(),
//                            message.getMessageProperties().getReceivedRoutingKey(), MessageProperties.PERSISTENT_TEXT_PLAIN
//                            , msg);
//                } catch (IOException ioException) {
//                    ioException.printStackTrace();
//                }
            }
        }
    }
}
