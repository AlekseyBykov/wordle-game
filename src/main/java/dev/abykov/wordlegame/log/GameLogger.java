package dev.abykov.wordlegame.log;

public interface GameLogger {

    void info(String format, Object... args);

    void error(String format, Object... args);

    void error(Throwable throwable, String format, Object... args);

    void error(Throwable throwable);
}
