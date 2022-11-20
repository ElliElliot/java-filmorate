package ru.yandex.practicum.filmorate.service;

import com.sun.jdi.InternalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    public Map<Long, User> getUsers() {
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
    public User getUserById(long id){
        return userStorage.getUserById(id);
    }
    public void deleteUserById(long id){
        userStorage.deleteUserById(id);
    }
    public Collection<User> findAll() {
        return userStorage.getUsers().values();
    }

    public void addFriends (long id, long friendId) { //добавление в друзья.
        if (!getUsers().containsKey(id) || !getUsers().containsKey(friendId)) {
            throw new ValidationException("Один из пользователей не существует");
        }
        User user = userStorage.getUserById(id);
        User friend = userStorage.getUserById(friendId);
        if (user.getFriendList().contains(friend.getId())) {
            throw new InternalException("Этот пользователь уже ваш друг");
        } else {
            user.getFriendList().add(friend.getId());
            friend.getFriendList().add(user.getId());
            log.info("Пользователи {} и {} теперь друзья", friend.getLogin(), user.getLogin());
        }
    }

    public void delFriend (long id, long friendId) { //удаление из друзей.
        User user = userStorage.getUserById(id);
        User friend = userStorage.getUserById(friendId);
        if (!userStorage.getUsers().containsKey(id) || !userStorage.getUsers().containsKey(friendId)) {
            throw new NotFoundException("Пользователь с id {} не найден");
        }
        if (!user.getFriendList().contains(friend.getId())) {
            throw new InternalException("Этот пользователь не является вашим другом");
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

    public void validate(User user) {
        if (user.getLogin().contains(" ")) {
            log.warn("Логин юзера '{}'", user.getLogin());
            throw new ValidationException("Логин не может содержать пробелы");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        Collection<User> userCollection = userStorage.getUsers().values();
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
