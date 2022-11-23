package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage{
    Map<Long, User> users = new HashMap<>();
    long id = 0;

    @Override
    public Map<Long, User> getUsers() {
        return users;
    }


    @Override
    public User create(User user) {
        user.setId(++id);
        users.put(user.getId(), user);
        log.info("Добавлен пользователь, логин: {}", user.getLogin());
        return user;
    }

    @Override
    public User update(User user) {
        if (!users.containsKey(user.getId())) {
            throw new ValidationException("Пользователя с таким id не существует, зарегистрируйте нового пользователя");
        } else {
            users.remove(user.getId());
            users.put(user.getId(), user);
            log.info("Информация о пользователе {} обновлена", user.getLogin());
            return user;
        }
    }

    @Override
    public User getUserById(long id) {
        log.info("Отправлен пользователь с id {} ", id);
        return users.get(id);
    }
}
