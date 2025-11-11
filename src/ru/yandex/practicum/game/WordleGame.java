package ru.yandex.practicum.game;

/*
в этом классе хранится словарь и состояние игры
    текущий шаг
    всё что пользователь вводил
    правильный ответ

в этом классе нужны методы, которые
    проанализируют совпадение слова с ответом
    предложат слово-подсказку с учётом всего, что вводил пользователь ранее

не забудьте про специальные типы исключений для игровых и неигровых ошибок
 */

import ru.yandex.practicum.dictionary.WordleDictionary;
import ru.yandex.practicum.exceptions.InvalidWordException;
import ru.yandex.practicum.exceptions.NoAttemptsLeftException;
import ru.yandex.practicum.exceptions.WordNotFoundException;

import java.io.PrintWriter;
import java.util.*;

public class WordleGame {
    private final String secretWord;
    private int attemptsLeft;
    private final WordleDictionary dictionary;
    private final PrintWriter log;

    private Set<Character> correctLetters = new HashSet<>();
    private Set<Character> wrongLetters = new HashSet();
    private Map<Integer, Character> correctPositions = new HashMap();
    private Map<Integer, Set<Character>> wrongPositions = new HashMap<>();
    private List<String> previousAttempts = new ArrayList<>();

    public WordleGame(WordleDictionary dictionary, PrintWriter log) {
        this.attemptsLeft = 6;
        this.dictionary = dictionary;
        this.log = log;
        this.secretWord = dictionary.getRandomWord();

        for (int i = 0; i < 5; i++) {
            wrongPositions.put(i, new HashSet<>());
        }

        log.println("Загаданное слово: " + secretWord);
    }

    public GameResult makeAttempt(String attempt) throws NoAttemptsLeftException, WordNotFoundException, InvalidWordException {
        if (attemptsLeft <= 0) {
            throw new NoAttemptsLeftException();
        }

        String normalizedAttempt = normalizeWord(attempt);

        if (normalizedAttempt.length() != 5) {
            throw new InvalidWordException("Слово должно состоять из 5 букв");
        }

        if (!dictionary.contains(normalizedAttempt)) {
            throw new WordNotFoundException(attempt);
        }

        previousAttempts.add(normalizedAttempt);
        attemptsLeft--;

        if (normalizedAttempt.equals(secretWord)) {
            return new GameResult(true, "Поздравляем! Вы угадали слово!", attemptsLeft);
        }

        String hint = generateHint(normalizedAttempt);

        if (attemptsLeft == 0) {
            return new GameResult(false,
                    "Игра окончена! Загаданное слово: " + secretWord, attemptsLeft);
        }

        return new GameResult(false, "Подсказка: " + hint, attemptsLeft);
    }

    public String getHint() {
        List<String> possibleWords = dictionary.findPossibleWords(
                correctLetters, wrongLetters, correctPositions, wrongPositions);

        if (possibleWords.isEmpty()) {
            return "Нет подходящих слов";
        }

        Random random = new Random();

        return possibleWords.get(random.nextInt(possibleWords.size()));
    }

    private String generateHint(String attempt) {
        StringBuilder  hint = new StringBuilder();

        for (int i = 0; i < 5; i++) {
            char attemptChar = attempt.charAt(i);
            char secretChar = secretWord.charAt(i);

            if (attemptChar == secretChar) {
                hint.append("+");
            } else if (secretWord.indexOf(attemptChar) != -1) {
                hint.append("^");
            } else {
                hint.append("_");
            }
        }

        return hint.toString();
    }

    public int getAttemptsLeft() {
        return attemptsLeft;
    }

    public boolean isGameOver() {
        return attemptsLeft < 0 || previousAttempts.contains(secretWord);
    }

    public String getSecretWord() {
        return secretWord;
    }

    private String normalizeWord(String word) {
        return word.toLowerCase()
                .replace('ё', 'е');
    }
}
