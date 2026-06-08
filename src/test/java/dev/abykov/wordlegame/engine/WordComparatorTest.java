package dev.abykov.wordlegame.engine;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WordComparatorTest {

    @Test
    void shouldMarkAllLettersAsCorrectWhenWordsAreEqual() {
        String result = WordComparator.compare("банка", "банка");

        assertEquals("+++++", result);
    }

    @Test
    void shouldMarkAbsentLetters() {
        String result = WordComparator.compare("банка", "мороз");

        assertEquals("-----", result);
    }

    @Test
    void shouldMarkPresentLetters() {
        String result = WordComparator.compare("банка", "актер");

        assertEquals("^^---", result);
    }

    @Test
    void shouldMarkMixedResult() {
        String result = WordComparator.compare("банка", "балка");

        assertEquals("++-++", result);
    }

    @Test
    void shouldHandleRepeatedLettersInGuess() {
        String result = WordComparator.compare("банка", "ааааа");

        assertEquals("-+--+", result);
    }

    @Test
    void shouldNotReuseAnswerLetters() {
        String result = WordComparator.compare("маска", "ссссс");

        assertEquals("--+--", result);
    }
}
