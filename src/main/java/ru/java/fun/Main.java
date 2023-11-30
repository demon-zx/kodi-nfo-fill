package ru.java.fun;

import picocli.CommandLine;
import ru.java.fun.command.Root;

public class Main {


    public static void main(String[] args) {
        new CommandLine(new Root()).execute(args);
    }


}