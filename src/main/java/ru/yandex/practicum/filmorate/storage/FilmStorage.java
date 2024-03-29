package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

public interface FilmStorage {
    Collection<Film> getFilms();
    Film create(Film film);
    Film update(Film film);
    Film getFilmById(int id);
    Film deleteFilmById(int id);
    Film addLike(int filmId, int userId);
    Film removeLike(int filmId, int userId);
    List<Film> getTopFilms(int count);
    void validate(int id);

}
