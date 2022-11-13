package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Set;
import java.util.TreeSet;

@Service
public class FilmService {
    //добавление лайка
    //удаление лайка,
    // вывод 10 наиболее популярных фильмов по количеству лайков.
    // Пусть пока каждый пользователь может поставить лайк фильму только один раз.
    private Set <Film> likesFilms;
    private TreeSet <Long> topFilms;
}
