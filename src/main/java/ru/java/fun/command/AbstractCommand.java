package ru.java.fun.command;

import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command
public abstract class AbstractCommand implements Callable<Integer> {

    @CommandLine.ParentCommand
    Root root;

    @CommandLine.Spec
    protected CommandLine.Model.CommandSpec spec;

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

    public CommandLine.Model.CommandSpec getSpec() {
        return spec;
    }

    public String getUri() {
        return root.getUri();
    }

    public String getToken() {
        return root.getToken();
    }
}
