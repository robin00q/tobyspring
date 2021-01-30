package springbook.user.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import springbook.user.domain.Level;
import springbook.user.domain.User;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static springbook.user.dao.UserService.MIN_LOGOUT_FOR_SILVER;
import static springbook.user.dao.UserService.MIN_RECOMMEND_FOR_GOLD;

@SpringBootTest
class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    UserDao userDao;

    List<User> users;

    @BeforeEach
    void setUp() {
        users = Arrays.asList(
                new User("user1", "이석준1", "111111", Level.BASIC, MIN_LOGOUT_FOR_SILVER - 1, 0),
                new User("user2", "이석준2", "222222", Level.BASIC, MIN_LOGOUT_FOR_SILVER, 0),
                new User("user3", "이석준3", "333333", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD - 1),
                new User("user4", "이석준4", "444444", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD),
                new User("user5", "이석준5", "555555", Level.GOLD, 60, Integer.MAX_VALUE)
        );
    }

    @Test
    void add() {
        userDao.deleteAll();

        User userWithLevel = users.get(4);
        User userWithoutLevel = users.get(0);
        userWithoutLevel.setLevel(null);

        userService.add(userWithLevel);
        userService.add(userWithoutLevel);

        User userWithLevelRead = userDao.get(userWithLevel.getId());
        User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());

        assertEquals(userWithLevel.getLevel(), userWithLevelRead.getLevel());
        assertEquals(Level.BASIC, userWithoutLevelRead.getLevel());
    }

    @Test
    void upgradeLevels() {
        userDao.deleteAll();
        for(User user : users) {
            userDao.add(user);
        }

        userService.upgradeLevels();

        checkLevelUpgraded(users.get(0), false);
        checkLevelUpgraded(users.get(1), true);
        checkLevelUpgraded(users.get(2), false);
        checkLevelUpgraded(users.get(3), true);
        checkLevelUpgraded(users.get(4), false);
    }

    private void checkLevelUpgraded(User user, boolean upgraded) {
        User userUpdate = userDao.get(user.getId());
        if(upgraded) {
            assertEquals(user.getLevel().nextLevel(), userUpdate.getLevel());
        } else {
            assertEquals(user.getLevel(), userUpdate.getLevel());
        }
    }

}