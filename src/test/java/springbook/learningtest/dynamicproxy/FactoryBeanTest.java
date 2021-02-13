package springbook.learningtest.dynamicproxy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ContextConfiguration(classes = MessageFactoryBean.class)
public class FactoryBeanTest {

    @Autowired
    ApplicationContext context;

    @Autowired
    Message message;

    @Test
    void getMessageFromFactoryBean() {
        Object messageByContext = context.getBean("message");
        assertTrue(messageByContext instanceof Message);
    }

    @Test
    void getFactoryBean() {
        Object factory = context.getBean("&message");
        assertTrue(factory instanceof MessageFactoryBean);
    }

    @Test
    void test() {
        assertTrue(message instanceof Message);
    }
}
