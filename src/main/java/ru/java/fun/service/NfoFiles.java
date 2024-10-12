package ru.java.fun.service;

import org.xml.sax.InputSource;
import ru.java.fun.nfo.BaseNfo;
import ru.java.fun.nfo.EpisodeNfo;
import ru.java.fun.nfo.MovieNfo;
import ru.java.fun.nfo.TVShowNfo;
import ru.java.fun.util.FileUtil;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardOpenOption.CREATE_NEW;
import static java.nio.file.StandardOpenOption.WRITE;

public class NfoFiles {

    private NfoFiles() {
    }

    public static void save(Path directory, TVShowNfo nfo) {
        Path file = directory.resolve("tvshow.nfo");
        save(file, nfo, TVShowNfo.class);
    }

    public static void save(Path episode, EpisodeNfo nfo) {
        Path file = FileUtil.replaceExtension(episode, ".nfo");
        save(file, nfo, EpisodeNfo.class);
    }

    public static void save(Path original, MovieNfo nfo) {
        Path file = FileUtil.replaceExtension(original, ".nfo");
        save(file, nfo, MovieNfo.class);
    }

    public static <T extends BaseNfo> void save(Path file, T nfo, Class<T> nfoClass) {
        try {
            JAXBContext ctx = JAXBContext.newInstance(nfoClass);
            Marshaller marshaller = ctx.createMarshaller();
            save(file, nfo, marshaller);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T extends BaseNfo> void save(Path file, T nfo, Marshaller marshaller) {
        try {
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            Files.deleteIfExists(file);
            try (Writer w = Files.newBufferedWriter(file, UTF_8, WRITE, CREATE_NEW)) {
                marshaller.marshal(nfo, w);
                w.flush();
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    public static MovieNfo load(Path nfo) {
        return load(nfo, MovieNfo.class);
    }

    private static <T> T load(Path nfo, Class<T> objectClass) {
        try (InputStream stream = Files.newInputStream(nfo)) {
            return unmarshall(new InputSource(stream), objectClass);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private static <T> T unmarshall(InputSource source, Class<T> objectClass) {
        try {
            JAXBContext ctx = JAXBContext.newInstance(objectClass);
            var unmarshaller = ctx.createUnmarshaller();
            //noinspection unchecked
            return (T) unmarshaller.unmarshal(source);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

}
