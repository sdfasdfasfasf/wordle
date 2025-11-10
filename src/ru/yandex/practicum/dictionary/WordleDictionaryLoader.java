package ru.yandex.practicum.dictionary;

import ru.yandex.practicum.exceptions.SystemException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/*
этот класс содержит в себе всю рутину по работе с файлами словарей и с кодировками
    ему нужны методы по загрузке списка слов из файла по имени файла
    на выходе должен быть класс WordleDictionary
 */
public class WordleDictionaryLoader {
    public WordleDictionary loadDictionary(String filename) throws SystemException {
        List<String> words = new ArrayList<>();

        try(BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(filename), "UTF-8"))) {

            String line;

            while((line = reader.readLine()) != null) {
                String normalizeWord = normalizeWord(line.trim());

                if(normalizeWord.length() == 5) {
                    words.add(normalizeWord);
                }
            }
        } catch (FileNotFoundException e) {
            throw new SystemException("Файл словаря не найден: " + filename, e);
        } catch (IOException e) {
            throw new SystemException("Ошибка чтения файла словаря: " + filename, e);
        }

        if(words.isEmpty()) {
            throw new SystemException("Словарь пуст или не содержит подходящих слов");
        }

        WordleDictionary dictionary = new WordleDictionary(words);

        return dictionary;
    }

    private String normalizeWord(String word) {
        return word.toLowerCase()
                .replace('ё', 'е');
    }
}
