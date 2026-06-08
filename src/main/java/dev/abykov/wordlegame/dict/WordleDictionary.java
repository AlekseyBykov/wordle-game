package dev.abykov.wordlegame.dict;

import dev.abykov.wordlegame.util.WordUtils;

import java.util.*;

public class WordleDictionary {

    private final List<String> words;
    private final Set<String> wordSet;

    private final Random random;

    public WordleDictionary(List<String> words) {
        this.words = new ArrayList<>(words);
        this.wordSet = new HashSet<>(words);

        this.random = new Random();
    }

    public boolean contains(String word) {
        return wordSet.contains(WordUtils.normalize(word));
    }

    public String getRandomWord() {
        return words.get(random.nextInt(words.size()));
    }

    public List<String> getWords() {
        return new ArrayList<>(words);
    }
}
