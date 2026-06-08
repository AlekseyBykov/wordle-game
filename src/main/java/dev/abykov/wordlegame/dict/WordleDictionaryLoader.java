package dev.abykov.wordlegame.dict;

import dev.abykov.wordlegame.exception.DictionaryLoadException;
import dev.abykov.wordlegame.log.WordleLogger;
import dev.abykov.wordlegame.util.WordUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class WordleDictionaryLoader {

    private final WordleLogger log;

    private static final String DEFAULT_DICT_FILE = "words_ru.txt";

    public WordleDictionaryLoader(WordleLogger log) {
        this.log = log;
    }

    public WordleDictionary load() {
        return this.load(DEFAULT_DICT_FILE);
    }

    public WordleDictionary load(String fileName) {
        Set<String> uniqueWords = new LinkedHashSet<>();

        try {
            log.info("Loading dictionary '%s'", fileName);

            List<String> lines = Files.readAllLines(
                    Path.of(fileName),
                    StandardCharsets.UTF_8
            );

            log.info("Read %d entries from '%s'", lines.size(), fileName);

            for (String line : lines) {
                String word = WordUtils.normalize(line);

                if (WordUtils.hasValidLength(word)) {
                    uniqueWords.add(word);
                }
            }

            log.info(
                    "Dictionary loaded: %d unique words (%d filtered out)",
                    uniqueWords.size(),
                    lines.size() - uniqueWords.size()
            );

            return new WordleDictionary(List.copyOf(uniqueWords));
        } catch (IOException e) {
            log.error(
                    e,
                    "Failed to load dictionary '%s'",
                    fileName
            );
            throw new DictionaryLoadException(
                    "Не удалось загрузить словарь: " + fileName,
                    e
            );
        }
    }
}
