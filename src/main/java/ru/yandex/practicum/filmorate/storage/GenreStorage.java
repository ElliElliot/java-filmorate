package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;

public interface GenreStorage {
    Collection<Genre> getGenres();
    Genre getById(long id);
    void genresForNewFilm (Film film);
    void deleteGenresFilm (int id);
    void updateGenresFilm (Film film);
}
