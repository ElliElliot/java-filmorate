package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FriendException;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    private UserStorage userStorage;

    public Collection<User> findAll() {
        return userStorage.getUsers().values();
    }

    public void addFriends (long id, long friendId) { //добавление в друзья.
        User user = userStorage.getUserById(id);
        User friend = userStorage.getUserById(friendId);
        if (user.getFriendList().contains(friend.getId())) {
            throw new FriendException("Этот пользователь уже ваш друг");
        } else {
            user.getFriendList().add(friend.getId());
            friend.getFriendList().add(user.getId());
            log.info("Пользователи {} и {} теперь друзья", friend.getLogin(), user.getLogin());
        }
    }

    public void delFriend (long id, long friendId) { //удаление из друзей.
        User user = userStorage.getUserById(id);
        User friend = userStorage.getUserById(friendId);
        if (!userStorage.getUsers().containsKey(friend.getId()) || !userStorage.getUsers().containsKey(user.getId())) {
            throw new NotFoundException("Пользователь с id {} не найден");
        }
        if (user.getFriendList().contains(friend.getId())) {
            throw new FriendException("Этот пользователь не является вашим другом");
        } else {
            user.getFriendList().remove(friend);
            friend.getFriendList().remove(user);
            log.info("Пользователи {} и {} больше не являются друзьями", friend.getLogin(), user.getLogin());
        }
    }

    public List<User> getFriendsListById(long id) {
        if (!userStorage.getUsers().containsKey(id)) {
            throw new NotFoundException("Пользователь не найден");
        }
        log.info("Запрос на получение списка друзей пользователя {} выполнен", userStorage.getUserById(id).getName());
        List <User> friendsList = new ArrayList<>();
        for (Long friendId: userStorage.getUserById(id).getFriendList()) {
            friendsList.add(userStorage.getUserById(friendId));
        }
        return friendsList;
    }

    public List<User> getCommonFriendsList(long id, long otherId) { //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        if (!userStorage.getUsers().containsKey(id) || !userStorage.getUsers().containsKey(otherId)) {
            throw new NotFoundException("Пользователи не найдены");
        }
        User user = userStorage.getUserById(id);
        User otherUser = userStorage.getUserById(otherId);
        log.info("Список общих друзей {} и {} отправлен", user.getName(), otherUser.getName());

        return user.getFriendList().stream()
                .filter(friendId -> otherUser.getFriendList().contains(friendId))
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }
}
