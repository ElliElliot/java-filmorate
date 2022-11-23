package ru.yandex.practicum.filmorate.controller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


public class FilmControllerTests {
//    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
//    private Validator validator = factory.getValidator();
//    FilmController filmController;
//    FilmStorage filmStorage;
//
//    @BeforeEach
//    public void setUp() {
//        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
//        validator = factory.getValidator();
//    }
//
//    @Test
//    void releaseDateBefore1895Test() { //тест неправильной даты выхода
//        Film film = new Film("Film", "releaseDateBefore1895Test",
//                LocalDate.of(1600, 01, 02), 120);
//        //System.out.println(assertThrows(ValidationException.class, () -> filmValidate.validate(film)));
//    }
//
//    @Test
//    void duplicateFilmTest(){//тест на дубль фильма
//        Film film = new Film("Film", "duplicateFilmTest",
//                LocalDate.of(2006, 01, 02), 120);
//        film.setId(1);
//        filmStorage.getFilms().put(film.getId(), film);
//        //System.out.println(assertThrows(ValidationException.class, () -> validate(film)));
//    }
//
//    @Test
//    void emptyNameTest() {//тест на пустое или из пробелов название фильма
//        Film film = new Film("", "emptyNameTest", LocalDate.of(2006, 01, 02), 120);
//        Film filmTwo = new Film(" ", "emptyNameTest", LocalDate.of(2006, 01, 02), 120);
//        Set<ConstraintViolation<Film>> violations = validator.validate(film);
//        Set<ConstraintViolation<Film>> violationsTwo = validator.validate(filmTwo);
//        assertFalse(violations.isEmpty());
//        assertThat(violations.size()).isEqualTo(1);
//        assertFalse(violationsTwo.isEmpty());
//        assertThat(violationsTwo.size()).isEqualTo(1);
//    }
//
//    @Test
//    void blancDescriptionTest() { //тест на пустое описание фильма или из пробелов
//        Film film = new Film("Film", " ", LocalDate.of(2006, 01, 02), 120);
//        Film filmTwo = new Film("FilmTwo", "", LocalDate.of(2006, 01, 02), 120);
//        Set<ConstraintViolation<Film>> violations = validator.validate(film);
//        Set<ConstraintViolation<Film>> violationsTwo = validator.validate(filmTwo);
//        assertFalse(violations.isEmpty());
//        assertFalse(violationsTwo.isEmpty());
//        assertThat(violations.size()).isEqualTo(1);
//        assertThat(violationsTwo.size()).isEqualTo(1);
//    }
//
//    @Test
//    void durationNotNullTest() {//тесть на нулевую или отрицательную продолжительность фильма
//        Film film = new Film("Film", "durationNotNullTest", LocalDate.of(2006, 01, 02), 0);
//        Set<ConstraintViolation<Film>> violations= validator.validate(film);
//        assertFalse(violations.isEmpty());
//        assertThat(violations.size()).isEqualTo(1);
//    }
//
//    @Test
//    void longDescriptionTest() {//тест на слишком длинное описание фильма
//        String longDescription = "Однажды посетители бара наблюдали, как Эрнест Хемингуэй поспорил на 10 долларов" +
//                "что напишет самый короткий рассказ, трогательный, но при этом состоящий всего из шести слов." +
//                "Спор он выиграл, представив оппоненту емкую и душещипательную строчку «For sale: baby shoes, never worn».";
//        Film film = new Film("Film", longDescription,
//                LocalDate.of(1894, 01, 02), 60);
//        Set<ConstraintViolation<Film>> violations = validator.validate(film);
//        assertFalse(violations.isEmpty());
//        System.out.println(assertThat(violations.size()).isEqualTo(1));
//    }
}