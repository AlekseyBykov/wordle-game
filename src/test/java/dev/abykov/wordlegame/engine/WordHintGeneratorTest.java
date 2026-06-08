package dev.abykov.wordlegame.engine;

import dev.abykov.wordlegame.dict.WordleDictionary;
import dev.abykov.wordlegame.exception.WordleGameException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WordHintGeneratorTest {

    private final WordHintGenerator generator = new WordHintGenerator();

    @Test
    void shouldReturnFirstDictionaryWordWhenHistoryIsEmpty() {
        WordleDictionary dictionary = new WordleDictionary(
                List.of(
                        "банка",
                        "маска",
                        "велюр"
                )
        );

        String hint = generator.suggest(dictionary, List.of());

        assertEquals("банка", hint);
    }

    @Test
    void shouldReturnWordMatchingSinglePreviousHint() {
        WordleDictionary dictionary = new WordleDictionary(
                List.of(
                        "банка",
                        "велюр",
                        "аббат"
                )
        );

        List<Guess> guesses = List.of(
                new Guess("аббат", "-----")
        );

        String hint = generator.suggest(dictionary, guesses);

        assertMatchesHistory(hint, guesses);
    }

    @Test
    void shouldReturnWordMatchingAllPreviousHints() {
        WordleDictionary dictionary = new WordleDictionary(
                List.of(
                        "кручь",
                        "велюр",
                        "мороз",
                        "аббат",
                        "банка"
                )
        );

        List<Guess> guesses = List.of(
                new Guess("аббат", "-----"),
                new Guess("мороз", "--^--")
        );

        String hint = generator.suggest(dictionary, guesses);

        assertMatchesHistory(hint, guesses);
    }

    @Test
    void shouldThrowExceptionWhenNoHintAvailable() {
        WordleDictionary dictionary = new WordleDictionary(
                List.of("банка")
        );

        List<Guess> guesses = List.of(
                new Guess("банка", "-----")
        );

        assertThrows(
                WordleGameException.class,
                () -> generator.suggest(dictionary, guesses)
        );
    }

    private void assertMatchesHistory(
            String candidate,
            List<Guess> guesses
    ) {
        for (Guess guess : guesses) {
            assertEquals(
                    guess.hint(),
                    WordComparator.compare(candidate, guess.word())
            );
        }
    }
}
