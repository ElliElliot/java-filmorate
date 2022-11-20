package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private Map<Long, Film> films = new HashMap<>();
    private FilmService filmService;

    private int filmId = 0;

    @Override
    public Map<Long, Film> getFilms() {
        return films;
    }

    @Override
    public Film create(Film film) {
        film.setId(++filmId);
        films.put(film.getId(), film);
        log.info("Фильм {} добавлен в коллекцию", film.getName());
        return film;
    }

    @Override
    public Film update(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new NotFoundException("Такого фильма нет");
        }
        films.remove(film.getId());
        films.put(film.getId(), film);
        log.info("Информация о фильме {} изменена", film.getName());
        return film;
    }

    @Override
    public Film getFilmById(long id) {
        return films.get(id);
    }

    @Override
    public void deleteFilmById(long id) {
        films.remove(id);
    }
}