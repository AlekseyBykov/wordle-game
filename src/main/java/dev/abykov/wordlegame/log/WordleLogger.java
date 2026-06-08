package dev.abykov.wordlegame.log;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class WordleLogger implements AutoCloseable {

    private static final String DEFAULT_LOG_FILE = "wordle.log";
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final PrintWriter writer;

    public WordleLogger() throws IOException {
        this(DEFAULT_LOG_FILE);
    }

    public WordleLogger(String fileName) throws IOException {
        writer = new PrintWriter(Files.newBufferedWriter(Path.of(fileName)));
    }

    public void info(String format, Object... args) {
        log("INFO", format.formatted(args));
    }

    public void error(String format, Object... args) {
        log("ERROR", format.formatted(args));
    }

    public void error(Throwable throwable, String format, Object... args) {
        error(format, args);
        throwable.printStackTrace(writer);
        writer.flush();
    }

    private void log(String level, String message) {
        writer.printf(
                "%s [%s] %s%n",
                LocalDateTime.now().format(FORMATTER),
                level,
                message
        );
        writer.flush();
    }

    public void error(Throwable throwable) {
        log("ERROR", throwable.toString());
        throwable.printStackTrace(writer);
        writer.flush();
    }

    @Override
    public void close() {
        writer.close();
    }
}
