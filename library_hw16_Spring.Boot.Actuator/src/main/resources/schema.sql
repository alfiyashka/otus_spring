DROP TABLE IF EXISTS AUTHOR;
CREATE TABLE AUTHOR(
                     ID BIGINT NOT NULL auto_increment PRIMARY KEY,
                     first_name VARCHAR(255),
                     LAST_NAME VARCHAR(255),
                     EMAIL VARCHAR(255),
                     PHONE_NUMBER VARCHAR(255)
);


DROP TABLE IF EXISTS GENRE;
CREATE TABLE GENRE(
                    ID BIGINT NOT NULL auto_increment PRIMARY KEY,
                    GENRE_NAME VARCHAR(255)
);

DROP TABLE IF EXISTS BOOK;
CREATE TABLE BOOK(
                   ISBN BIGINT NOT NULL auto_increment PRIMARY KEY,
                   NAME VARCHAR(255),
                   PUBLISHING_YEAR INTEGER,
                   AUTHOR_ID BIGINT NOT NULL,
                   GENRE_ID BIGINT NOT NULL,
                   foreign key (AUTHOR_ID) references AUTHOR(ID),
                   foreign key (GENRE_ID) references GENRE(ID)
);

DROP TABLE IF EXISTS COMMENT;
CREATE TABLE COMMENT(
                      ID BIGINT NOT NULL auto_increment PRIMARY KEY,
                      COMMENT VARCHAR(1000),
                      BOOK_ID BIGINT,
                      foreign key (BOOK_ID) references BOOK(ISBN)
);

DROP TABLE IF EXISTS USER;
CREATE TABLE USER(ID BIGINT NOT NULL auto_increment PRIMARY KEY,
                  LOGIN VARCHAR(255),
                  PASSWORD VARCHAR(255)
);
