import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WikiToCSV {

    public static void main(String[] args) throws IOException {
        String wiki = Files.readString(Path.of("wiki.txt"));
        try(PrintWriter writer = new PrintWriter(System.out)) {
            toCSV(writer, wiki, "\t");
        }
    }

    private static final Pattern pLine = Pattern.compile("^\\|(?<name>\\w+)\\s*=\\s*(?<value>.+?)?$");

    private static final List<Map.Entry<String, String>> columns = List.of(
            Map.entry("Episode id", "EpisodeNumber2"),
            Map.entry("Episode name", "Title"),
            Map.entry("Original episode name", "AltTitle"),
            Map.entry("Premiere dd MMMMM yyyy", "OriginalAirDate"),
            Map.entry("Description", "ShortSummary")
    );

    public static void toCSV(PrintWriter writer, String wiki, String delimiter) {
        var header = columns.stream()
                .map(Map.Entry::getKey)
                .collect(Collectors.joining(delimiter));
        writer.println(header);
        Map<String, String> row = new HashMap<>();
        List<String> lines = wiki.lines()
                .collect(Collectors.toList());
        String name = null;
        StringBuilder value = new StringBuilder();
        for (String line : lines) {
            if (line.startsWith("{{Episode list")) {
                row.clear();
            } else if (line.startsWith("|")) {
                if (name != null) {
                    row.put(name, value.toString());
                    value = new StringBuilder();
                }
                var m = pLine.matcher(line);
                if (m.matches()) {
                    name = m.group("name");
                    String v = m.group("value");
                    if (v != null) {
                        value.append(v);
                    }
                }
            } else if (line.startsWith("}}")) {
                if (name != null) {
                    row.put(name, value.toString());
                    value = new StringBuilder();
                }
                name = null;
                var csv = columns.stream()
                        .map(c -> row.get(c.getValue()))
                        .map(WikiToCSV::clear)
                        .collect(Collectors.joining(delimiter));
                writer.println(csv);
            } else {
                if (name != null) {
                    value.append(line);
                }

            }
        }
    }

    public static String clear(String line) {
        return line.replaceAll("'''", "")
                .replaceAll("''", "")
                .replaceAll("<ref .+?</ref>", "")
                .replaceAll("<ref .+?/>", "")
                .replaceAll("\\[\\[([^].]+?\\|)?(.+?)]]", "$2")
                .replaceAll("<br>", " ")
                .replaceAll("<br/>", " ");
    }
}
