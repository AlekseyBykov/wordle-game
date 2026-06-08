package dev.abykov.wordlegame.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WordUtilsTest {

    @Test
    void shouldNormalizeWord() {
        String result = WordUtils.normalize("  ЁЖИК  ");

        assertEquals("ежик", result);
    }

    @Test
    void shouldReplaceYoWithYe() {
        String result = WordUtils.normalize("ёлка");

        assertEquals("елка", result);
    }

    @Test
    void shouldReturnTrueForValidLength() {
        assertTrue(WordUtils.hasValidLength("банка"));
    }

    @Test
    void shouldReturnFalseForShortWord() {
        assertFalse(WordUtils.hasValidLength("кот"));
    }

    @Test
    void shouldReturnFalseForLongWord() {
        assertFalse(WordUtils.hasValidLength("программист"));
    }
}
