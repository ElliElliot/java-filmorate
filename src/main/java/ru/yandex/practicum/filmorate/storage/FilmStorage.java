package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Map;

public interface FilmStorage { //будут определены методы добавления, удаления и модификации объектов.
    Map<Long, Film> getFilms();
    Film create(Film film);
    Film update(Film film);
    Film getFilmById(long id);
    void deleteFilmById(long id);
}
