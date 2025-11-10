package ru.yandex.practicum;

import ru.yandex.practicum.dictionary.WordleDictionary;
import ru.yandex.practicum.dictionary.WordleDictionaryLoader;
import ru.yandex.practicum.exceptions.GameException;
import ru.yandex.practicum.exceptions.SystemException;
import ru.yandex.practicum.game.WordleGame;
import ru.yandex.practicum.game.GameResult;

import java.io.*;
import java.util.*;

/*
в главном классе нам нужно:
    создать лог-файл (он должен передаваться во все классы)
    создать загрузчик словарей WordleDictionaryLoader
    загрузить словарь WordleDictionary с помощью класса WordleDictionaryLoader
    затем создать игру WordleGame и передать ей словарь
    вызвать игровой метод в котором в цикле опрашивать пользователя и передавать информацию в игру
    вывести состояние игры и конечный результат
 */
public class Wordle {
    private static final String DICTIONARY_FILE = "russian_nouns.txt";
    private static final String LOG_FILE = "wordle.log";

    public static void main(String[] args) {
        PrintWriter log = null;

        try {
            log = createLogWriter();

            WordleDictionaryLoader loader = new WordleDictionaryLoader();
            WordleDictionary dictionary = loader.loadDictionary(DICTIONARY_FILE);

            log.println("" + dictionary.getWords().size());

            WordleGame game = new WordleGame(dictionary, log);

            runGame(game, log);
        } catch(SystemException e) {
            System.err.println("Системная ошибка: " + e.getMessage());

            if(log != null) {
                log.println("Системная ошибка: " + e.getMessage());
                e.printStackTrace(log);
            }
        } catch(Exception e) {
            System.err.println("Непредвиденная ошибка: " + e.getMessage());

            if (log != null) {
                log.println("НЕПРЕДВИДЕННАЯ ОШИБКА: " + e.getMessage());
                e.printStackTrace(log);
            }
        } finally {
            if(log != null) {
                log.close();
            }
        }
    }

    private static PrintWriter createLogWriter() throws SystemException {
        try {
            return new PrintWriter(new FileWriter(LOG_FILE, true), true);
        } catch(IOException e) {
            throw new SystemException("Не удалось создать лог-файл: " + LOG_FILE, e);
        }
    }

    private static void runGame(WordleGame game, PrintWriter log) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Добро пожаловать в Wordle!");
        System.out.println("У вас есть 6 попыток чтобы угадать слово из 5 букв.");
        System.out.println("Символы подсказки: + - правильная позиция, ^ - есть в слове, _ - нет в слове");
        System.out.println("Для подсказки нажмите Enter без ввода слова.");
        System.out.println();

        while(!game.isGameOver()) {
            System.out.println("Введите слово (" + game.getAttemptsLeft() + " попыток осталось): ");
            String input = scanner.nextLine().trim();

            try {
                if(input.isEmpty()) {
                    String hint = game.getHint();
                    System.out.println("Подсказка: " + hint);
                    continue;
                }

                GameResult result = game.makeAttempt(input);
                System.out.println(result.getMessage());
                System.out.println();

                if(result.isWin() || game.getAttemptsLeft() == 0) {
                    break;
                }
            } catch (GameException e) {
                System.out.println("Ошибка: " + e.getMessage());
                System.out.println("Попробуйте еще раз.");

                log.println("Игровая ошибка: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Произошла непредвиденная ошибка. Попробуйте еще раз.");
                log.println("Ошибка в игровом процессе: " + e.getMessage());
                e.printStackTrace(log);
            }
        }

        scanner.close();
        System.out.println("Спасибо за игру!");
        log.println("Игра завершена");
    }
}
