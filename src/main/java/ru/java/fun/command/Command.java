package ru.java.fun.command;

import ru.java.fun.Input;

import java.io.IOException;

public interface Command {

    void execute(Input input) throws IOException;

}
