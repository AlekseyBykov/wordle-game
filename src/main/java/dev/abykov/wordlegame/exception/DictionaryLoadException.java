package dev.abykov.wordlegame.exception;

public class DictionaryLoadException extends RuntimeException {

    public DictionaryLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}
