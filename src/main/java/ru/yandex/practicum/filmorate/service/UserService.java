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
        log.info("Список пользователей отправлен");
       return userStorage.getUsers();
    }
    public User create(User user){
        if (!validate(user)) {
            log.error("Ошибка 400 при создании пользователя {}", user);
            throw new ValidationException("Ошибка валидации");
        }
        return userStorage.create(user);
    }
    public User update(User user){
        if (checkUser(user.getId())==false) {
            log.error("Ошибка 404 при попытке обновления пользователя {}", user);
            throw new NotFoundException("Такой фильм не существует");
        }
        if (!validate(user)) {
            log.error("Ошибка 400 при попытке обновления пользователя {}", user);
            throw new ValidationException("Ошибка валидации");
        }
        return userStorage.update(user);
    }
    public User getUserById(long id){
        if (checkUser(id)==false) {
            log.error("Ошибка 404 при попытке получить пользователя по id {}", id);
            throw new NotFoundException("Такой пользователь не существует");
        }
        return userStorage.getUserById(id);
    }

    public void addFriends (long id, long friendId) { //добавление в друзья.
        if (checkUser(id)==false || checkUser(friendId)==false) {
            log.error("Ошибка 404 при попытке сделать друзьями пользователей с id {} и {}", id, friendId);
            throw new NotFoundException("Пользователь не существует");
        }
        User user = userStorage.getUserById(id);
        User friend = userStorage.getUserById(friendId);
        if (user.getFriendList().contains(friendId)) {
            throw new InternalException("Этот пользователь уже ваш друг");
        }
            user.getFriendList().add(friend.getId());
            friend.getFriendList().add(user.getId());
            log.info("Пользователи {} и {} теперь друзья", friend.getLogin(), user.getLogin());
    }

    public void delFriend (long id, long friendId) { //удаление из друзей.
        if (checkUser(id)==false || checkUser(friendId)==false) {
            log.error("Ошибка 404 при попытке прекратить дружбу пользователей с id {} и {}", id, friendId);
            throw new NotFoundException("Пользователь не найден");
        }
        User user = userStorage.getUserById(id);
        User friend = userStorage.getUserById(friendId);
        if (!user.getFriendList().contains(friend.getId())) {
            log.error("Ошибка 500 при попытке прекратить дружбу пользователей с id {} и {}", id, friendId);
            throw new InternalException("Этот пользователь не является вашим другом");
        } else {
            user.getFriendList().remove(friend);
            friend.getFriendList().remove(user);
            log.info("Пользователи {} и {} больше не являются друзьями", friend.getLogin(), user.getLogin());
        }
    }

    public List<User> getFriendsListById(long id) {
        if (!userStorage.getUsers().containsKey(id)) {
            log.error("Ошибка 404 при получении списка друзей пользователя c id {}", id);
            throw new NotFoundException("Пользователь не найден");
        }
        List <User> friendsList = new ArrayList<>();
        for (Long friendId: userStorage.getUserById(id).getFriendList()) {
            friendsList.add(userStorage.getUserById(friendId));
        }
        log.info("Запрос на получение списка друзей пользователя {} выполнен", userStorage.getUserById(id).getName());
        return friendsList;
    }

    public List<User> getCommonFriendsList(long id, long otherId) {
        if (!userStorage.getUsers().containsKey(id) || !userStorage.getUsers().containsKey(otherId)) {
            log.error("Ошибка 404 при получении общего списка друзей пользователя c id {} и {}", id, otherId);
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

    private boolean validate(User user) {
        if (user.getLogin().contains(" ")) {
            log.warn("Логин юзера '{}'", user.getLogin());
            return false;
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        Collection<User> userCollection = userStorage.getUsers().values();
        for (User us : userCollection) {
            if (user.getLogin().equals(us.getLogin()) ) {
                log.warn("user e-mail: '{}'\n us email: {}", user, us);
                return false;
            } else if (user.getEmail().equals(us.getEmail())) {
                log.warn("user e-mail: '{}'\n us email: {}", user, us);
                return false;
            }
        }
        return true;
    }

    private boolean checkUser (long id) {
        if (!getUsers().containsKey(id)) {
            return false;
        }
        return true;
    }
}
