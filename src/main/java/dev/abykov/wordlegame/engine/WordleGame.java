package dev.abykov.wordlegame.engine;

import dev.abykov.wordlegame.dict.WordleDictionary;
import dev.abykov.wordlegame.exception.InvalidWordLengthException;
import dev.abykov.wordlegame.exception.WordNotFoundException;
import dev.abykov.wordlegame.exception.WordleGameException;
import dev.abykov.wordlegame.log.GameLogger;
import dev.abykov.wordlegame.util.WordUtils;

import java.util.ArrayList;
import java.util.List;

public class WordleGame {

    private static final int MAX_STEPS = 6;

    private final GameLogger log;
    private final WordleDictionary dictionary;
    private final WordHintGenerator hintGenerator;

    private final String answer;
    private final List<Guess> guesses;

    private int currentStep;

    public WordleGame(
            WordleDictionary dictionary,
            WordHintGenerator hintGenerator,
            GameLogger log,
            String answer
    ) {
        this.log = log;
        this.dictionary = dictionary;
        this.hintGenerator = hintGenerator;

        this.answer = answer;
        this.guesses = new ArrayList<>();
        this.currentStep = 0;
    }

    public WordleGame(
            WordleDictionary dictionary,
            WordHintGenerator hintGenerator,
            GameLogger log
    ) {
        this(
                dictionary,
                hintGenerator,
                log,
                dictionary.getRandomWord()
        );
    }

    public String submitGuess(String word) throws WordleGameException {
        log.info("Player entered '%s'", word);

        String normalizedWord = normalizeAndValidate(word);
        String result = WordComparator.compare(answer, normalizedWord);

        registerMove(normalizedWord, result);

        log.info(
                "Comparison: '%s' -> '%s'",
                normalizedWord,
                result
        );

        return result;
    }

    private String normalizeAndValidate(String word) throws WordleGameException {
        String normalizedWord = WordUtils.normalize(word);

        if (!WordUtils.hasValidLength(normalizedWord)) {
            throw new InvalidWordLengthException(word);
        }

        if (!dictionary.contains(normalizedWord)) {
            throw new WordNotFoundException(word);
        }

        return normalizedWord;
    }

    public String suggestWord() throws WordleGameException {
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
