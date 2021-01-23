package springbook.user.dao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import springbook.user.domain.User;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserDaoTest {

    @Autowired
    ApplicationContext context;

    private UserDao dao;
    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    void setUp() {
        dao = context.getBean("userDao", UserDao.class);
        user1 = new User("user1", "이석준1", "111111");
        user2 = new User("user2", "이석준2", "222222");
        user3 = new User("user3", "이석준3", "333333");
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

}