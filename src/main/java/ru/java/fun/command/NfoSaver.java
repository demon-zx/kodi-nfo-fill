package ru.java.fun.command;

import ru.java.fun.Input;
import ru.java.fun.nfo.MovieNfo;
import ru.java.fun.util.FileUtil;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardOpenOption.CREATE_NEW;
import static java.nio.file.StandardOpenOption.WRITE;

public class NfoSaver {

    private NfoSaver() {
    }

    public static void save(Path original, MovieNfo movie) {
        try {
            JAXBContext ctx = JAXBContext.newInstance(MovieNfo.class);
            Marshaller marshaller = ctx.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            Path file = FileUtil.replaceExtension(original,".nfo");
            Files.deleteIfExists(file);
            try (Writer w = Files.newBufferedWriter(file, UTF_8, WRITE, CREATE_NEW)) {
                marshaller.marshal(movie, w);
                w.flush();
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

}
