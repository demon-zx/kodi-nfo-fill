package ru.java.fun.service;

public interface Logger {

    void println(Level level, Object line);

    void println(Level level, String line);

    void printf(Level level, String format, Object... args);

    enum Level {
        INFO(0), DEBUG(1), TRACE(2);

        private final int value;

        Level(int value) {
            this.value = value;
        }

        public int value() {
            return value;
        }
    }
}
