package dev.abykov.wordlegame;

import dev.abykov.wordlegame.dict.WordleDictionary;
import dev.abykov.wordlegame.dict.WordleDictionaryLoader;
import dev.abykov.wordlegame.engine.WordHintGenerator;
import dev.abykov.wordlegame.engine.WordleGame;
import dev.abykov.wordlegame.exception.DictionaryLoadException;
import dev.abykov.wordlegame.exception.WordleGameException;
import dev.abykov.wordlegame.log.WordleLogger;

import java.io.IOException;
import java.util.Scanner;

public class WordleApplication {

    public static void main(String[] args) {
        try (
                WordleLogger log = new WordleLogger();
                Scanner scanner = new Scanner(System.in)
        ) {
            WordleDictionaryLoader dictionaryLoader = new WordleDictionaryLoader(log);
            WordleDictionary dictionary = dictionaryLoader.load();
            WordHintGenerator hintGenerator = new WordHintGenerator();
            WordleGame game = new WordleGame(dictionary, hintGenerator, log);

            while (!game.isFinished()) {
                System.out.println("Введите слово:");

                String input = scanner.nextLine();

                if (input.isBlank()) {
                    String hint = game.suggestWord();
                    System.out.println("Подсказка: " + hint);
                    continue;
                }

                try {
                    String result = game.submitGuess(input);

                    System.out.println(result);
                    System.out.println(
                            "Осталось попыток: "
                                    + game.getRemainingSteps()
                    );
                } catch (WordleGameException e) {
                    log.error(e, "Guess rejected");
                    System.out.println(e.getMessage());
                }
            }

            log.info(
                    "Game finished. Result: %s, answer: '%s', attempts: %d",
                    game.isWon() ? "WIN" : "LOSE",
                    game.getAnswer(),
                    game.getCurrentStep()
            );

            System.out.println(
                    game.isWon()
                            ? "Вы победили!"
                            : "Вы проиграли."
            );

            System.out.println(
                    "Загаданное слово: "
                            + game.getAnswer()
            );

        } catch (DictionaryLoadException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("Не удалось создать лог.");
        }
    }
}
