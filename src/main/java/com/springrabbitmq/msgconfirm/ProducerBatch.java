package com.springrabbitmq.msgconfirm;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;
import com.springrabbitmq.uitls.RabbitUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.NavigableSet;
import java.util.TreeSet;

public class ProducerBatch {
    public static void main(String[] args) throws IOException, InterruptedException {
        //创建连接
        Connection connection = RabbitUtils.connection();
        //创建通道
        Channel channel = connection.createChannel();
        //交换机名称
        String exchangeName = "msgConfirm";
        String routingKey = "info";
        //创建交换机
        channel.exchangeDeclare(exchangeName, "direct");

        String queue = channel.queueDeclare().getQueue();

        //储存未确认的消息标识
        NavigableSet<Object> confirmSet = Collections.synchronizedNavigableSet(new TreeSet<>());

        channel.confirmSelect();
        //添加监听
        channel.addConfirmListener(new ConfirmListener() {
            /**
             * 处理返回确认成功
             * @param l
             * @param b
             * @throws IOException
             */
            @Override
            public void handleAck(long l, boolean b) throws IOException {
                if (b) {
                    confirmSet.headSet(l + 1).clear();
                } else {
                    confirmSet.remove(l);
                }
            }

            /**
             * 处理返回失败
             * @param l
             * @param b
             * @throws IOException
             */

            @Override
            public void handleNack(long l, boolean b) throws IOException {
                if (b) {
                    confirmSet.headSet(l + 1).clear();
                } else {
                    confirmSet.remove(l);
                }
            }
        });
        //同步发送多条消息
        for (int i = 0; i < 15; i++) {

            long tag = channel.getNextPublishSeqNo();
            channel.basicPublish(exchangeName, routingKey, MessageProperties.PERSISTENT_TEXT_PLAIN, ("成功发送消息" + i).getBytes(StandardCharsets.UTF_8));
            confirmSet.add(tag);
        }
        channel.waitForConfirmsOrDie();

        RabbitUtils.close(channel, connection);
    }
}
