package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;


import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping("/films")
public class FilmController{

    private final FilmService filmService;
    @GetMapping
    public Collection<Film> getAllFilms() { //    получение всех фильмов.
        return filmService.getFilms();
    }

    @GetMapping ("/popular")
    public List<Film>  getTopFilms (@RequestParam(defaultValue = "10", required = false) Integer count) {
        return filmService.getTopFilms(count);
    }

    @GetMapping  ("/{id}") //возвращаем пользователя
    public Film getFilmById (@PathVariable int id) {
        return filmService.getFilmById(id);
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {//    добавление фильма;
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {//    обновление фильма;
        return filmService.update(film);
    }

    @PutMapping("/{id}/like/{userId}")  //пользователь ставит лайк фильму.
    public void addLike (@PathVariable int id, @PathVariable int userId) {
        filmService.addLike(id, userId);
    }

    @DeleteMapping ("/{id}/like/{userId}") //пользователь удаляет лайк.
    public void removeLike (@PathVariable int id, @PathVariable int userId) {
        filmService.removeLike(id, userId);
    }

    @DeleteMapping("/{id}")
    public Film deleteFilmById(@PathVariable int id) {
        return filmService.deleteFilmById(id);
    }
}
