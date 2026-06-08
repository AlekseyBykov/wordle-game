package dev.abykov.wordlegame.exception;

public class InvalidWordLengthException extends WordleGameException {

    public InvalidWordLengthException(String word) {
        super("Слово должно состоять из пяти букв: " + word);
    }
}
