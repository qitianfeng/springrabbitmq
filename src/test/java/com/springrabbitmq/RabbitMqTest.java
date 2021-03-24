package com.springrabbitmq;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@SpringBootTest(classes = SRApplication.class)
@RunWith(SpringRunner.class)
public class RabbitMqTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    public void test() {
        rabbitTemplate.convertAndSend("hello", "hello world");
    }

    @Test
    public void testWork() {
        for (int i = 0; i < 10; i++) {
            rabbitTemplate.convertAndSend("work", "hello work 模型" + i);
        }
    }

    @Test
    public void testFanout(){
        rabbitTemplate.convertAndSend("logs","","fanout");
    }

    @Test
    public void testRouting() {
        rabbitTemplate.convertAndSend("direct","info","hello info");
    }
    @Test
    public void testTopic() {
        rabbitTemplate.convertAndSend("topic","user.save","hello topic");
    }
}
