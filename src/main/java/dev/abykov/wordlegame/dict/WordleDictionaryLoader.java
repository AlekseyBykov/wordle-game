package dev.abykov.wordlegame.dict;

import dev.abykov.wordlegame.exception.DictionaryLoadException;
import dev.abykov.wordlegame.log.GameLogger;
import dev.abykov.wordlegame.util.WordUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class WordleDictionaryLoader {

    private final GameLogger log;

    private static final String DEFAULT_DICT_FILE = "words_ru.txt";

    public WordleDictionaryLoader(GameLogger log) {
        this.log = log;
    }

    public WordleDictionary load() {
        return this.load(DEFAULT_DICT_FILE);
    }

    public WordleDictionary load(String fileName) {
        Set<String> uniqueWords = new LinkedHashSet<>();
        int totalWords = 0;

        log.info("Loading dictionary '%s'", fileName);
        try (
                BufferedReader reader = Files.newBufferedReader(
                        Path.of(fileName),
                        StandardCharsets.UTF_8
                )
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                totalWords++;
                processWord(uniqueWords, line);
            }

            log.info(
                    "Read %d entries from '%s'",
                    totalWords,
                    fileName
            );

            if (uniqueWords.isEmpty()) {
                throw new DictionaryLoadException("Словарь не содержит подходящих слов");
            }

            log.info(
                    "Dictionary loaded: %d unique words (%d filtered out)",
                    uniqueWords.size(),
                    totalWords - uniqueWords.size()
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

    private void processWord(Set<String> uniqueWords, String line) {
        String word = WordUtils.normalize(line);

        if (WordUtils.hasValidLength(word)) {
            uniqueWords.add(word);
        }
    }
}
