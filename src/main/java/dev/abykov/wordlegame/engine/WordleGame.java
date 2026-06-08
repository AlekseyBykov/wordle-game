package dev.abykov.wordlegame.engine;

import dev.abykov.wordlegame.dict.WordleDictionary;
import dev.abykov.wordlegame.exception.InvalidWordLengthException;
import dev.abykov.wordlegame.exception.WordNotFoundException;
import dev.abykov.wordlegame.exception.WordleGameException;
import dev.abykov.wordlegame.log.WordleLogger;
import dev.abykov.wordlegame.util.WordUtils;

import java.util.ArrayList;
import java.util.List;

public class WordleGame {

    private static final int MAX_STEPS = 6;

    private final WordleLogger log;
    private final WordleDictionary dictionary;
    private final WordHintGenerator hintGenerator;

    private final String answer;
    private final List<Guess> guesses;

    private int currentStep;

    public WordleGame(
            WordleDictionary dictionary,
            WordHintGenerator hintGenerator,
            WordleLogger log
    ) {
        this.log = log;
        this.dictionary = dictionary;

        this.answer = dictionary.getRandomWord();
        this.hintGenerator = hintGenerator;
        this.guesses = new ArrayList<>();

        this.currentStep = 0;
    }

    public String submitGuess(String word) {
        log.info("Player entered '%s'", word);

        try {
            String normalizedWord = normalizeAndValidate(word);
            String result = WordComparator.compare(answer, normalizedWord);

            registerMove(normalizedWord, result);

            guesses.add(new Guess(normalizedWord, result));

            log.info(
                    "Comparison: '%s' -> '%s'",
                    normalizedWord,
                    result
            );

            return result;
        } catch (WordleGameException e) {
            log.error(e, "Guess rejected");
            throw e;
        }
    }

    private String normalizeAndValidate(String word) {
        String normalizedWord = WordUtils.normalize(word);

        if (!WordUtils.hasValidLength(normalizedWord)) {
            throw new InvalidWordLengthException(word);
        }

        if (!dictionary.contains(normalizedWord)) {
            throw new WordNotFoundException(word);
        }

        return normalizedWord;
    }

    public String suggestWord() {
        String hint = hintGenerator.suggest(dictionary, guesses);

        log.info("Hint suggested: '%s'", hint);

        return hint;
    }

    private void registerMove(String word, String result) {
        guesses.add(new Guess(word, result));
        currentStep++;
    }

    private boolean isCorrectGuess() {
        return answer.equals(guesses.getLast().word());
    }

    public boolean isFinished() {
        return isWon() || currentStep >= MAX_STEPS;
    }

    public boolean isWon() {
        return !guesses.isEmpty() && isCorrectGuess();
    }

    public int getCurrentStep() {
        return currentStep;
    }

    public int getRemainingSteps() {
        return MAX_STEPS - currentStep;
    }

    public String getAnswer() {
        return answer;
    }
}
