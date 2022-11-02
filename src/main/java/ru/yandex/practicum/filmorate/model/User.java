package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@Data
public class User {
    private int id; //идентификатор пользователя
    @NotBlank
    @Email
    private String email; //электронная почта  пользователя
    @NotBlank
    private String login; // логин пользователя
    private String name; //имя пользователя для отображения
    @NotNull
    @PastOrPresent
    private LocalDate birthday; //дата рождения пользователя
}
