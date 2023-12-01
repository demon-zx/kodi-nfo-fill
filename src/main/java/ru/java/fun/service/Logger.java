package ru.java.fun.service;

public interface Logger {

    void println(Object line);

    void println(String line);

    void printf(String format, Object... args);
}
