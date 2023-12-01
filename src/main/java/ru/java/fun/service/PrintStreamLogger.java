package ru.java.fun.service;

import java.io.PrintWriter;

public class PrintStreamLogger implements Logger {

    private final PrintWriter out;

    public PrintStreamLogger(PrintWriter out) {
        this.out = out;
    }

    @Override
    public void println(Object line) {
        out.println(line);
    }

    @Override
    public void println(String line) {
        out.println(line);
    }

    @Override
    public void printf(String format, Object... args) {
        out.printf(format, args);
    }
}
