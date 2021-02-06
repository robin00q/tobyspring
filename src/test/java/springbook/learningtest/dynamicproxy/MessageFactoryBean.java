package springbook.learningtest.dynamicproxy;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

// 빈으로 등록하자
@Component(value = "message")
public class MessageFactoryBean implements FactoryBean<Message> {
    String text;

    // DI 받기
    public void setText(String text) {
        this.text = text;
    }

    // 실제 빈으로 사용될 오브젝트를 직접 생성한다.
    @Override
    public Message getObject() throws Exception {
        return Message.newMessage(this.text);
    }

    @Override
    public Class<?> getObjectType() {
        return Message.class;
    }

    // 요청마다 다른 빈을 리턴하므로 false
    @Override
    public boolean isSingleton() {
        return false;
    }
}
