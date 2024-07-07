package ru.java.fun.command;

import picocli.CommandLine;

@CommandLine.Command(
        name = "kodi-fill",
        mixinStandardHelpOptions = true,
        showDefaultValues = true,
        description = "Scrap media info and generate nfo files for kodi",
        subcommands = {
                MovieCommand.class,
                MoviesCommand.class,
                SerialCommand.class,
                FakeSerialCommand.class,
                TsvToSerialCommand.class,
                SerialToTsvCommand.class
        }
)
public class Root {

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

    public String getUri() {
        return uri;
    }

    public String getToken() {
        return token;
    }
}
