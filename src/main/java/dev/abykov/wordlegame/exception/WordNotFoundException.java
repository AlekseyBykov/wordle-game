package dev.abykov.wordlegame.exception;

public class WordNotFoundException extends WordleGameException {

    public WordNotFoundException(String word) {
        super("Слова нет в словаре: " + word);
    }
}
