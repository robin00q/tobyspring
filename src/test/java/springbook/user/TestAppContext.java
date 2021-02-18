package springbook.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.MailSender;
import springbook.user.dao.MockMailSender;
import springbook.user.dao.TestUserService;
import springbook.user.dao.UserDao;

@Configuration
@Profile("test")
public class TestAppContext {

    @Autowired
    UserDao userDao;

    @Bean
    public TestUserService testUserService() {
        TestUserService testUserService = new TestUserService();
        testUserService.setUserDao(userDao);
        testUserService.setMailSender(mailSender());
        return testUserService;
    }

    @Bean
    public MailSender mailSender() {
        return new MockMailSender();
    }
}
