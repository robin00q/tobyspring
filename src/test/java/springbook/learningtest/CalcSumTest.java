package springbook.learningtest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalcSumTest {

    private Calculator calculator;
    private String numFilePath;

    @BeforeEach
    void setUp() {
        calculator = new Calculator();
        numFilePath = getClass().getResource("/numbers.txt").getPath();
    }

    @Test
    void sumOfNumbers() throws IOException {
        int sum = calculator.calcSum(numFilePath);
        assertEquals(10, sum);
    }

    @Test
    void multiplyOfNumbers() throws IOException {
        int multiply = calculator.calcMultiply(numFilePath);
        assertEquals(24, multiply);
    }

    @Test
    void concatenateOfNumbers() throws IOException {
        String concatenate = calculator.concatenate(numFilePath);
        assertEquals("1234", concatenate);
    }
}
