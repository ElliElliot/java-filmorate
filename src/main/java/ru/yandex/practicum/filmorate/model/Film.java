package ru.yandex.practicum.filmorate.model;

import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
public class Film {

    @PositiveOrZero
    private int id; //идентификатор фильма
    @NotBlank
    private String name; //название фильма
    @NotBlank
    @Size(max = 200, message = "Краткость - сестра таланта. Уложитесь в 200 символов")
    private String description; //описание фильма
    @NotNull
    private LocalDate releaseDate; //дата выхода фильма
    @Positive
    private long duration; //длительность фильма

    private List<Genre> genres; //жанр
    private Mpa mpa; //рейтинг Ассоциации кинокомпаний (англ. Motion Picture Association, сокращённо МРА)
}
