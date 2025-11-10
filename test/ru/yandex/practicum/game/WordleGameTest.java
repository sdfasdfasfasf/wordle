package ru.yandex.practicum.game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.dictionary.WordleDictionary;
import ru.yandex.practicum.exceptions.GameException;
import ru.yandex.practicum.exceptions.InvalidWordException;
import ru.yandex.practicum.exceptions.NoAttemptsLeftException;
import ru.yandex.practicum.exceptions.WordNotFoundException;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class WordleGameTest {
    private WordleDictionary dictionary;
    private PrintWriter log;
    private WordleGame game;

    @BeforeEach
    void setUp() {
        List<String> words = Arrays.asList("клава", "слово");
        dictionary = new WordleDictionary(words);
        log = new PrintWriter(new StringWriter());
        game = new WordleGame(dictionary, log);
    }

    @Test
    void testGameCreation() {
        WordleGame game = new WordleGame(dictionary, log);
        assertNotNull(game.getSecretWord());
        assertEquals(6, game.getAttemptsLeft());
    }

    @Test
    void testCorrectGuess() {
        String secretWord = game.getSecretWord();

        GameResult result = game.makeAttempt(secretWord);

        assertTrue(result.isWin());
        assertTrue(result.getMessage().contains(("Поздравляем")));
        assertEquals(5, result.getAttemptsLeft());
        assertTrue(game.isGameOver());
    }

    @Test
    void testDictionaryContains() {
        assertTrue(dictionary.contains("клава"));
        assertFalse(dictionary.contains("твтрв"));
    }

    @Test
    void testIncorrectGuess() {
        String wrongWord = "слово"; // предполагаем, что это не загаданное слово

        // Сохраняем загаданное слово для проверки
        String secretWord = game.getSecretWord();
        if (wrongWord.equals(secretWord)) {
            wrongWord = "клава"; // выбираем другое слово
        }

        GameResult result = game.makeAttempt(wrongWord);

        assertFalse(result.isWin());
        assertTrue(result.getMessage().contains("Подсказка"));
        assertEquals(5, result.getAttemptsLeft());
        assertFalse(game.isGameOver());
    }

    @Test
    void testWordNotFoundException() {
        assertThrows(WordNotFoundException.class, () -> {
            game.makeAttempt("гонец");
        });

        // Количество попыток не должно уменьшиться
        assertEquals(6, game.getAttemptsLeft());
    }

    @Test
    void testInvalidWordException() {
        assertThrows(InvalidWordException.class, () -> {
            game.makeAttempt("аб"); // слишком короткое
        });

        assertThrows(InvalidWordException.class, () -> {
            game.makeAttempt("абвгде"); // слишком длинное
        });

        // Количество попыток не должно уменьшиться
        assertEquals(6, game.getAttemptsLeft());
    }

    @Test
    void testGameOverAfterWin() {
        String secretWord = game.getSecretWord();
        game.makeAttempt(secretWord);

        assertTrue(game.isGameOver());
    }

    @Test
    void testHintGeneration() {
        String secretWord = game.getSecretWord();
        String hint = game.getHint();

        assertNotNull(hint);
        assertFalse(hint.isEmpty());
        assertTrue(dictionary.contains(hint));
    }

    @Test
    void testMultipleAttempts() {
        String wrongWord = getWrongWord(game.getSecretWord());

        // Первая попытка
        GameResult result1 = game.makeAttempt(wrongWord);
        assertFalse(result1.isWin());
        assertEquals(5, result1.getAttemptsLeft());

        // Вторая попытка
        GameResult result2 = game.makeAttempt(wrongWord);
        assertFalse(result2.isWin());
        assertEquals(4, result2.getAttemptsLeft());

        assertFalse(game.isGameOver());
    }

    @Test
    void testHintAfterAttempts() {
        // Делаем несколько попыток
        String wrongWord = getWrongWord(game.getSecretWord());
        game.makeAttempt(wrongWord);
        game.makeAttempt(wrongWord);

        String hint = game.getHint();
        assertNotNull(hint);
        assertTrue(dictionary.contains(hint));
    }

    private String getWrongWord(String secretWord) {
        // Возвращаем слово, которое точно не является загаданным
        for (String word : Arrays.asList("слово", "клава")) {
            if (!word.equals(secretWord)) {
                return word;
            }
        }
        return "стул";
    }
}
