package ru.java.fun.command;

import picocli.CommandLine;

@CommandLine.Command(
        name = "kodi-fill",
        mixinStandardHelpOptions = true,
        showDefaultValues = true,
        description = "Scrap media info and generate nfo files for kodi",
        subcommands = {
                MovieCommand.class
        }
)
public class Root {

}
