package springbook.learningtest.pointcut;

import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PointcutExpressionTest {

    @Test
    void methodSignaturePointcut() throws NoSuchMethodException {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(public int springbook.learningtest.pointcut.Target.minus(int,int) throws java.lang.RuntimeException)");

        // Target.minus()
        assertTrue(pointcut.getClassFilter().matches(Target.class) &&
                pointcut.getMethodMatcher().matches(Target.class.getMethod("minus", int.class, int.class), null));

        // Target.plus()
        assertFalse(pointcut.getClassFilter().matches(Target.class) &&
                pointcut.getMethodMatcher().matches(Target.class.getMethod("plus", int.class, int.class), null));

        // Bean.method()
        assertFalse(pointcut.getClassFilter().matches(Bean.class));
    }

    @Test
    void pointcut() throws Exception {
        targetClassPointcutMathches("execution(* *(..))", true, true, true, true, true, true);
    }

    void targetClassPointcutMathches(String expression, boolean... expected) throws Exception {
        pointcutMathches(expression, expected[0], Target.class, "hello");
        pointcutMathches(expression, expected[1], Target.class, "hello", String.class);
        pointcutMathches(expression, expected[2], Target.class, "plus", int.class, int.class);
        pointcutMathches(expression, expected[3], Target.class, "minus", int.class, int.class);
        pointcutMathches(expression, expected[4], Target.class, "method");
        pointcutMathches(expression, expected[5], Bean.class, "method");
    }

    public void pointcutMathches(String expression, Boolean expected, Class<?> clazz, String methodName, Class<?>... args) throws Exception {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(expression);

        if(expected) {
            assertTrue(pointcut.getClassFilter().matches(clazz) &&
                    pointcut.getMethodMatcher().matches(clazz.getMethod(methodName, args), null));
        } else {
            assertFalse(pointcut.getClassFilter().matches(clazz) &&
                    pointcut.getMethodMatcher().matches(clazz.getMethod(methodName, args), null));
        }
    }
}