package springbook.learningtest;

public interface LineCallback<T> {
    T doSomethingWithLine(String line, T value);
}
