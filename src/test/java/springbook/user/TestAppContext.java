package springbook.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springbook.user.dao.MockMailSender;
import springbook.user.dao.TestUserService;
import springbook.user.dao.UserDao;

@Configuration
public class TestAppContext {

    @Autowired
    UserDao userDao;

    @Bean
    public TestUserService testUserService() {
        TestUserService testUserService = new TestUserService();
        testUserService.setUserDao(userDao);
        testUserService.setMailSender(new MockMailSender());
        return testUserService;
    }
}
