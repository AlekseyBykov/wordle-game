package dev.abykov.wordlegame.util;

public final class WordUtils {

    public static final int WORD_LENGTH = 5;

    private WordUtils() {
    }

    public static String normalize(String word) {
        return word.trim()
                .toLowerCase()
                .replace('ё', 'е');
    }

    public static boolean hasValidLength(String word) {
        return word.length() == WORD_LENGTH;
    }
}
