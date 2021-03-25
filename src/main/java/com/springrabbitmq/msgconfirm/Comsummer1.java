package com.springrabbitmq.msgconfirm;

import com.rabbitmq.client.*;
import com.springrabbitmq.uitls.RabbitUtils;

import java.io.IOException;

public class Comsummer1 {
    public static void main(String[] args) throws IOException {
        Connection connection = RabbitUtils.connection();
        Channel channel = connection.createChannel();
        //声明 交换机 以及 通道类型
        channel.exchangeDeclare("msgConfirm", "direct",true,false,null);

        //创建一个临时队列
        String queue = channel.queueDeclare().getQueue();

        //基于route key 绑定队列和交换机,参数3：routingkey:只有与producer的routingkey相同，才能对生产者的消息进行消费
        //可以根据不同的routing key 进行队列的绑定
        channel.queueBind(queue, "msgConfirm", "info");

        channel.basicConsume(queue, true, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println(new String(body));
            }
        });

    }
}
