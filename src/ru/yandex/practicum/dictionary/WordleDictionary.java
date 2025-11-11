package ru.yandex.practicum.dictionary;

import ru.yandex.practicum.exceptions.InvalidWordException;
import ru.yandex.practicum.exceptions.SystemException;

import java.util.List;
import java.util.*;
/*
этот класс содержит в себе список слов List<String>
    его методы похожи на методы списка, но учитывают особенности игры
    также этот класс может содержать рутинные функции по сравнению слов, букв и т.д.
 */

public class WordleDictionary {
    private List<String> words;
    private Set<String> wordSet;

    public  WordleDictionary(List<String> words) {
        if (words == null || words.isEmpty()) {
            throw new SystemException("Словарь не может быть пустым");
        }

        this.words = new ArrayList<>(words);
        this.wordSet = new HashSet<>(words);
    }

    public List<String> getWords(){
        return words;
    }

    public boolean contains(String word) throws InvalidWordException {
        if (word == null || word.length() != 5) {
            throw new InvalidWordException("Слово должно состоять из 5 букв");
        }

        String normalizedWord = normalizeWord(word);

        return wordSet.contains(normalizedWord);
    }

    public String getRandomWord() {
        Random random = new Random();

        return words.get(random.nextInt(words.size()));
    }

    public List<String> findPossibleWords(Set<Character> correctLetters,
                                          Set<Character> wrongLetters,
                                          Map<Integer, Character> correctPositions,
                                          Map<Integer, Set<Character>> wrongPositions) {
        List<String> possibleWords = new ArrayList<>();

        for (String word : words) {
            if(isWordPossible(word, correctLetters, wrongLetters, correctPositions, wrongPositions)) {
                possibleWords.add(word);
            }
        }

        return possibleWords;
    }

    private boolean isWordPossible(String word,
                                   Set<Character> correctLetters,
                                   Set<Character> wrongLetters,
                                   Map<Integer, Character> correctPositions,
                                   Map<Integer, Set<Character>> wrongPositions) {

        for (Map.Entry<Integer, Character> entry : correctPositions.entrySet()) {
            int position = entry.getKey();
            char expectedChar = entry.getValue();

            if (word.charAt(position) != expectedChar) {
                return false;
            }
        }

        for(Map.Entry<Integer, Set<Character>> entry : wrongPositions.entrySet()) {
            int position = entry.getKey();
            Set<Character> invalidChars = entry.getValue();

            if (invalidChars.contains(word.charAt(position))) {
                return false;
            }
        }

        for (char letter : correctLetters) {
            if(word.indexOf(letter) == -1) {
                return false;
            }
        }

        for (char letter : wrongLetters) {
            if(word.indexOf(letter) != -1) {
                return false;
            }
        }

        return true;
    }

    private String normalizeWord(String word) {
        return word.toLowerCase()
                .replace('ё', 'е');
    }
}
