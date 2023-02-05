package ru.yandex.practicum.filmorate.storage;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.assertj.core.api.AssertionsForClassTypes;
import ru.yandex.practicum.filmorate.model.User;
import org.assertj.core.api.Assertions;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import java.util.Collection;
import java.time.LocalDate;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserStorageTests {

    private final UserDbStorage userDbStorage;

    @Test
    void createUserTest() {
        User user = User.builder()
                .email("example@mail.ma")
                .login("login")
                .name("Name")
                .birthday(LocalDate.of(1995, 1, 24))
                .build();
        userDbStorage.create(user);
        AssertionsForClassTypes.assertThat(user).extracting("id").isNotNull();
        AssertionsForClassTypes.assertThat(user).extracting("name").isNotNull();
    }

    @Test
    void findUserByIdTest() {
        User user = User.builder()
                .email("example@mail.ma")
                .login("login")
                .name("Name")
                .birthday(LocalDate.of(1995, 1, 24))
                .build();
        userDbStorage.create(user);
        AssertionsForClassTypes.assertThat(userDbStorage.getUserById(user.getId())).hasFieldOrPropertyWithValue("id", user.getId());
    }

    @Test
    void getAllUsersTest() {
        User user = User.builder()
                .email("example@mail.ma")
                .login("login")
                .name("Name")
                .birthday(LocalDate.of(1995, 1, 24))
                .build();
        userDbStorage.create(user);
        Collection<User> users = userDbStorage.getUsers();
        Assertions.assertThat(users).isNotEmpty().isNotNull().doesNotHaveDuplicates();
        Assertions.assertThat(users).extracting("email").contains(user.getEmail());
        Assertions.assertThat(users).extracting("login").contains(user.getLogin());
    }

    @Test
    void removeUserByIdTest() {
        User user = User.builder()
                .email("example@mail.ma")
                .login("login")
                .name("Name")
                .birthday(LocalDate.of(2000, 12, 22))
                .build();
        userDbStorage.create(user);
        userDbStorage.deleteById(user.getId());
        Assertions.assertThatThrownBy(()->userDbStorage.getUserById(user.getId()))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void updateUserByIdTest() {
        User user = User.builder()
                .email("example@mail.ma")
                .login("login")
                .name("Name")
                .birthday(LocalDate.of(1995, 1, 24))
                .build();
        userDbStorage.create(user);
        user.setName("testUpdateName");
        user.setLogin("testUpdateLogin");
        user.setEmail("updateExample@mail.ma");
        userDbStorage.update(user);
        AssertionsForClassTypes.assertThat(userDbStorage.getUserById(user.getId()))
                .hasFieldOrPropertyWithValue("login", "testUpdateLogin")
                .hasFieldOrPropertyWithValue("name", "testUpdateName")
                .hasFieldOrPropertyWithValue("email", "updateExample@mail.ma");
    }

    @Test
    public void testUpdateUserNotFound() {
        User user = User.builder()
                .id(99999)
                .login("testLogin")
                .email("example@mail.ma")
                .birthday(LocalDate.of(1995, 1, 24))
                .build();
        Assertions.assertThatThrownBy(() -> userDbStorage.update(user))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void addFriendshipTest() {
        User friend = User.builder()
                .email("example_friend@mail.ma")
                .login("friendLogin")
                .name("Name")
                .birthday(LocalDate.of(1995, 1, 24))
                .build();
        User follower = User.builder()
                .email("example_followerd@mail.ma")
                .login("followerLogin")
                .name("NameName")
                .birthday(LocalDate.of(2000, 1, 24))
                .build();
        userDbStorage.create(friend);
        userDbStorage.create(follower);
        assertThat(userDbStorage.getFriendsListById(friend.getId()).isEmpty());
        userDbStorage.followUser(friend.getId(), follower.getId());
        assertThat(userDbStorage.getFriendsListById(friend.getId())).isNotNull();
        Assertions.assertThat(userDbStorage.getFriendsListById(friend.getId()).size() == 2);
    }

    @Test
    void getFriendshipTest() {
        User friend = User.builder()
                .email("example_friend@mail.ma")
                .login("friendLogin")
                .name("Name")
                .birthday(LocalDate.of(2000, 12, 22))
                .build();
        User follower = User.builder()
                .email("example_followerd@mail.ma")
                .login("followerLogin")
                .name("NameName")
                .birthday(LocalDate.of(2000, 10, 20))
                .build();
        userDbStorage.create(friend);
        userDbStorage.create(follower);
        assertThat(userDbStorage.getFriendsListById(friend.getId()).isEmpty());
        userDbStorage.followUser(friend.getId(), follower.getId());
        Assertions.assertThat(userDbStorage.getFriendsListById(friend.getId()).size() == 2);
    }

    @Test
    void removeFriendshipTest() {
        User friend = User.builder()
                .email("example_friend@mail.ma")
                .login("friendLogin")
                .name("Name")
                .birthday(LocalDate.of(1995, 1, 24))
                .build();
        User follower = User.builder()
                .email("example_followerd@mail.ma")
                .login("followerLogin")
                .name("NameName")
                .birthday(LocalDate.of(2000, 1, 24))
                .build();
        userDbStorage.create(friend);
        userDbStorage.create(follower);
        assertThat(userDbStorage.getFriendsListById(friend.getId()).isEmpty());
        userDbStorage.followUser(friend.getId(), follower.getId());
        assertThat(userDbStorage.getFriendsListById(friend.getId())).isNotNull();
        Assertions.assertThat(userDbStorage.getFriendsListById(friend.getId()).size() == 2);
        userDbStorage.unfollowUser(friend.getId(), follower.getId());
        Assertions.assertThat(userDbStorage.getFriendsListById(friend.getId()).size() == 1);
    }

    @Test
    void getCommonFriendshipTest() {
        User friend = User.builder()
                .email("example_friend@mail.ma")
                .login("friendLogin")
                .name("Name")
                .birthday(LocalDate.of(1005, 1, 24))
                .build();
        User follower = User.builder()
                .email("example_followerd@mail.ma")
                .login("followerLogin")
                .name("NameName")
                .birthday(LocalDate.of(2000, 1, 24))
                .build();
        User following = User.builder()
                .email("example_followingd@mail.ma")
                .login("following")
                .name("NameNameName")
                .birthday(LocalDate.of(2005, 1, 24))
                .build();
        userDbStorage.create(friend);
        userDbStorage.create(follower);
        userDbStorage.create(following);
        userDbStorage.followUser(friend.getId(), following.getId());
        userDbStorage.followUser(follower.getId(), following.getId());
        Assertions.assertThat(userDbStorage.getCommonFriendsList(friend.getId(), follower.getId()).size() == 1);
    }
}