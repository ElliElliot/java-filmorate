package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class Film {
    private int id; //идентификатор фильма
    @NotBlank
    private final String name; //название фильма
    @Size(max = 200, message = "Краткость - сестра таланта. Уложитесь в 200 символов")
    private  String description; //описание фильма
    @NotNull
    //дата релиза — не раньше 28 декабря 1895 года
    private final LocalDate releaseDate; //дата выхода фильма
    @Positive
    private final long duration; //длительность фильма
}
