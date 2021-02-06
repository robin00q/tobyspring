package springbook.user.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.PlatformTransactionManager;
import springbook.user.dao.factory.TxProxyFactoryBean;
import springbook.user.domain.Level;
import springbook.user.domain.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static springbook.user.dao.UserServiceImpl.MIN_LOGOUT_FOR_SILVER;
import static springbook.user.dao.UserServiceImpl.MIN_RECOMMEND_FOR_GOLD;

@SpringBootTest
class UserServiceImplTest {

    @Autowired
    UserServiceImpl userServiceImpl;

    @Autowired
    UserDao userDao;

    @Autowired
    PlatformTransactionManager transactionManager;

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    MailSender mailSender;

    List<User> users;

    @BeforeEach
    void setUp() {
        users = Arrays.asList(
                new User("user1", "이석준1", "email1.com", "111111", Level.BASIC, MIN_LOGOUT_FOR_SILVER - 1, 0),
                new User("user2", "이석준2", "email2.com", "222222", Level.BASIC, MIN_LOGOUT_FOR_SILVER, 0),
                new User("user3", "이석준3", "email3.com", "333333", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD - 1),
                new User("user4", "이석준4", "email4.com", "444444", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD),
                new User("user5", "이석준5", "email5.com", "555555", Level.GOLD, 60, Integer.MAX_VALUE)
        );
    }

    @Test
    void add() {
        userDao.deleteAll();

        User userWithLevel = users.get(4);
        User userWithoutLevel = users.get(0);
        userWithoutLevel.setLevel(null);

        userServiceImpl.add(userWithLevel);
        userServiceImpl.add(userWithoutLevel);

        User userWithLevelRead = userDao.get(userWithLevel.getId());
        User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());

        assertEquals(userWithLevel.getLevel(), userWithLevelRead.getLevel());
        assertEquals(Level.BASIC, userWithoutLevelRead.getLevel());
    }

    static class MockUserDao implements UserDao {

        private List<User> users;
        private List<User> updated = new ArrayList<>();

        private MockUserDao(List<User> users) {
            this.users = users;
        }

        public List<User> getUpdated() {
            return updated;
        }

        @Override
        public List<User> getAll() {
            return this.users;
        }

        @Override
        public void update(User user) {
            updated.add(user);
        }

        @Override
        public void add(User user) {
            throw new UnsupportedOperationException();
        }

        @Override
        public User get(String id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void deleteAll() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int getCount() {
            throw new UnsupportedOperationException();
        }
    }

    @Test
    void upgradeLevels_isolated() {
        UserServiceImpl userServiceImpl = new UserServiceImpl();

        MockUserDao mockUserDao = new MockUserDao(this.users);
        userServiceImpl.setUserDao(mockUserDao);

        MockMailSender mockMailSender = new MockMailSender();
        userServiceImpl.setMailSender(mockMailSender);

        userServiceImpl.upgradeLevels();

        List<User> updated = mockUserDao.getUpdated();
        assertEquals(2, updated.size());
        checkUserAndLevel(updated.get(0), "user2", Level.SILVER);
        checkUserAndLevel(updated.get(1), "user4", Level.GOLD);

        List<String> request = mockMailSender.getRequests();
        assertEquals(2, request.size());
        assertEquals(users.get(1).getEmail(), request.get(0));
        assertEquals(users.get(3).getEmail(), request.get(1));

    }

    private void checkUserAndLevel(User updated, String expectedId, Level expectedLevel) {
        assertEquals(expectedId, updated.getId());
        assertEquals(expectedLevel, updated.getLevel());
    }

    @Test
    void mockUpgradeLevels() {
        UserServiceImpl userServiceImpl = new UserServiceImpl();

        UserDao mockUserDao = mock(UserDao.class);
        when(mockUserDao.getAll()).thenReturn(this.users);
        userServiceImpl.setUserDao(mockUserDao);

        MailSender mockMailSender = mock(MailSender.class);
        userServiceImpl.setMailSender(mockMailSender);

        userServiceImpl.upgradeLevels();

        verify(mockUserDao, times(2)).update(any(User.class));
        verify(mockUserDao).update(users.get(1));
        assertEquals(Level.SILVER, users.get(1).getLevel());
        verify(mockUserDao).update(users.get(3));
        assertEquals(Level.GOLD, users.get(3).getLevel());

        ArgumentCaptor<SimpleMailMessage> mailMessageArg = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mockMailSender, times(2)).send(mailMessageArg.capture());
        List<SimpleMailMessage> mailMessages = mailMessageArg.getAllValues();
        assertEquals(users.get(1).getEmail(), mailMessages.get(0).getTo()[0]);
        assertEquals(users.get(3).getEmail(), mailMessages.get(1).getTo()[0]);
    }

    @Test
    @DirtiesContext
    void upgradeLevels() {
        userDao.deleteAll();
        for(User user : users) {
            userDao.add(user);
        }

        MockMailSender mockMailSender = new MockMailSender();
        userServiceImpl.setMailSender(mockMailSender);

        userServiceImpl.upgradeLevels();

        checkLevelUpgraded(users.get(0), false);
        checkLevelUpgraded(users.get(1), true);
        checkLevelUpgraded(users.get(2), false);
        checkLevelUpgraded(users.get(3), true);
        checkLevelUpgraded(users.get(4), false);

        List<String> request = mockMailSender.getRequests();
        assertEquals(2, request.size());
        assertEquals(users.get(1).getEmail(), request.get(0));
        assertEquals(users.get(3).getEmail(), request.get(1));
    }

    private void checkLevelUpgraded(User user, boolean upgraded) {
        User userUpdate = userDao.get(user.getId());
        if(upgraded) {
            assertEquals(user.getLevel().nextLevel(), userUpdate.getLevel());
        } else {
            assertEquals(user.getLevel(), userUpdate.getLevel());
        }
    }

    static class TestUserService extends UserServiceImpl {
        private String id;

        public TestUserService(String id) {
            this.id = id;
        }

        protected void upgradeLevel(User user) {
            if(user.getId().equals(this.id)) throw new TestUserServiceException();
            super.upgradeLevel(user);
        }
    }

    static class TestUserServiceException extends RuntimeException {
    }

    static class MockMailSender implements MailSender {

        private List<String> requests = new ArrayList<>();

        public List<String> getRequests() {
            return requests;
        }

        @Override
        public void send(SimpleMailMessage simpleMessage) throws MailException {
            requests.add(simpleMessage.getTo()[0]);
        }

        @Override
        public void send(SimpleMailMessage... simpleMessages) throws MailException {

        }
    }

    @Test
    @DirtiesContext
    void upgradeAllOrNothing() {
        TestUserService testUserService = new TestUserService(users.get(3).getId());
        testUserService.setUserDao(userDao);
        testUserService.setMailSender(mailSender);

        TxProxyFactoryBean txProxyFactoryBean
                = applicationContext.getBean("&userService", TxProxyFactoryBean.class);
        txProxyFactoryBean.setTarget(testUserService);
        UserService txUserService = (UserService) txProxyFactoryBean.getObject();

        userDao.deleteAll();
        for (User user : users) {
            userDao.add(user);
        }

        assertThrows(TestUserServiceException.class, () -> txUserService.upgradeLevels());

        checkLevelUpgraded(users.get(1), false);
    }
}