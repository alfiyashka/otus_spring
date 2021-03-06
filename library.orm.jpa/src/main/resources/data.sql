INSERT INTO GENRE (ID, GENRE_NAME) VALUES (1, 'DRAMA');
INSERT INTO GENRE (ID, GENRE_NAME) VALUES (2, 'SCIENTIFIC');
INSERT INTO GENRE (ID, GENRE_NAME) VALUES (3, 'COMEDY');
INSERT INTO GENRE (ID, GENRE_NAME) VALUES (4, 'TRAGEDY');
INSERT INTO GENRE (ID, GENRE_NAME) VALUES (5, 'FANTASY');
INSERT INTO GENRE (ID, GENRE_NAME) VALUES (6, 'BIOGRATHY');

COMMIT;

INSERT INTO AUTHOR VALUES (1, 'Dmitry', 'Tolstoy', 'dtlstoy@mail.ru', null);
INSERT INTO AUTHOR VALUES (2, 'Alexandra', 'Belova', 'abelova@yandex.ru', '8912232434');
INSERT INTO AUTHOR VALUES (3, 'John', 'Tipping', 'jtibbing@gmail.ru', '23434354');
INSERT INTO AUTHOR VALUES (4, 'Michail', 'Fry', 'michailbook@gmail.ru', '234354565');

COMMIT;

INSERT INTO BOOK VALUES (1, 'Vetra', 1996, 1, 4);
INSERT INTO BOOK VALUES (2, 'Human Anatomy', 2018, 3, 2);
INSERT INTO BOOK VALUES (3, 'My Life', 2013, 4, 6);
INSERT INTO BOOK VALUES (4, 'While the river flows', 2019, 2, 1);
INSERT INTO BOOK VALUES (5, 'Joe''s Adventures', 1996, 1, 5);
INSERT INTO BOOK VALUES (6, 'Vetra 2', 1998, 1, 4);

COMMIT;

INSERT INTO COMMENT VALUES (1, 'Interesting', 1);
INSERT INTO COMMENT VALUES (2, 'Read 3 times', 1);

COMMIT;