package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    public Collection<User> getUsers() {
       log.info("Список пользователей отправлен");
       return userStorage.getUsers();
    }
    public User create(User user){
        validate(user);
        return userStorage.create(user);
    }
    public User update(User user){
        validate(user);
        return userStorage.update(user);
    }
    public User getUserById(int id){
        return userStorage.getUserById(id);
    }

    public void addFriends (int id, int friendId) { //добавление в друзья.
        User user = userStorage.getUserById(id);
        User friend = userStorage.getUserById(friendId);
        userStorage.followUser(id, friendId);
            log.info("Пользователи {} и {} теперь друзья", friend.getLogin(), user.getLogin());
    }

    public void delFriend (int id, int friendId) { //удаление из друзей.
        userStorage.unfollowUser(id, friendId);
        log.info("Пользователи {} и {} теперь не друзья", id, friendId);
    }

    public List<User> getFriendsListById(int id) {
        log.info("Запрос на получение списка друзей пользователя {} выполнен", userStorage.getUserById(id).getName());
        return userStorage.getFriendsListById(id);
    }

    public List<User> getCommonFriendsList(int id, int otherId) {
        User user = userStorage.getUserById(id);
        User otherUser = userStorage.getUserById(otherId);
        log.info("Список общих друзей {} и {} отправлен", user.getName(), otherUser.getName());
        return userStorage.getCommonFriendsList(id, otherId);
    }

    private void validate(User user) {
        if (user.getName() == null || user.getName().isBlank()) user.setName(user.getLogin());
    }
}
