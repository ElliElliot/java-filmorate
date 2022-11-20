package ru.yandex.practicum.filmorate.exception;

public class InternalException extends RuntimeException {

    public InternalException (String massage) {
        super(massage);
    }
}