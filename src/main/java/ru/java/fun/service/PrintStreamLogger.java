package ru.java.fun.service;

import java.io.PrintWriter;

public class PrintStreamLogger implements Logger {

    private final PrintWriter out;
    private Level maximum;

    public PrintStreamLogger(PrintWriter out, Level maximum) {
        this.out = out;
        this.maximum = maximum;
    }

    @Override
    public void println(Level level, Object line) {
        if (isForPrint(level)) {
            out.println(line);
        }
    }

    private boolean isForPrint(Level level) {
        return maximum.value() >= level.value();
    }

    @Override
    public void println(Level level, String line) {
        if (isForPrint(level)) {
            out.println(line);
        }
    }

    @Override
    public void printf(Level level, String format, Object... args) {
        if (isForPrint(level)) {
            out.printf(format, args);
        }
    }
}
