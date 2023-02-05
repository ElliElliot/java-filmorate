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
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmStorageTest {

    private final FilmService filmService;

    private final UserService userService;

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
        filmService.create(film);
        AssertionsForClassTypes.assertThat(film).extracting("id").isNotNull();
        AssertionsForClassTypes.assertThat(film).extracting("name").isNotNull();
    }

    @Test
    void updateFilmTest() {
        filmService.create(film);
        film.setName("testUpdateFilm");
        film.setDescription("testUpdateDescription");
        filmService.update(film);
        AssertionsForClassTypes.assertThat(filmService.getFilmById(film.getId()))
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
        Assertions.assertThatThrownBy(() -> filmService.update(filmForUpdate))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void getFilmTest() {
        filmService.create(film);
        filmService.getFilmById(film.getId());
        AssertionsForClassTypes.assertThat(filmService.getFilmById(film.getId())).hasFieldOrPropertyWithValue("id", film.getId());
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
        userService.create(user);
        filmService.create(filmForLike);
        System.out.println(user.getId() + " - Это UserId!");
        System.out.println(filmForLike.getId() + " - Это FilmId!");
        filmService.addLike(filmForLike.getId(), user.getId());
        assertThat(filmService.getTopFilms(filmForLike.getId()).isEmpty());
        assertThat(filmService.getTopFilms(filmForLike.getId())).isNotNull();
        Assertions.assertThat(filmService.getTopFilms(filmForLike.getId()).size() == 2);
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
        userService.create(user1);
        filmService.create(filmForLike);
        filmService.create(filmForLike);
        filmService.addLike(filmForLike.getId(), user1.getId());
        filmService.removeLike(filmForLike.getId(), user1.getId());
        assertThat(filmService.getTopFilms(filmForLike.getId()).isEmpty());
        assertThat(filmService.getTopFilms(filmForLike.getId())).isNotNull();
        Assertions.assertThat(filmService.getTopFilms(filmForLike.getId()).size() == 1);
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
        filmService.create(film);
        filmService.create(filmForLike);
        filmService.create(otherFilmForLike);
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
        userService.create(user);
        userService.create(user1);
        userService.create(user2);
        filmService.addLike(film.getId(), user.getId());
        filmService.addLike(filmForLike.getId(), user1.getId());
        filmService.addLike(otherFilmForLike.getId(), user2.getId());
        filmService.addLike(film.getId(), user1.getId());
        filmService.addLike(film.getId(), user2.getId());
        assertThat(filmService.getTopFilms(film.getId())).isNotNull();
        Assertions.assertThat(filmService.getTopFilms(film.getId()).size() == 6);
    }
}