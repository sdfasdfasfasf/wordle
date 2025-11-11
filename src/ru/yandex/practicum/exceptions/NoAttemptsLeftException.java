package ru.yandex.practicum.exceptions;

public class NoAttemptsLeftException extends GameException {
    public NoAttemptsLeftException() {
        super("Попытки закончились");
    }
}
