package ru.java.fun;

import org.apache.commons.cli.*;
import ru.java.fun.command.Command;
import ru.java.fun.command.FilmCommand;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

@SuppressWarnings("CallToPrintStackTrace")
public class Main {

    private static final Option OPTION_KP_TOKEN = Option.builder()
            .option("t")
            .longOpt("token")
            .hasArg()
            .desc("Token for api.kinopoisk.dev")
            .required()
            .build();
    private static final Option OPTION_KP_URI = Option.builder()
            .option("u")
            .longOpt("uri")
            .hasArg()
            .desc("Url for api.kinopoisk.dev")
            .build();

    private static final Option OPTION_NAME = Option.builder()
            .option("n")
            .longOpt("name")
            .hasArg()
            .desc("Film name")
            .build();
    private static final Option OPTION_FILE_NAME = Option.builder()
            .option("f")
            .longOpt("file-name")
            .hasArg()
            .desc("File name")
            .required()
            .build();

    private static final Map<Option, BiConsumer<Input, String>> optionsHandlers = Map.of(
            OPTION_FILE_NAME, (input, fullFileName) -> {
                int index = fullFileName.lastIndexOf('.');
                if (index > 0) {
                    String fileName = fullFileName.substring(0, index);
                    input.setFileName(fileName);
                    input.setFileExtension(fullFileName.substring(fileName.length() + 1));
                } else {
                    input.setFileName(fullFileName);
                    input.setFileExtension("");
                }
            },
            OPTION_NAME, Input::setFilmName,
            OPTION_KP_URI, Input::setUri,
            OPTION_KP_TOKEN, Input::setToken
    );
    private static final Function<String, Command> unknownCommand = command -> {
        throw new IllegalArgumentException("Unknown command:" + command);
    };
    private static final Map<String, Function<String, Command> > commands = Map.of(
            "film", command -> new FilmCommand()
    );

    public static void main(String[] args) {
        CommandLineParser parser = new DefaultParser();
        Options options = new Options();
        try {
            optionsHandlers.keySet()
                    .forEach(options::addOption);
            CommandLine cmd = parser.parse(options, args);
            Input input = input(cmd);
            List<String> arguments = cmd.getArgList();
            String name = arguments.get(0);
            Command command = commands.getOrDefault(name, unknownCommand)
                    .apply(name);
            command.execute(input);
        } catch (ParseException|IllegalArgumentException e) {
            System.out.println(e.getMessage());
            HelpFormatter help = new HelpFormatter();
            help.printHelp("Usage: ", options);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private static Input input(CommandLine cmd) {
        Input input = new Input();
        input.setUri("https://api.kinopoisk.dev/v1.4");
        for (Option option : cmd.getOptions()) {
            BiConsumer<Input, String> function = optionsHandlers.get(option);
            if (function == null) {
                throw new IllegalStateException("Not found handler for option: " + option.getLongOpt());
            }
            function.accept(input, option.getValue());
        }
        return input;
    }
}