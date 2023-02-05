package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {
    private int id; //идентификатор пользователя
    @NotBlank
    @Email
    @NotNull
    private String email; //электронная почта  пользователя
    @NotBlank
    @NotNull
    private String login; // логин пользователя
    private String name; //имя пользователя для отображения
    @NotNull
    @PastOrPresent
    private LocalDate birthday; //дата рождения пользователя
}
