package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    UserStorage userStorage;
    UserService userService;

    @GetMapping
    public Collection<User> getAllUsers() {
        return userStorage.getUsers().values();
    }

    @GetMapping  ("/{id}/friends") //возвращаем список пользователей, являющихся его друзьями.
    public Collection<User> getFriendsUserById (@PathVariable long id) {
        return userService.getFriendsListById(id);
    }

    @GetMapping ("/{id}/friends/common/{otherId}") //список друзей, общих с другим пользователем.
    public List<User> getCommonFriendsList(@PathVariable long id, @PathVariable long otherId) {
        return userService.getCommonFriendsList(id, otherId);
    }

    @PostMapping
    public User create(@RequestBody User user) {
        return  userStorage.create(user);
    }

    @PutMapping
    public User put(@RequestBody User user) {
        return  userStorage.update(user);
    }

    @PutMapping("/{id}/friends/{friendId}") //добавление в друзья.
    public void addFriend(@PathVariable int id, @PathVariable int friendId) {
        userService.addFriends(id, friendId);
    }

    @DeleteMapping ("/{id}/friends/{friendId}") //удаление из друзей.
    public void deleteFriend (@PathVariable int id, @PathVariable int friendId) {
        userService.delFriend(id, friendId);
    }

}
