MERGE INTO mpa (MPA_ID, NAME) VALUES (1, 'G');
MERGE INTO mpa (MPA_ID, NAME) VALUES (2, 'PG');
MERGE INTO mpa (MPA_ID, NAME) VALUES (3, 'PG-13');
MERGE INTO mpa (MPA_ID, NAME) VALUES (4, 'R');
MERGE INTO mpa (MPA_ID, NAME) VALUES (5, 'NC-17');

MERGE INTO GENRE (genre_id, name) VALUES (1, 'Комедия');
MERGE INTO GENRE (genre_id, name) VALUES (2, 'Драма');
MERGE INTO GENRE (genre_id, name) VALUES (3, 'Мультфильм');
MERGE INTO GENRE (genre_id, name) VALUES (4, 'Триллер');
MERGE INTO GENRE (genre_id, name) VALUES (5, 'Документальный');
MERGE INTO GENRE (genre_id, name) VALUES (6, 'Боевик');
