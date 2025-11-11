package ru.yandex.practicum.exceptions;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExceptionsTest {
    @Test
    void testGameException() {
        GameException exception = new GameException("Тестовое сообщение");
        assertEquals("Тестовое сообщение", exception.getMessage());
    }

    @Test
    void testWordNotFoundException() {
        WordNotFoundException exception = new WordNotFoundException("тест");
        assertEquals("Слово 'тест' не найдено в словаре", exception.getMessage());
        assertTrue(exception instanceof GameException);
    }

    @Test
    void testInvalidWordException() {
        InvalidWordException exception = new InvalidWordException("Неверная длина");
        assertEquals("Неверная длина", exception.getMessage());
        assertTrue(exception instanceof GameException);
    }

    @Test
    void testNoAttemptsLeftException() {
        NoAttemptsLeftException exception = new NoAttemptsLeftException();
        assertEquals("Попытки закончились", exception.getMessage());
        assertTrue(exception instanceof GameException);
    }

    @Test
    void testSystemException() {
        SystemException exception = new SystemException("Системная ошибка");
        assertEquals("Системная ошибка", exception.getMessage());

        Throwable cause = new IOException("IO error");
        SystemException exceptionWithCause = new SystemException("Ошибка", cause);
        assertEquals("Ошибка", exceptionWithCause.getMessage());
        assertEquals(cause, exceptionWithCause.getCause());
    }
}
