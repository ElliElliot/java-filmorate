package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.model.Genre;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreStorage genreStorage;

    public Collection<Genre> getGenres() {
        return genreStorage.getGenres();
    }

    public Genre getGenreById(int id) {
        return genreStorage.getById(id);
    }
}