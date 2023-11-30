package ru.java.fun.command;

import picocli.CommandLine;

import java.util.concurrent.Callable;

public abstract class AbstractCommand implements Callable<Integer> {

    @CommandLine.Spec
    protected CommandLine.Model.CommandSpec spec;

    @CommandLine.Option(
            names = {"-u", "--uri"},
            description = "Url for api.kinopoisk.dev"
    )
    protected String uri = "https://api.kinopoisk.dev/v1.4";

    @CommandLine.Option(
            names = {"-t", "--token"},
            description = "Token for api.kinopoisk.dev",
            required = true
    )
    protected String token;

    protected void println(String line) {
        spec.commandLine()
                .getOut()
                .println(line);
    }

    protected void printf(String format, Object... args) {
        spec.commandLine()
                .getOut()
                .printf(format, args);
    }

}
