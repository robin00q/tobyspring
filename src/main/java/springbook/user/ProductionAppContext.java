package springbook.user;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSender;
import springbook.user.mail.DummyMailSender;

@Configuration
public class ProductionAppContext {

    @Bean
    @ConditionalOnMissingBean(MailSender.class)
    public MailSender mailSender() {
        return new DummyMailSender();
    }
}
