package dev.abykov.wordlegame.log;

public class SilentWordleLogger implements GameLogger {

    @Override
    public void info(String format, Object... args) {
    }

    @Override
    public void error(String format, Object... args) {
    }

    @Override
    public void error(Throwable throwable, String format, Object... args) {
    }

    @Override
    public void error(Throwable throwable) {
    }
}
