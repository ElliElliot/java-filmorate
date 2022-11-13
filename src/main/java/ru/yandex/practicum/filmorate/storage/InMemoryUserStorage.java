package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validate.UserValidate;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage{ //перенесите туда всю логику хранения, обновления и поиска объектов.
    Map<Long, User> users = new HashMap<>(); //список юзеров
    long id = 0;
    UserValidate userValidate;

    @Override
    public Map<Long, User> getUsers() {
        return users;
    }

    @Override
    public User create(User user) {
        userValidate.validate(user);
        user.setId(++id);
        users.put(user.getId(), user);
        log.info("Добавлен пользователь, логин: {}", user.getLogin());
        return user;
    }

    @Override
    public User update(User user) {
        if (!users.containsKey(user.getId())) {
            throw new ValidationException("Пользователя с таким id не существует, зарегистрируйте нового пользователя");
        }
        userValidate.validate(user);
        users.remove(user.getId());
        users.put(user.getId(), user);
        log.info("Информация о пользователе {} обновлена", user.getLogin());
        return user;
    }

    @Override
    public User getUserById(long id) {
        log.info("Отправлен пользователь с id {} ", id);
        return users.get(id);
    }

    @Override
    public void deleteUserById(long id) {
        log.info("Удален пользователь с id {} ", id);
        users.remove(id);
    }

    @Override
    public void deleteAllUsers() {
        log.info("Удалены все пользователи");
        users.clear();
    }
}
