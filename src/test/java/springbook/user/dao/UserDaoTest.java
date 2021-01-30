package springbook.user.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import springbook.user.domain.Level;
import springbook.user.domain.User;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class UserDaoTest {

    @Autowired
    ApplicationContext context;
    @Autowired
    UserDao dao;
    @Autowired
    DataSource dataSource;

    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    void setUp() {
        user1 = new User("user1", "이석준1", "111111", Level.BASIC, 1, 0);
        user2 = new User("user2", "이석준2", "222222", Level.SILVER, 55, 10);
        user3 = new User("user3", "이석준3", "333333", Level.GOLD, 100, 40);
    }

    @Test
    void update() {
        dao.deleteAll();

        dao.add(user1);
        dao.add(user2);

        user1.setName("이석준");
        user1.setPassword("password");
        user1.setLevel(Level.GOLD);
        user1.setLogin(1000);
        user1.setRecommend(999);
        dao.update(user1);

        User user1update = dao.get(user1.getId());
        checkSameUser(user1, user1update);
        User user2update = dao.get(user2.getId());
        checkSameUser(user2, user2update);
    }

    @Test
    void duplicateKey() {
        dao.deleteAll();

        dao.add(user1);
        assertThrows(DuplicateKeyException.class, () -> dao.add(user1));
    }

    @Test
    void addAndGet() throws SQLException {
        dao.deleteAll();
        assertEquals(0, dao.getCount());

        dao.add(user1);
        assertEquals(1, dao.getCount());

        User user2 = dao.get(user1.getId());

        assertEquals(user1.getId(), user2.getId());
        assertEquals(user1.getName(), user2.getName());
        assertEquals(user1.getPassword(), user2.getPassword());
    }

    @Test
    void getCountTest() throws SQLException {
        dao.deleteAll();
        assertEquals(0, dao.getCount());

        dao.add(user1);
        assertEquals(1, dao.getCount());

        dao.add(user2);
        assertEquals(2, dao.getCount());

        dao.add(user3);
        assertEquals(3, dao.getCount());
    }

    @Test
    void getUserFailure() throws SQLException {
        dao.deleteAll();

        assertEquals(0, dao.getCount());

        assertThrows(EmptyResultDataAccessException.class, () -> dao.get("unknown_id"));
    }

    @Test
    void getAll() {
        dao.deleteAll();

        List<User> users0 = dao.getAll();
        assertEquals(0, users0.size());


        dao.add(user1);
        List<User> users1 = dao.getAll();
        assertEquals(1, users1.size());
        checkSameUser(user1, users1.get(0));

        dao.add(user2);
        List<User> users2 = dao.getAll();
        assertEquals(2, users2.size());
        checkSameUser(user1, users2.get(0));
        checkSameUser(user2, users2.get(1));

        dao.add(user3);
        List<User> users3 = dao.getAll();
        assertEquals(3, users3.size());
        checkSameUser(user1, users2.get(0));
        checkSameUser(user2, users2.get(1));
        checkSameUser(user3, users3.get(2));
    }

    private void checkSameUser(User user1, User user2) {
        assertEquals(user1.getId(), user2.getId());
        assertEquals(user1.getName(), user2.getName());
        assertEquals(user1.getPassword(), user2.getPassword());
        assertEquals(user1.getLevel(), user2.getLevel());
        assertEquals(user1.getLogin(), user2.getLogin());
        assertEquals(user1.getRecommend(), user2.getRecommend());
    }



}