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
        //一次处理一条消息
        channel.basicQos(1);

        channel.exchangeDeclare(exchangeName,"direct",true,false,null);
        //创建临时队列
        String queue = channel.queueDeclare().getQueue();


        //将队列绑定到交换机并指定routingkey
        channel.queueBind(queue,exchangeName,routingKey);
        //参数2 ，true ，消息自动确认
        channel.basicConsume(queue,false,new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //为false，表示消息没被消费过，反之，被消费了
                boolean redeliver = envelope.isRedeliver();
                System.out.println("" + new String(body));
                // 手动确认消息，应答 参数一，消息编号，参数二，true，表示确认小于等于当前编号的所有消息，false，表示单个确认当前消息
                channel.basicAck(envelope.getDeliveryTag(),false);
            }
        });

    }
}
