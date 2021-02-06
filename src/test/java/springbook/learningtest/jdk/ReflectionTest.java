package springbook.learningtest.jdk;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReflectionTest {

    @Test
    void invokeMethod() throws Exception {
        String name = "Spring";

        // length
        assertEquals(6, name.length());

        Method lengthMethod = String.class.getMethod("length");
        assertEquals(6, lengthMethod.invoke(name));

        // charAt
        assertEquals('S', name.charAt(0));

        Method charAtMethod = String.class.getMethod("charAt", int.class);
        assertEquals('S', charAtMethod.invoke(name, 0));
    }
}
