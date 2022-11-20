package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@RequiredArgsConstructor
public class Film {
    private long id; //идентификатор фильма
    @NotBlank
    private final String name; //название фильма
    @NotBlank
    @Size(max = 200, message = "Краткость - сестра таланта. Уложитесь в 200 символов")
    private final String description; //описание фильма
    @NotNull
    private final LocalDate releaseDate; //дата выхода фильма
    @Positive
    private final long duration; //длительность фильма
    private int like;
    private Set<Integer> usersLikes = new HashSet<>();
}
