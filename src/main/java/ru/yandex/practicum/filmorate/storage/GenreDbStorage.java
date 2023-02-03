package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.sql.SQLException;
import java.util.Collection;
import java.sql.ResultSet;

@Slf4j
@Repository
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Genre> getGenres() {
        String sqlQuery = "SELECT * FROM genre";
        return jdbcTemplate.query(sqlQuery, this::makeGenre);
    }

    @Override
    public Genre getById(long id) {
        final String sqlQuery = "SELECT * FROM genre WHERE GENRE_ID = ?";
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (!genreRows.next()) {
            log.warn("Жанр c id {} не найден.", id);
            throw new NotFoundException("Жанр не найден");
        }
        return jdbcTemplate.queryForObject(sqlQuery, this::makeGenre, id);
    }

    @Override
    public void genresForNewFilm(Film film) {
        final String genresSqlQuery = "INSERT INTO film_genre (FILM_ID, GENRES_ID) VALUES (?, ?)";
        if (film.getGenres() != null) {
            for (Genre g : film.getGenres()) {
                jdbcTemplate.update(genresSqlQuery, film.getId(), g.getId());
            }
        }
    }

    @Override
    public void deleteGenresFilm (int id) {
        final String genresSqlQuery = "DELETE FROM film_genre WHERE FILM_ID = ?";
        jdbcTemplate.update(genresSqlQuery, id);
    }

    @Override
    public void updateGenresFilm (Film film){
        if (film.getGenres() != null) {
            final String deleteGenresQuery = "DELETE FROM film_genre WHERE FILM_ID = ?";
            final String updateGenresQuery = "INSERT INTO film_genre (FILM_ID, GENRES_ID) VALUES (?, ?)";
            jdbcTemplate.update(deleteGenresQuery, film.getId());
            for (Genre g : film.getGenres()) {
                String checkDuplicate = "SELECT * FROM film_genre WHERE FILM_ID = ? AND GENRES_ID = ?";
                SqlRowSet checkRows = jdbcTemplate.queryForRowSet(checkDuplicate, film.getId(), g.getId());
                if (!checkRows.next()) {
                    jdbcTemplate.update(updateGenresQuery, film.getId(), g.getId());
                }
            }
        }
    }

    private Genre makeGenre(ResultSet resultSet, int rowNum) throws SQLException {
        int id = resultSet.getInt("GENRE_ID");
        String nameGenre = resultSet.getString("NAME");
        return new Genre(id, nameGenre);
    }
}