package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
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
    private final UserStorage userStorage;

    public Map<Long, Film> getFilms(){
        return filmStorage.getFilms();
    }

    public Film create(Film film) {
        if (!validate(film)) {
            throw new ValidationException("Ошибка валидации");
        }
        return filmStorage.create(film);
    }
    public Film update(Film film) {
        if (checkFilm(film.getId())==false) {
            throw new NotFoundException("Такой фильм не существует");
        }
        if (!validate(film)) {
            throw new ValidationException("Ошибка валидации");
        }
        return filmStorage.update(film);
    }
    public Film getFilmById(long id) {
        if (checkFilm(id)==false) {
            throw new NotFoundException("Такой фильм не существует");
        }
        return filmStorage.getFilmById(id);
    }

    public Collection<Film> findAll(){
        return filmStorage.getFilms().values();
    }

    public List<Film> getTopFilms (int count) {
        log.info("Список популярных фильмов отправлен");
        return findAll().stream()
                .sorted((o1, o2) -> Integer.compare(o2.getUsersLikes().size(), o1.getUsersLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }

    public void addLike(long filmId, long userId) {
        if (!filmStorage.getFilms().containsKey(filmId)) {
            throw new NotFoundException("Фильм не найден");
        }
        if (!userStorage.getUsers().containsKey(userId)) {
            throw new NotFoundException("Пользователь не найден");
        }
        filmStorage.getFilmById(filmId).getUsersLikes().add((int) userId);
        log.info("Пользователь {} поставил лайк фильму {}", userId, filmId);
    }

    public void removeLike(long filmId, long userId) {
        if (!filmStorage.getFilms().containsKey(filmId)) {
            throw new NotFoundException("Фильм не найден");
        }
        if (!userStorage.getUsers().containsKey(userId)) {
            throw new NotFoundException("Пользователь не найден");
        }
            filmStorage.getFilmById(filmId).getUsersLikes().remove(userId);
            log.info("Пользователь {} удалил лайк к фильму {}", userId, filmId);

    }

    public boolean validate(Film film) {
        if (film.getReleaseDate().isBefore(DATE)) {
            log.warn("film.getReleaseDate film release date: '{}'", film.getReleaseDate());
            return false;
        } else if (film.getDuration() < 0) {
            log.warn("film.getDuration film duration: {}", film.getDuration());
            return false;
        }
        Collection<Film> filmCollection = filmStorage.getFilms().values();
        for (Film fl : filmCollection) {
            if (film.getName().equals(fl.getName()) && film.getReleaseDate().equals(fl.getReleaseDate())) {
                log.warn("film film: '{}'\n fl film: {}", film, fl);
                return false;
            }
        }
        return true;
    }

    private boolean checkFilm (long id) {
        if (!getFilms().containsKey(id)) {
            return false;
        }
        return true;
    }
}
