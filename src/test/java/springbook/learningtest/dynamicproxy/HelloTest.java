package springbook.learningtest.dynamicproxy;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HelloTest {

    @Test
    void simpleProxy() {
        Hello hello = new HelloTarget();

        assertEquals("Hello Toby", hello.sayHello("Toby"));
        assertEquals("Hi Toby", hello.sayHi("Toby"));
        assertEquals("Thank You Toby", hello.sayThankYou("Toby"));
    }

    @Test
    void simpleProxy2() {
        Hello hello = new HelloUpperCase(new HelloTarget());

        assertEquals("HELLO TOBY", hello.sayHello("Toby"));
        assertEquals("HI TOBY", hello.sayHi("Toby"));
        assertEquals("THANK YOU TOBY", hello.sayThankYou("Toby"));
    }

    @Test
    void dynamicProxy() {
        Hello proxiedHello = (Hello) Proxy.newProxyInstance(
                getClass().getClassLoader(),
                new Class[] {Hello.class},
                new UppercaseHandler(new HelloTarget()));

        assertEquals("HELLO TOBY", proxiedHello.sayHello("Toby"));
        assertEquals("HI TOBY", proxiedHello.sayHi("Toby"));
        assertEquals("THANK YOU TOBY", proxiedHello.sayThankYou("Toby"));
    }
}