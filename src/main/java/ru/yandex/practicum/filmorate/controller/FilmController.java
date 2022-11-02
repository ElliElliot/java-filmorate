package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import java.util.Collection;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>(); //список фильмов
    private static final LocalDate DATE = LocalDate.of(1895, 12, 28);
    private int id = 0;

    @GetMapping
    public Collection<Film> getAllFilms() { //    получение всех фильмов.
        return films.values();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {//    добавление фильма;
        validate(film);
        film.setId(++id);
        films.put(film.getId(), film);
        log.info("Фильм {} добавлен", film.getName());
        return film;
    }

    @PutMapping
    public Film put(@Valid @RequestBody Film film) {//    обновление фильма;
        validate(film);
        if (!films.containsKey(film.getId())) throw new ValidationException("Такого фильма нет");
        films.remove(film.getId());
        films.put(film.getId(), film);
        log.info("Информация о фильме {} изменена", film.getName());
        return film;
    }

    private void validate(@Valid @RequestBody Film film) {
        if (film.getReleaseDate().isBefore(DATE)) {
            log.warn("film.getReleaseDate film release date: '{}'", film.getReleaseDate());
            throw new ValidationException("Неверная дата релиза");
        } else if (film.getDuration() < 0) {
            log.warn("film.getDuration film duration: {}", film.getDuration());
            throw new ValidationException("Неверная длительность фильма");
        }
        Collection<Film> filmCollection = films.values();
        for (Film fl : filmCollection) {
            if (film.getName().equals(fl.getName()) && film.getReleaseDate().equals(fl.getReleaseDate())) {
                log.warn("film film: '{}'\n fl film: {}", film, fl);
                throw new ValidationException("Фильм уже добавлен");
            }
        }
    }
}
