package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Map;

public interface UserStorage { //будут определены методы добавления, удаления и модификации объектов

    //возвращаем список пользователей, являющихся его друзьями.

    Map<Long, User> getUsers();
    void deleteAllUsers();
    User create(User user);
    User update(User user);
    User getUserById(long id);
    void deleteUserById(long id);

}
