package com.springrabbitmq.springbootconfirm;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;


/***
 * 只有消息被消费者消费，就会触发回调
 */
@Component
public class ConfirmCallBackService implements RabbitTemplate.ConfirmCallback {

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if(!ack) {
            System.out.println("消息发送异常");
        } else {
            System.out.println("发送确认------------------------------------------------------- "+correlationData.getId()+"  "+ack+"  "+cause);
        }

    }
}
