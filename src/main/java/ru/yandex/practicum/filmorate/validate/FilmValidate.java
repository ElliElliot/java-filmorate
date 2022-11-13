package ru.yandex.practicum.filmorate.validate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;

@Slf4j
public class FilmValidate {
    private static final LocalDate DATE = LocalDate.of(1895, 12, 28);
    private FilmStorage filmStorage;

    void validate(@Valid @RequestBody Film film) {
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
