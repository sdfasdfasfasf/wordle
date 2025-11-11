package ru.yandex.practicum.dictionary;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.exceptions.InvalidWordException;
import ru.yandex.practicum.exceptions.SystemException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class WordleDictionaryTest {
    private WordleDictionary dictionary;

    @BeforeEach
    void setUp() {
        List<String> words = Arrays.asList("клава", "слово");
        dictionary = new WordleDictionary(words);
    }

    @Test
    void testDictionaryCreationWithEmptyList() {
        assertThrows(SystemException.class, () -> {
            new WordleDictionary(Collections.emptyList());
        });
    }

    @Test
    void testDictionaryCreationWithNull() {
        assertThrows(SystemException.class, () -> {
            new WordleDictionary(null);
        });
    }

    @Test
    void testContainsWord() {
        assertTrue(dictionary.contains("кЛАва"));
        assertTrue(dictionary.contains("СЛОВО"));
        assertTrue(dictionary.contains("слово"));
    }

    @Test
    void testContainsInvalidWord() {
        assertThrows(InvalidWordException.class, () -> {
            dictionary.contains("аб");
        });

        assertThrows(InvalidWordException.class, () -> {
            dictionary.contains("абвгде");
        });

        assertThrows(InvalidWordException.class, () -> {
            dictionary.contains(null);
        });
    }

    @Test
    void testGetRandomWord() {
        String word1 = dictionary.getRandomWord();
        String word2 = dictionary.getRandomWord();

        assertNotNull(word1);
        assertNotNull(word2);
        assertTrue(dictionary.contains(word1));
        assertTrue(dictionary.contains(word2));

        // Проверяем, что слова из словаря
        List<String> words = dictionary.getWords();
        assertTrue(words.contains(word1));
        assertTrue(words.contains(word2));
    }

    @Test
    void testFindPossibleWords() {
        Set<Character> correctLetters = new HashSet<>(Arrays.asList('с', 'т'));
        Set<Character> wrongLetters = new HashSet<>(Arrays.asList('х', 'з'));
        Map<Integer, Character> correctPositions = new HashMap<>();
        correctPositions.put(0, 'с');
        Map<Integer, Set<Character>> wrongPositions = new HashMap<>();
        wrongPositions.put(1, new HashSet<>(Arrays.asList('х')));

        List<String> possibleWords = dictionary.findPossibleWords(
                correctLetters, wrongLetters, correctPositions, wrongPositions);

        assertNotNull(possibleWords);
        // Должны найти слова, начинающиеся на 'с'
        assertTrue(possibleWords.stream().allMatch(word -> word.startsWith("с")));
    }

    @Test
    void testFindPossibleWordsNoRestrictions() {
        List<String> possibleWords = dictionary.findPossibleWords(
                Collections.emptySet(),
                Collections.emptySet(),
                Collections.emptyMap(),
                Collections.emptyMap()
        );

        assertEquals(2, possibleWords.size());
    }
}