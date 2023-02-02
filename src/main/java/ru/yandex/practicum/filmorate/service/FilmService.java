package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class FilmService {
    private static final LocalDate FIRST_FILM_DATE = LocalDate.of(1895, 12, 28);
    private final  FilmStorage filmStorage;

    public Collection<Film> getFilms(){
        log.info("Отправлен список фильмов");
        return filmStorage.getFilms();
    }

    public Film create(Film film) {
        validate(film);
        return filmStorage.create(film);
    }
    public Film update(Film film) {
        validate(film);
        return filmStorage.update(film);
    }
    public Film getFilmById(int id) {
        return filmStorage.getFilmById(id);
    }

    public List<Film> getTopFilms (int count) {
        log.info("Список популярных фильмов отправлен");
        return filmStorage.getTopFilms(count);
    }

    public void addLike(int filmId, int userId) {
        filmStorage.addLike(filmId, userId);
        log.info("Пользователь {} поставил лайк фильму {}", userId, filmId);
    }

    public void removeLike(int filmId, int userId) {
            filmStorage.removeLike(filmId, userId);
            log.info("Пользователь {} удалил лайк к фильму {}", userId, filmId);
    }
    public Film deleteFilmById(int id) {
        return  filmStorage.deleteFilmById(id);
    }

    private void validate(Film film) {
        if (film.getReleaseDate().isBefore(FIRST_FILM_DATE))
            throw new ValidationException("В то время кино еще не было");
    }

}
