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
                new User("user1", "이석준1", "111111", Level.BASIC, 49, 0),
                new User("user2", "이석준2", "222222", Level.BASIC, 50, 0),
                new User("user3", "이석준3", "333333", Level.SILVER, 60, 29),
                new User("user4", "이석준4", "444444", Level.SILVER, 60, 30),
                new User("user5", "이석준5", "555555", Level.GOLD, 60, 100)
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

        checkLevel(users.get(0), Level.BASIC);
        checkLevel(users.get(1), Level.SILVER);
        checkLevel(users.get(2), Level.SILVER);
        checkLevel(users.get(3), Level.GOLD);
        checkLevel(users.get(4), Level.GOLD);
    }

    private void checkLevel(User user, Level expectedLevel) {
        User userUpdate = userDao.get(user.getId());
        assertEquals(expectedLevel, userUpdate.getLevel());
    }

}