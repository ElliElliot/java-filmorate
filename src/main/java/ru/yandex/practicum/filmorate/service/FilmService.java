package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {

    private static final LocalDate DATE = LocalDate.of(1895, 12, 28);
    private final  FilmStorage filmStorage;

    public Map<Long, Film> getFilms(){
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
    public Film getFilmById(long id) {
        return filmStorage.getFilmById(id);
    }

    public Collection<Film> findAll(){
        return filmStorage.getFilms().values();
    }

    public List<Film> getTopFilms (int count) {
        log.info("Список популярных фильмов отправлен");
        return filmStorage.getFilms().values().stream()
                .sorted((o1, o2) -> Integer.compare(o2.getUsersLikes().size(), o1.getUsersLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }

    public void addLike(long filmId, long userId) {
        if (!filmStorage.getFilms().containsKey(filmId)) {
            throw new NotFoundException("Фильм не найден");
        }
        filmStorage.getFilmById(filmId).getUsersLikes().add((int) userId);
        log.info("Пользователь {} поставил лайк фильму {}", userId, filmId);
    }

    public void removeLike(long filmId, long userId) {
        if (!filmStorage.getFilms().containsKey(filmId)) {
            throw new NotFoundException("Фильм не найден");
        }
        if (!filmStorage.getFilmById(filmId).getUsersLikes().contains(userId)) {
            throw new NotFoundException("Лайк от пользователя отсутствует");
        }
        filmStorage.getFilmById(filmId).getUsersLikes().remove(userId);
        log.info("Пользователь {} удалил лайк к фильму {}", userId, filmId);
    }

    public void validate(Film film) {
        if (film.getReleaseDate().isBefore(DATE)) {
            log.warn("film.getReleaseDate film release date: '{}'", film.getReleaseDate());
            throw new ValidationException("Неверная дата релиза");
        } else if (film.getDuration() < 0) {
            log.warn("film.getDuration film duration: {}", film.getDuration());
            throw new ValidationException("Неверная длительность фильма");
        }
        Collection<Film> filmCollection = filmStorage.getFilms().values();
        for (Film fl : filmCollection) {
            if (film.getName().equals(fl.getName()) && film.getReleaseDate().equals(fl.getReleaseDate())) {
                log.warn("film film: '{}'\n fl film: {}", film, fl);
                throw new ValidationException("Фильм уже добавлен");
            }
        }
    }
}
