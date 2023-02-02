package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.FriendshipStatus;
import ru.yandex.practicum.filmorate.model.User;
import lombok.RequiredArgsConstructor;
import java.sql.PreparedStatement;
import lombok.extern.slf4j.Slf4j;
import java.sql.SQLException;
import java.time.LocalDate;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.List;
import java.sql.Date;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Collection <User> getUsers() {
        String sqlQuery = "SELECT * FROM users";
        log.info("Список пользователей отправлен");
        return jdbcTemplate.query(sqlQuery, this::makeUser);
    }

    @Override
    public User create(User user) {
        final String sqlQuery = "INSERT INTO users (EMAIL, LOGIN, NAME, BIRTHDAY) " +
                "VALUES ( ?, ?, ?, ?)";
        KeyHolder generatedId = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            final PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"USER_ID"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, generatedId);
        log.info("Пользователь с id {} отправлен", user.getId());
        user.setId(generatedId.getKey().intValue());
        return user;
    }

    @Override
    public User update(User user) {
        final String checkQuery = "SELECT * FROM users WHERE USER_ID = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(checkQuery, user.getId());
        if (!userRows.next()) {
            log.warn("Пользователь с id {} не найден", user.getId());
            throw new NotFoundException("Пользователь не найден");
        }
        final String sqlQuery = "UPDATE users SET EMAIL = ?, LOGIN = ?, NAME = ?, BIRTHDAY = ? " +
                "WHERE USER_ID = ?";
        jdbcTemplate.update(sqlQuery,
                user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        log.info("Пользователь с id {} обновлен", user.getId());
        return user;
    }

    @Override
    public User getUserById(int id) {
        final String sqlQuery = "SELECT * FROM users WHERE USER_ID = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (!userRows.next()) {
            log.warn("Пользователь с идентификатором {} не найден.", id);
            throw new NotFoundException("Пользователь не найден");
        }
        final String checkQuery = "select * from users where USER_ID = ?";
        log.info("Пользователь с id {} отправлен", id);
        return jdbcTemplate.queryForObject(checkQuery, this::makeUser, id);
    }

    @Override
    public User deleteById(int id) {
        final String sqlQuery = "DELETE FROM users WHERE USER_ID = ?";
        User user = getUserById(id);
        jdbcTemplate.update(sqlQuery, id);
        log.info("Пользователь с id {} удален", id);
        return user;
    }

    @Override
    public List<Integer> followUser(int followerId, int followingId) {
        validate(followerId, followingId);
        final String sqlForWriteQuery = "INSERT INTO friendship (FRIEND_ID, OTHER_FRIEND_ID, STATUS) " +
                "VALUES (?, ?, ?)";
        final String sqlForUpdateQuery = "UPDATE friendship SET STATUS = ? " +
                "WHERE FRIEND_ID = ? AND OTHER_FRIEND_ID = ?";
        final String checkMutualQuery = "SELECT * FROM friendship WHERE FRIEND_ID = ? AND OTHER_FRIEND_ID = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(checkMutualQuery, followerId, followingId);

        if (userRows.first()) {
            jdbcTemplate.update(sqlForUpdateQuery, FriendshipStatus.CONFIRMED.toString(), followerId, followingId);
        } else {
            jdbcTemplate.update(sqlForWriteQuery, followerId, followingId, FriendshipStatus.REQUIRED.toString());
        }
        log.info("Пользователь {} подписался на {}", followerId, followingId);
        return List.of(followerId, followingId);
    }

    @Override
    public List<Integer> unfollowUser(int followerId, int followingId) {
        final String sqlQuery = "DELETE FROM friendship WHERE FRIEND_ID = ? AND OTHER_FRIEND_ID = ?";
        jdbcTemplate.update(sqlQuery, followerId, followingId);
        log.info("Пользователь {} отписался от {}", followerId, followingId);
        return List.of(followerId, followingId);
    }

    @Override
    public List<User> getFriendsListById(int id) {
        final String checkQuery = "SELECT * FROM users WHERE USER_ID = ?";
        SqlRowSet followingRow = jdbcTemplate.queryForRowSet(checkQuery, id);
        if (!followingRow.next()) {
            log.warn("Пользователь с id {} не найден", id);
            throw new NotFoundException("Пользователь не найден");
        }
        final String sqlQuery = "SELECT USER_ID, EMAIL, LOGIN, NAME, BIRTHDAY " +
                "FROM USERS " +
                "LEFT JOIN friendship mf on users.USER_ID = mf.OTHER_FRIEND_ID " +
                "where FRIEND_ID = ? AND STATUS LIKE 'REQUIRED'";
        log.info("Запрос получения списка друзей пользователя {} выполнен", id);
        return jdbcTemplate.query(sqlQuery, this::makeUser, id);
    }

    @Override
    public List<User> getCommonFriendsList(int followerId, int followingId) {
        validate(followingId, followerId);
        final String sqlQuery = "SELECT USER_ID, EMAIL, LOGIN, NAME, BIRTHDAY " +
                "FROM friendship AS f " +
                "LEFT JOIN users u ON u.USER_ID = f.OTHER_FRIEND_ID " +
                "WHERE f.FRIEND_ID = ? AND f.OTHER_FRIEND_ID IN ( " +
                "SELECT OTHER_FRIEND_ID " +
                "FROM friendship AS f " +
                "LEFT JOIN users AS u ON u.USER_ID = f.OTHER_FRIEND_ID " +
                "WHERE f.FRIEND_ID = ? )";
        log.info("Список общих друзей {} и {} отправлен", followingId, followerId);
        return jdbcTemplate.query(sqlQuery, this::makeUser, followingId, followerId);
    }

    private User makeUser(ResultSet resultSet, int rowNum) throws SQLException {
        int id = resultSet.getInt("USER_ID");
        String email = resultSet.getString("EMAIL");
        String login = resultSet.getString("LOGIN");
        String name = resultSet.getString("NAME");
        LocalDate birthday = resultSet.getDate("BIRTHDAY").toLocalDate();
        return new User(id, email, login, name, birthday);
    }

    private void validate(int userId, int otherUserId) {
        final String check = "SELECT * FROM USERS WHERE USER_ID = ?";
        SqlRowSet followingRow = jdbcTemplate.queryForRowSet(check, userId);
        SqlRowSet followerRow = jdbcTemplate.queryForRowSet(check, otherUserId);
        if (!followingRow.next() || !followerRow.next()) {
            log.warn("Пользователи с id {} и {} не найдены", userId, otherUserId);
            throw new NotFoundException("Пользователи не найдены");
        }
    }
}