package dev.abykov.wordlegame.engine;

import dev.abykov.wordlegame.dict.WordleDictionary;
import dev.abykov.wordlegame.exception.InvalidWordLengthException;
import dev.abykov.wordlegame.exception.WordNotFoundException;
import dev.abykov.wordlegame.log.SilentWordleLogger;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WordleGameTest {

    private static final List<String> WORDS = List.of(
            "мороз",
            "велюр",
            "кручь",
            "аббат"
    );

    @Test
    void shouldAcceptValidGuess() throws Exception {
        WordleGame game = createGameWithAnswer("мороз");

        String result = game.submitGuess("мороз");

        assertEquals("+++++", result);
        assertEquals(1, game.getCurrentStep());
        assertEquals(5, game.getRemainingSteps());
        assertTrue(game.isWon());
        assertTrue(game.isFinished());
    }

    @Test
    void shouldRejectWordWithInvalidLength() {
        WordleGame game = createGameWithAnswer("мороз");

        assertThrows(
                InvalidWordLengthException.class,
                () -> game.submitGuess("кот")
        );

        assertEquals(0, game.getCurrentStep());
        assertEquals(6, game.getRemainingSteps());
        assertFalse(game.isFinished());
    }

    @Test
    void shouldRejectUnknownWord() {
        WordleGame game = createGameWithAnswer("мороз");

        assertThrows(
                WordNotFoundException.class,
                () -> game.submitGuess("банка")
        );

        assertEquals(6, game.getRemainingSteps());
        assertFalse(game.isWon());
        assertFalse(game.isFinished());
    }

    @Test
    void shouldContinueGameAfterIncorrectGuess() throws Exception {
        WordleGame game = createGameWithAnswer("мороз");

        String result = game.submitGuess("велюр");

        assertEquals("----^", result);
        assertEquals(1, game.getCurrentStep());
        assertEquals(5, game.getRemainingSteps());
        assertFalse(game.isWon());
        assertFalse(game.isFinished());
    }

    @Test
    void shouldIncreaseStepCounterForEveryValidGuess() throws Exception {
        WordleGame game = createGameWithAnswer("мороз");

        game.submitGuess("велюр");
        game.submitGuess("аббат");
        game.submitGuess("мороз");

        assertEquals(3, game.getCurrentStep());
        assertEquals(3, game.getRemainingSteps());
        assertTrue(game.isWon());
    }

    @Test
    void shouldFinishGameAfterMaxAttempts() throws Exception {
        WordleGame game = createGameWithAnswer("мороз");

        for (int i = 0; i < 6; i++) {
            game.submitGuess("велюр");
        }

        assertFalse(game.isWon());
        assertTrue(game.isFinished());
        assertEquals(6, game.getCurrentStep());
        assertEquals(0, game.getRemainingSteps());
    }

    @Test
    void shouldSuggestWordMatchingPreviousGuesses() throws Exception {
        WordleGame game = createGameWithAnswer("кручь");

        game.submitGuess("аббат");
        game.submitGuess("мороз");

        String hint = game.suggestWord();

        assertMatchesHistory(
                hint,
                List.of(
                        new Guess("аббат", "-----"),
                        new Guess("мороз", "--^--")
                )
        );
    }

    @Test
    void shouldNotSuggestAlreadyEnteredWord() throws Exception {
        WordleGame game = createGameWithAnswer("кручь");

        game.submitGuess("аббат");
        game.submitGuess("мороз");

        String hint = game.suggestWord();

        assertNotEquals("аббат", hint);
        assertNotEquals("мороз", hint);
    }

    private WordleGame createGameWithAnswer(String expectedAnswer) {
        return new WordleGame(
                new WordleDictionary(WORDS),
                new WordHintGenerator(),
                new SilentWordleLogger(),
                expectedAnswer
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
