package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public Collection<User> getAllUsers() {
        return userService.getUsers().values();
    }

    @GetMapping  ("/{id}/friends") //возвращаем список пользователей, являющихся его друзьями.
    public Collection<User> getFriendsUserById (@PathVariable long id) {
        return userService.getFriendsListById(id);
    }

    @GetMapping  ("/{id}") //возвращаем пользователя
    public User getUserById (@PathVariable long id) {
        return userService.getUserById(id);
    }

    @GetMapping ("/{id}/friends/common/{otherId}") //список друзей, общих с другим пользователем.
    public List<User> getCommonFriendsList(@PathVariable long id, @PathVariable long otherId) {
        return userService.getCommonFriendsList(id, otherId);
    }

    @PostMapping
    public User create(@RequestBody User user) {
        return  userService.create(user);
    }

    @PutMapping
    public User put(@RequestBody User user) {
        return  userService.update(user);
    }

    @PutMapping("/{id}/friends/{friendId}") //добавление в друзья.
    public void addFriend(@Valid @PathVariable int id, @PathVariable int friendId) {
        userService.addFriends(id, friendId);
    }

    @DeleteMapping ("/{id}/friends/{friendId}") //удаление из друзей.
    public void deleteFriend (@PathVariable int id, @PathVariable int friendId) {
        userService.delFriend(id, friendId);
    }

}
