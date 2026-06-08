package dev.abykov.wordlegame.engine;

public final class WordComparator {

    public static final char CORRECT = '+';
    public static final char PRESENT = '^';
    public static final char ABSENT = '-';

    private WordComparator() {
    }

    public static String compare(String answer, String guess) {
        char[] result = new char[answer.length()];

        boolean[] usedAnswer = new boolean[answer.length()];
        boolean[] usedGuess = new boolean[guess.length()];

        markExactMatches(
                answer,
                guess,
                result,
                usedAnswer,
                usedGuess
        );

        markPresentLetters(
                answer,
                guess,
                result,
                usedAnswer,
                usedGuess
        );

        return new String(result);
    }

    private static void markExactMatches(
            String answer,
            String guess,
            char[] result,
            boolean[] usedAnswer,
            boolean[] usedGuess
    ) {
        for (int i = 0; i < answer.length(); i++) {
            if (answer.charAt(i) == guess.charAt(i)) {
                result[i] = CORRECT;

                usedAnswer[i] = true;
                usedGuess[i] = true;
            }
        }
    }

    private static void markPresentLetters(
            String answer,
            String guess,
            char[] result,
            boolean[] usedAnswer,
            boolean[] usedGuess
    ) {
        for (int i = 0; i < guess.length(); i++) {

            if (usedGuess[i]) {
                continue;
            }

            result[i] = findMatchingLetter(
                    answer,
                    guess.charAt(i),
                    usedAnswer
            )
                    ? PRESENT
                    : ABSENT;
        }
    }

    private static boolean findMatchingLetter(
            String answer,
            char letter,
            boolean[] usedAnswer
    ) {
        for (int i = 0; i < answer.length(); i++) {

            if (usedAnswer[i]) {
                continue;
            }

            if (answer.charAt(i) == letter) {
                usedAnswer[i] = true;
                return true;
            }
        }

        return false;
    }
}
