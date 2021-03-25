package com.springrabbitmq.uitls;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitUtils {

    public static ConnectionFactory connectionFactory;

    //在类加载的时候加载
    static {
        connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("101.132.142.155");
        connectionFactory.setPort(5672);
        //虚拟主机
        connectionFactory.setVirtualHost("/ems");
        connectionFactory.setUsername("ems");
        connectionFactory.setPassword("123");
    }

    /**
     *
     * @return 具体的连接
     */
    public static Connection connection() {
        try {
            //直接返回连接
            return connectionFactory.newConnection();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return null;
    }

    public  static void close(Channel channel,Connection connection) {
        try {
            channel.close();
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

}
