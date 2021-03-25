package com.springrabbitmq.msgconfirm;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;
import com.springrabbitmq.uitls.RabbitUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Producer {
    public static void main(String[] args) throws IOException, InterruptedException {
        //创建连接
        Connection connection = RabbitUtils.connection();
        //创建通道
        Channel channel = connection.createChannel();
        //交换机名称
        String exchangeName = "msgConfirm";
        String routingKey = "info";
        //创建交换机
        channel.exchangeDeclare(exchangeName, "direct", true, false, null);

        channel.confirmSelect();
        channel.basicPublish(exchangeName, routingKey, MessageProperties.PERSISTENT_BASIC, "成功发送消息".getBytes(StandardCharsets.UTF_8));

        if (channel.waitForConfirms()) {
            System.out.println("发送成功");
        } else {
            System.out.println("发送失败");
        }
        RabbitUtils.close(channel, connection);


    }
}
