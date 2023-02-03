package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.List;
import java.sql.Date;
import java.util.Objects;

@Repository
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Film> getFilms() {
        final String sqlQuery = "select * from films";
        return jdbcTemplate.query(sqlQuery, this::makeFilm);
    }

    @Override
    public Film create(Film film) {
        final String sqlQuery = "INSERT INTO films (NAME, DESCRIPTION, RELEASE_DATE, DURATION) " +
                "VALUES (?, ?, ?, ?)";
        KeyHolder generatedId = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"FILM_ID"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setLong(4, film.getDuration());
            return stmt;
        }, generatedId);
        film.setId(Objects.requireNonNull(generatedId.getKey()).intValue());
        return film;
    }

    @Override
    public Film update(Film film) {
        final String sqlQuery = "UPDATE films SET NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, " +
                "DURATION = ?" +
                "WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getId());
                film.setMpa(findMpa(film.getId()));
                film.setGenres(findGenres(film.getId()));
        return film;
    }

    @Override
    public Film getFilmById(int id) {
        String checkQuery = "SELECT * FROM films WHERE FILM_ID = ?";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(checkQuery, id);
        if (!filmRows.next()) {
            log.warn("Фильм с id {} не найден.", id);
            throw new NotFoundException("Фильм не найден");
        }
        final String sqlQuery = "SELECT * FROM films WHERE FILM_ID = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::makeFilm, id);
    }

    @Override
    public Film deleteFilmById(int id) {
        Film film = getFilmById(id);
        final String sqlQuery = "DELETE FROM films WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, id);
        return film;
    }

    @Override
    public Film addLike(int filmId, int userId) {
        validate(filmId);
        final String sqlQuery = "INSERT INTO films_likes (FILM_ID_FOR_LIKE, USER_ID_FOR_LIKE) VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, userId);
        log.info("Пользователь c id {} поставил лайк к фильму c id {}", userId, filmId);
        return getFilmById(filmId);
    }

    @Override
    public Film removeLike(int filmId, int userId) {
        validate(filmId);
        final String sqlQuery = "DELETE FROM films_likes " +
                "WHERE FILM_ID_FOR_LIKE = ? AND USER_ID_FOR_LIKE = ?";
        jdbcTemplate.update(sqlQuery, filmId, userId);
        log.info("Пользователь c id {} удалил лайк к фильму c id {}", userId, filmId);
        return getFilmById(filmId);
    }

    @Override
    public List<Film> getTopFilms(int count) {
        String sqlQuery = "SELECT FILM_ID, name, description, release_date, duration " +
                "FROM films " +
                "LEFT JOIN films_likes fl ON films.FILM_ID = fl.FILM_ID_FOR_LIKE " +
                "group by films.FILM_ID, fl.FILM_ID_FOR_LIKE IN ( " +
                "    SELECT FILM_ID " +
                "    FROM films_likes " +
                ") " +
                "ORDER BY COUNT(fl.FILM_ID_FOR_LIKE) DESC " +
                "LIMIT ?";
        return jdbcTemplate.query(sqlQuery, this::makeFilm, count);
    }

    @Override
    public void validate(int filmId) {
        final String checkFilmQuery = "SELECT * FROM films WHERE FILM_ID = ?";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(checkFilmQuery, filmId);
        if (!filmRows.next()) {
            log.warn("Фильм c id {} не найден.", filmId);
            throw new NotFoundException("Фильм не найден");
        }
    }

    private Film makeFilm(ResultSet resultSet, int rowNum) throws SQLException {
        final int id = resultSet.getInt("FILM_ID");
        final String name = resultSet.getString("NAME");
        final String description = resultSet.getString("DESCRIPTION");
        final LocalDate releaseDate = resultSet.getDate("RELEASE_DATE").toLocalDate();
        long duration = resultSet.getLong("DURATION");
        return new Film(id, name, description, releaseDate, duration, findGenres(id), findMpa(id));
    }

    private List<Genre> findGenres(int filmId) {
        final String genresSqlQuery = "SELECT GENRE_ID, NAME " +
                "FROM genre " +
                "LEFT JOIN film_genre FG on genre.GENRE_ID = FG.GENRES_ID " +
                "WHERE FILM_ID = ?";
        return jdbcTemplate.query(genresSqlQuery, this::makeGenre, filmId);
    }

    private Genre makeGenre(ResultSet resultSet, int rowNum) throws SQLException {
        final int id = resultSet.getInt("GENRE_ID");
        final String name = resultSet.getString("NAME");
        return new Genre(id, name);
    }

    private Mpa findMpa(int filmId) {
        final String mpaSqlQuery = "SELECT MPA_ID, NAME " +
                "FROM mpa " +
                "LEFT JOIN FILMS_MPA FM ON mpa.MPA_ID = FM.RATING_MPA_ID " +
                "WHERE FILMS_ID = ?";
        return jdbcTemplate.queryForObject(mpaSqlQuery, this::makeMpa, filmId);
    }

    private Mpa makeMpa(ResultSet resultSet, int rowNum) throws SQLException {
        final int id = resultSet.getInt("MPA_ID");
        final String name = resultSet.getString("NAME");
        return new Mpa(id, name);
    }
}