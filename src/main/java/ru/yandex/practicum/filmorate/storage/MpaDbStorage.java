package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.sql.SQLException;
import java.util.Collection;
import java.sql.ResultSet;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection<Mpa> getMPAs() {
        String sqlQuery = "SELECT * FROM mpa";
        return jdbcTemplate.query(sqlQuery, this::makeMpa);
    }

    @Override
    public Mpa getById(int id) {
        String sqlQuery = "SELECT * FROM mpa WHERE MPA_ID = ?";
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (!mpaRows.next()) {
            log.warn("Рейтинг c id {} не найден.", id);
            throw new NotFoundException("Рейтинг не найден");
        }
        return jdbcTemplate.queryForObject(sqlQuery, this::makeMpa, id);
    }

    @Override
    public void mpaForNewFilm (Film film) {
        final String mpaSqlQuery = "INSERT INTO films_mpa (FILMS_ID, RATING_MPA_ID) VALUES (?, ?)";
        jdbcTemplate.update(mpaSqlQuery, film.getId(), film.getMpa().getId());
    }

    @Override
    public void deleteMpaFilm (int id){
        String mpaSqlQuery = "DELETE FROM films_mpa WHERE FILMS_ID = ?";
        jdbcTemplate.update(mpaSqlQuery, id);
    }

    @Override
    public void updateMpaFilm (Film film){
        if (film.getMpa() != null) {
            final String deleteMpa = "DELETE FROM films_mpa WHERE FILMS_ID = ?";
            final String updateMpa = "INSERT INTO films_mpa (FILMS_ID, RATING_MPA_ID) VALUES (?, ?)";
            jdbcTemplate.update(deleteMpa, film.getId());
            jdbcTemplate.update(updateMpa, film.getId(), film.getMpa().getId());
        }
    }

    @Override
    public Mpa findMpa(int filmId) {
        final String mpaSqlQuery = "SELECT MPA_ID, NAME " +
                "FROM mpa " +
                "LEFT JOIN FILMS_MPA FM ON mpa.MPA_ID = FM.RATING_MPA_ID " +
                "WHERE FILMS_ID = ?";
        return jdbcTemplate.queryForObject(mpaSqlQuery, this::makeMpa, filmId);
    }

    private Mpa makeMpa(ResultSet resultSet, int rowNum) throws SQLException {
        int id = resultSet.getInt("MPA_ID");
        String nameMpa = resultSet.getString("NAME");
        return new Mpa(id, nameMpa);
    }
}