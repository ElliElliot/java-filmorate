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
public class FilmController{

    //    PUT /users/{id}/friends/{friendId} — добавление в друзья.
//            DELETE /users/{id}/friends/{friendId} — удаление из друзей.
//            GET /users/{id}/friends — возвращаем список пользователей, являющихся его друзьями.
//            GET /users/{id}/friends/common/{otherId} — список друзей, общих с другим пользователем.
//            PUT /films/{id}/like/{userId} — пользователь ставит лайк фильму.
//    DELETE /films/{id}/like/{userId} — пользователь удаляет лайк.
//            GET /films/popular?count={count} — возвращает список из первых count фильмов по количеству лайков. Если значение параметра count не задано, верните первые 10.


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
}
