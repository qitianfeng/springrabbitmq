package com.springrabbitmq;


import com.springrabbitmq.springbootconfirm.ConfirmCallBackService;
import com.springrabbitmq.springbootconfirm.ReturnCallbackService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.charset.StandardCharsets;
import java.util.UUID;


@SpringBootTest(classes = SRApplication.class)
@RunWith(SpringRunner.class)
public class RabbitMqTest1 {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ConfirmCallBackService confirmCallBackService;

    @Autowired
    private ReturnCallbackService returnCallbackService;

    @Test
    public void sendMsg() {

        String exchange = "confirmExchange";
        String routingKey = "info";
        String msg = "消息来了，速速解决！！！！！！！！！！！";

        /**
         * 确保消息发送失败后可以进行重新返回到队列中
         *
         */
        rabbitTemplate.setMandatory(true);
        /**
         * 消息投递到失败队列的回调处理
         */
        rabbitTemplate.setReturnCallback(returnCallbackService);


        //发送消息
        for (int i = 0; i < 5; i++) {
            rabbitTemplate.convertAndSend(exchange, routingKey, msg.getBytes(StandardCharsets.UTF_8), new MessagePostProcessor() {
                        @Override
                        public Message postProcessMessage(Message message) throws AmqpException {
                            message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                            return message;
                        }
                    },
                    new CorrelationData(UUID.randomUUID().toString())
            );
        }
    }
}
