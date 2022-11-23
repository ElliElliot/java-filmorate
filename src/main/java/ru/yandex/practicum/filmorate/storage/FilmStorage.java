package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Map;

public interface FilmStorage {
    Map<Long, Film> getFilms();
    Film create(Film film);
    Film update(Film film);
    Film getFilmById(long id);
}
