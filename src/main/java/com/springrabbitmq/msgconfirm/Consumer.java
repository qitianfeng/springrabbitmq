package com.springrabbitmq.msgconfirm;

import com.rabbitmq.client.*;
import com.springrabbitmq.uitls.RabbitUtils;

import java.io.IOException;

public class Consumer {
    public static void main(String[] args) throws IOException {
        Connection connection = RabbitUtils.connection();
        Channel channel = connection.createChannel();
        String exchangeName = "msgConfirm";
        String routingKey = "info";
        channel.exchangeDeclare(exchangeName,"direct",true,false,null);
        //创建临时队列
        String queue = channel.queueDeclare().getQueue();

        //将队列绑定到交换机并指定routingkey
        channel.queueBind(queue,exchangeName,routingKey);
        channel.basicConsume(queue,true,new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("" + new String(body));

            }
        });

    }
}
