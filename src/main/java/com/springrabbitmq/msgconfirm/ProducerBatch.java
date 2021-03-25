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

        //添加监听，消息发送失败处理，异步消息确认监听器，需要在发送消息前启动
        channel.addConfirmListener(new ConfirmListener() {
            /**
             * 处理返回确认成功
             * @param l 为确认的消息的编号， 从1开始自动递增用于标记当前是第几个消息
             * @param multitude 为当前消息是否同时确认了多个    为true，则表示本次确认了多条消息，消息等于当前参数1（消息编号）的所有消息
             *                  为false 则表示只确认了当前编号的消息
             * @throws IOException
             */
            @Override
            public void handleAck(long l, boolean multitude) throws IOException {
                if (multitude) {
                    confirmSet.headSet(l + 1).clear();
                } else {
                    confirmSet.remove(l);
                }
            }

            /**
             * 处理返回失败
             * @param l 为没有被确认的消息，需要重新进行消息补发
             * @param multitude  如果为true，则表示小于当前编号的所以的消息可能都没有发送成功需要进行消息的补发
             *                   为false，则表示当前编号的消息没法发送成功需要进行补发
             * @throws IOException
             */
            @Override
            public void handleNack(long l, boolean multitude) throws IOException {
                if (multitude) {
                    confirmSet.headSet(l + 1).clear();
                } else {
                    confirmSet.remove(l);
                }
            }
        });
        //发送多条消息
        for (int i = 0; i < 15; i++) {

            long tag = channel.getNextPublishSeqNo();
            channel.basicPublish(exchangeName, routingKey, MessageProperties.PERSISTENT_TEXT_PLAIN, ("成功发送消息" + i).getBytes(StandardCharsets.UTF_8));
            confirmSet.add(tag);
        }

        RabbitUtils.close(channel, connection);
    }
}
