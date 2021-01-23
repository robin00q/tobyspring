package springbook.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.util.HashSet;
import java.util.Set;

@SpringBootTest
public class JUnitTest {

    @Autowired
    ApplicationContext context;

    static Set<JUnitTest> testObjects = new HashSet<>();
    static ApplicationContext contextObject = null;

    @Test
    void test1() {
        Assertions.assertFalse(testObjects.contains(this));
        testObjects.add(this);
        Assertions.assertTrue((contextObject == null) || (context == contextObject));
        contextObject = context;
    }

    @Test
    void test2() {
        Assertions.assertFalse(testObjects.contains(this));
        testObjects.add(this);
        Assertions.assertTrue((contextObject == null) || (context == contextObject));
        contextObject = context;
    }

    @Test
    void test3() {
        Assertions.assertFalse(testObjects.contains(this));
        testObjects.add(this);
        Assertions.assertTrue((contextObject == null) || (context == contextObject));
        contextObject = context;
    }
}
