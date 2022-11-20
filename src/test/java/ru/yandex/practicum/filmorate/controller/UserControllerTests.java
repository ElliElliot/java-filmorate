package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTests  {
    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private Validator validator = factory.getValidator();
    UserController userController;
    UserStorage userStorage;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        //userController = new UserController();
    }

    @Test
    void loginToNameTest() {
        User user = new User("login@test.ru", "loginToNameTest", LocalDate.of(1995, 01, 24));
        user.setName(" ");
        //userValidate.validate(user);
        assertNotNull(user.getName());
        assertEquals(user.getName(), user.getLogin());
    }

    @Test
    void duplicateUserTest() { //тест одного и того же пользователя
        User user = new User("login@test.ru", "duplicateUserTest", LocalDate.of(1995, 01, 24));
        user.setId(1);
        userStorage.getUsers().put(user.getId(), user);
        //System.out.println(assertThrows(ValidationException.class, () -> userValidate.validate(user)));
    }

    @Test
    void emailNullTest() {//тест пустой почты
        User user = new User(null, "emailNullTest", LocalDate.of(1995, 01, 24));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    void loginNullTest() { //тест логина пустой
        User user = new User("login@test.ru", null, LocalDate.of(1995, 01, 24));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    void birthdateNullTest() { //тест с пустым д.р.
        User user = new User("login@test.ru", "birthdateNullTest", null);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertThat(violations.size()).isEqualTo(1);
    }

    @Test
    void birthdateIncorrectTest() { //тест с др из будущего
        User user = new User("login@test.ru", "userFuture", LocalDate.of(2025, 01, 24));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty());
        assertThat(violations.size()).isEqualTo(1);
    }
}