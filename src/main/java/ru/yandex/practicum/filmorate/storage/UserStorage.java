package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface UserStorage {

    Collection<User> getUsers();
    User create(User user);
    User update(User user);
    User getUserById(int id);
    User deleteById(int id);
    List<Integer> followUser(int followerId, int followingId);
    List<Integer> unfollowUser(int followerId, int followingId);
    List<User> getFriendsListById(int id);
    List<User> getCommonFriendsList(int firstId, int secondId);

    void validate(int id);
}
