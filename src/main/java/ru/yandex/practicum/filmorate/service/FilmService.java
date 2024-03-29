package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.MpaStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class FilmService {
    private static final LocalDate FIRST_FILM_DATE = LocalDate.of(1895, 12, 28);
    private final  FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final MpaStorage mpaStorage;
    private final GenreStorage genreStorage;

    public Collection<Film> getFilms(){
        log.info("Отправлен список фильмов");
        return filmStorage.getFilms();
    }

    public Film create(Film film) {
        validateReleaseDate(film);
        Film newFilm = filmStorage.create(film);
        mpaStorage.mpaForNewFilm(film);
        genreStorage.genresForNewFilm(film);
        newFilm.setMpa(mpaStorage.findMpa(film.getId()));
        newFilm.setGenres(genreStorage.findGenres(film.getId()));
        return newFilm;
    }
    public Film update(Film film) {
        validateReleaseDate(film);
        filmStorage.validate(film.getId());
        mpaStorage.updateMpaFilm(film);
        genreStorage.updateGenresFilm(film);
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
        userStorage.validate(userId);
        filmStorage.addLike(filmId, userId);
        log.info("Пользователь {} поставил лайк фильму {}", userId, filmId);
    }

    public void removeLike(int filmId, int userId) {
        userStorage.validate(userId);
        filmStorage.removeLike(filmId, userId);
        log.info("Пользователь {} удалил лайк к фильму {}", userId, filmId);
    }
    public Film deleteFilmById(int id) {
        genreStorage.deleteGenresFilm(id);
        mpaStorage.deleteMpaFilm(id);
        return  filmStorage.deleteFilmById(id);
    }

    private void validateReleaseDate(Film film) {
        if (film.getReleaseDate().isBefore(FIRST_FILM_DATE))
            throw new ValidationException("В то время кино еще не было");
    }
}
