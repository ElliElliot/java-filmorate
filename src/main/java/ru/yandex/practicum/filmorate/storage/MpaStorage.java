package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import java.util.Collection;

public interface MpaStorage {

    Collection<Mpa> getMPAs();

    Mpa getById(int id);
    void mpaForNewFilm (Film film);
    void deleteMpaFilm (int id);
    void updateMpaFilm (Film film);
    Mpa findMpa(int filmId);
}