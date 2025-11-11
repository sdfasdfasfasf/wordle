package ru.yandex.practicum.game;

public class GameResult {
    private final boolean win;
    private final String message;
    private final int attemptsLeft;

    public GameResult(boolean win, String message, int attemptsLeft) {
        this.win = win;
        this.message = message;
        this.attemptsLeft = attemptsLeft;
    }

    public boolean isWin() {
        return win;
    }

    public String getMessage() {
        return message;
    }

    public int getAttemptsLeft() {
        return attemptsLeft;
    }
}
