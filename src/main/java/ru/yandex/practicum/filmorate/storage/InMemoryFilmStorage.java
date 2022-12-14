package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@Component
@RequiredArgsConstructor
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private Map<Long, Film> films = new HashMap<>();

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
            log.error("Ошибка 404 при обновлении фильма {}", film);
            throw new NotFoundException("Такого фильма нет");
        } else {
            films.remove(film.getId());
            films.put(film.getId(), film);
            log.info("Информация о фильме {} изменена", film.getName());
            return film;
        }
    }

    @Override
    public Film getFilmById(long id) {
        return films.get(id);
    }
}