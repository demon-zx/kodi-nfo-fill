package ru.java.fun.command;

import ru.java.fun.Input;

import java.io.IOException;

public interface Command<I> {

    void execute(I input) throws IOException;

}
