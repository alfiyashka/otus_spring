use library;

INSERT INTO genre (ID, GENRE_NAME) VALUES (1, 'DRAMA');
INSERT INTO genre (ID, GENRE_NAME) VALUES (2, 'SCIENTIFIC');
INSERT INTO genre (ID, GENRE_NAME) VALUES (3, 'COMEDY');
INSERT INTO genre (ID, GENRE_NAME) VALUES (4, 'TRAGEDY');
INSERT INTO genre (ID, GENRE_NAME) VALUES (5, 'FANTASY');
INSERT INTO genre (ID, GENRE_NAME) VALUES (6, 'BIOGRATHY');

COMMIT;

INSERT INTO author VALUES (1, 'Dmitry', 'Tolstoy', 'dtlstoy@mail.ru', null);
INSERT INTO author VALUES (2, 'Alexandra', 'Belova', 'abelova@yandex.ru', '8912232434');
INSERT INTO author VALUES (3, 'John', 'Tipping', 'jtibbing@gmail.ru', '23434354');
INSERT INTO author VALUES (4, 'Michail', 'Fry', 'michailbook@gmail.ru', '234354565');

COMMIT;

INSERT INTO book VALUES (1, 'Vetra', 1996, 1, 4);
INSERT INTO book VALUES (2, 'Human Anatomy', 2018, 3, 2);
INSERT INTO book VALUES (3, 'My Life', 2013, 4, 6);
INSERT INTO book VALUES (4, 'While the river flows', 2019, 2, 1);
INSERT INTO book VALUES (5, 'Joe''s Adventures', 1996, 1, 5);
INSERT INTO book VALUES (6, 'Vetra 2', 1998, 1, 4);

COMMIT;

INSERT INTO comment VALUES (1, 'Interesting', 1);
INSERT INTO comment VALUES (2, 'Read 3 times', 1);

COMMIT;

INSERT INTO user VALUES (1, 'admin', '$2a$10$46EiMLeacFJq9dj.O5oDzu/CTCtmplo/5.QKQE5spiA9IIerfs72i'); -- password
INSERT INTO user VALUES (2, 'alfiya', '$2a$10$hYrTym98OrAfW1N91.7NN.NasRvNH6U1Ly2GmcQVMEPNMiY9107la'); -- mypassword

COMMIT;