package ru.yandex.practicum.filmorate.storage;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.assertj.core.api.AssertionsForClassTypes;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import org.assertj.core.api.Assertions;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmStorageTest {

    private final FilmDbStorage filmDbStorage;

    private final UserDbStorage userDbStorage;

    private Film film = Film.builder()
            .name("testFilm")
            .description("description")
            .releaseDate(LocalDate.of(2022, 1, 24))
            .duration(110)
            .mpa(new Mpa(1, "G"))
            .genres(null)
            .build();

    @Test
    void addFilmTest() {
        filmDbStorage.create(film);
        AssertionsForClassTypes.assertThat(film).extracting("id").isNotNull();
        AssertionsForClassTypes.assertThat(film).extracting("name").isNotNull();
    }

    @Test
    void updateFilmTest() {
        filmDbStorage.create(film);
        film.setName("testUpdateFilm");
        film.setDescription("testUpdateDescription");
        filmDbStorage.update(film);
        AssertionsForClassTypes.assertThat(filmDbStorage.getFilmById(film.getId()))
                .hasFieldOrPropertyWithValue("name", "testUpdateFilm")
                .hasFieldOrPropertyWithValue("description", "testUpdateDescription");
    }

    @Test
    void updateFilmNotFoundTest() {
        Film filmForUpdate = Film.builder()
                .id(9999)
                .name("testFilm")
                .description(("description"))
                .releaseDate(LocalDate.of(2022, 1, 24))
                .mpa(new Mpa(1, "G"))
                .genres(null)
                .build();
        Assertions.assertThatThrownBy(() -> filmDbStorage.update(filmForUpdate))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void getFilmTest() {
        filmDbStorage.create(film);
        filmDbStorage.getFilmById(film.getId());
        AssertionsForClassTypes.assertThat(filmDbStorage.getFilmById(film.getId())).hasFieldOrPropertyWithValue("id", film.getId());
    }

    @Test
    void addLikeFilmTest() {
        User user = User.builder()
                .id(1)
                .email("example@mail.mail")
                .login("login")
                .name("Name")
                .birthday(LocalDate.of(1995, 1, 24))
                .build();
        Film filmForLike = Film.builder()
                .name("testFilm")
                .description("description")
                .releaseDate(LocalDate.of(2020, 1, 24))
                .duration(123)
                .mpa(new Mpa(1, "G"))
                .genres(null)
                .build();
        userDbStorage.create(user);
        filmDbStorage.create(filmForLike);
        System.out.println(user.getId() + " - Это UserId!");
        System.out.println(filmForLike.getId() + " - Это FilmId!");
        filmDbStorage.addLike(filmForLike.getId(), user.getId());
        assertThat(filmDbStorage.getTopFilms(filmForLike.getId()).isEmpty());
        assertThat(filmDbStorage.getTopFilms(filmForLike.getId())).isNotNull();
        Assertions.assertThat(filmDbStorage.getTopFilms(filmForLike.getId()).size() == 2);
    }

    @Test
    void removeFilmLikeTest() {
        User user1 = User.builder()
                .id(1)
                .email("exampleee@mail.mail")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(1995, 1, 24))
                .build();
        Film filmForLike = Film.builder()
                .name("testFilm")
                .description("description")
                .releaseDate(LocalDate.of(2022, 1, 24))
                .duration(123)
                .mpa(new Mpa(1, "G"))
                .genres(null)
                .build();
        userDbStorage.create(user1);
        filmDbStorage.create(filmForLike);
        filmDbStorage.create(filmForLike);
        filmDbStorage.addLike(filmForLike.getId(), user1.getId());
        filmDbStorage.removeLike(filmForLike.getId(), user1.getId());
        assertThat(filmDbStorage.getTopFilms(filmForLike.getId()).isEmpty());
        assertThat(filmDbStorage.getTopFilms(filmForLike.getId())).isNotNull();
        Assertions.assertThat(filmDbStorage.getTopFilms(filmForLike.getId()).size() == 1);
    }

    @Test
    void getBestFilmTest() {
        Film filmForLike = Film.builder()
                .name("testFilm")
                .description("description")
                .releaseDate(LocalDate.of(2023, 1, 24))
                .duration(123)
                .mpa(new Mpa(1, "G"))
                .genres(null)
                .build();
        Film otherFilmForLike = Film.builder()
                .name("testFilm")
                .description("description")
                .releaseDate(LocalDate.of(2020, 1, 24))
                .duration(123)
                .mpa(new Mpa(1, "G"))
                .genres(null)
                .build();
        filmDbStorage.create(film);
        filmDbStorage.create(filmForLike);
        filmDbStorage.create(otherFilmForLike);
        User user = User.builder()
                .id(1)
                .email("example@mail.mail")
                .login("login")
                .name("Name")
                .birthday(LocalDate.of(2000, 1, 24))
                .build();
        User user1 = User.builder()
                .id(1)
                .email("example@mail.mail")
                .login("login")
                .name("Name")
                .birthday(LocalDate.of(2000, 1, 24))
                .build();
        User user2= User.builder()
                .id(1)
                .email("example@mail.mail")
                .login("login")
                .name("Name")
                .birthday(LocalDate.of(2000, 1, 24))
                .build();
        userDbStorage.create(user);
        userDbStorage.create(user1);
        userDbStorage.create(user2);
        filmDbStorage.addLike(film.getId(), user.getId());
        filmDbStorage.addLike(filmForLike.getId(), user1.getId());
        filmDbStorage.addLike(otherFilmForLike.getId(), user2.getId());
        filmDbStorage.addLike(film.getId(), user1.getId());
        filmDbStorage.addLike(film.getId(), user2.getId());
        assertThat(filmDbStorage.getTopFilms(film.getId())).isNotNull();
        Assertions.assertThat(filmDbStorage.getTopFilms(film.getId()).size() == 6);
    }
}