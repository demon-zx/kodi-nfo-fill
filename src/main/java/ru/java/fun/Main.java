package ru.java.fun;

import picocli.CommandLine;
import ru.java.fun.command.MovieCommand;

public class Main {


    public static void main(String[] args) {
        new CommandLine(new MovieCommand()).execute(args);
    }


}