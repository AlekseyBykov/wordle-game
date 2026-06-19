package dev.abykov.wordlegame.engine;

import dev.abykov.wordlegame.dict.WordleDictionary;
import dev.abykov.wordlegame.exception.WordleGameException;

import java.util.List;

public class WordHintGenerator {

    public String suggest(
            WordleDictionary dictionary,
            List<Guess> guesses
    ) throws WordleGameException {
        for (String candidate : dictionary.getWords()) {
            if (matchesAllGuesses(candidate, guesses)) {
                return candidate;
            }
        }

        throw new WordleGameException("Нет доступных слов для подсказки");
    }

    private boolean matchesAllGuesses(String candidate, List<Guess> guesses) {
        for (Guess guess : guesses) {

            if (candidate.equals(guess.word())) {
                return false;
            }

            String actualHint = WordComparator.compare(candidate, guess.word());

            if (!actualHint.equals(guess.hint())) {
                return false;
            }
        }

        return true;
    }
}
