package ru.yandex.practicum.dictionary;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import ru.yandex.practicum.exceptions.SystemException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WordleDictionaryLoaderTest {
    @TempDir
    Path tempDir;

    @Test
    void testLoadDictionarySuccess() throws IOException {
        File dictionaryFile = tempDir.resolve("test_dict.txt").toFile();

        try(FileWriter writer = new FileWriter(dictionaryFile)) {
            writer.write("стол\nстул\nноут\nмышь\nклава\n");
        }

        WordleDictionaryLoader loader = new WordleDictionaryLoader();
        WordleDictionary dictionary = loader.loadDictionary(dictionaryFile.getAbsolutePath());

        List<String> words = dictionary.getWords();
        assertNotNull(words);
        assertEquals(1, words.size());
        assertTrue(words.contains("клава"));
    }

    @Test
    void testLoadDictionaryWithNormalization() throws IOException {
        File dictionaryFile = tempDir.resolve("test_dict.txt").toFile();
        try (FileWriter writer = new FileWriter(dictionaryFile)) {
            writer.write("пчёла\nсъём\nмышь\n");
        }

        WordleDictionaryLoader loader = new WordleDictionaryLoader();
        List<String> words = loader.loadDictionary(dictionaryFile.getAbsolutePath()).getWords();

        assertEquals("пчела", words.get(0)); // ё → е
    }

    @Test
    void testLoadDictionaryFileNotFound() {
        WordleDictionaryLoader loader = new WordleDictionaryLoader();

        SystemException exception = assertThrows(SystemException.class, () -> {
            loader.loadDictionary("nonexistent_file.txt");
        });

        assertTrue(exception.getMessage().contains("Файл словаря не найден"));
    }

    @Test
    void testLoadDictionaryEmptyFile() throws IOException {
        File dictionaryFile = tempDir.resolve("empty_dict.txt").toFile();
        dictionaryFile.createNewFile();

        WordleDictionaryLoader loader = new WordleDictionaryLoader();

        SystemException exception = assertThrows(SystemException.class, () -> {
            loader.loadDictionary(dictionaryFile.getAbsolutePath());
        });

        assertTrue(exception.getMessage().contains("Словарь пуст или не содержит подходящих слов"));
    }

    @Test
    void testLoadDictionaryOnlyInvalidWords() throws IOException {
        File dictionaryFile = tempDir.resolve("invalid_dict.txt").toFile();
        try (FileWriter writer = new FileWriter(dictionaryFile)) {
            writer.write("а\nаб\nабв\nабвг\nабвгде\n"); // только неподходящие длины
        }

        WordleDictionaryLoader loader = new WordleDictionaryLoader();

        SystemException exception = assertThrows(SystemException.class, () -> {
            loader.loadDictionary(dictionaryFile.getAbsolutePath());
        });

        assertTrue(exception.getMessage().contains("Словарь пуст или не содержит подходящих слов"));
    }
}