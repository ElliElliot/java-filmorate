package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    public Map<Integer, User> users = new HashMap<>(); //список юзеров
    private int userId = 0;

    @GetMapping
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        validate(user);
        user.setId(++userId);
        users.put(user.getId(), user);
        log.info("Добавлен пользователь, логин: {}", user.getLogin());
        return user;
    }

    @PutMapping
    public User put(@RequestBody User user) {
        validate(user);
        if (!users.containsKey(user.getId()))
            throw new ValidationException("Пользователя с таким id не существует, зарегистрируйте нового пользователя");
        users.remove(user.getId());
        users.put(user.getId(), user);
        log.info("Информация о пользователе {} обновлена", user.getLogin());
        return user;
    }
    void validate(@RequestBody User user) {
        if (user.getLogin().contains(" ")) {
            log.warn("Логин юзера '{}'", user.getLogin());
            throw new ValidationException("Логин не может содержать пробелы");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        Collection<User> userCollection = users.values();
        for (User us : userCollection) {
            if (user.getLogin().equals(us.getLogin()) ) {
                log.warn("user e-mail: '{}'\n us email: {}", user, us);
                throw new ValidationException("Пользователь с таким login уже существует");
            } else if (user.getEmail().equals(us.getEmail())) {
                log.warn("user e-mail: '{}'\n us email: {}", user, us);
                throw new ValidationException("Пользователь с таким email уже существует");

            }
        }
    }
}
