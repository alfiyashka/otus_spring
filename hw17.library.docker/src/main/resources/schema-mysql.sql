DROP database IF EXISTS library;
create database library;

use library;
DROP TABLE IF EXISTS author;
CREATE TABLE author(
                     ID BIGINT NOT NULL auto_increment PRIMARY KEY,
                     first_name VARCHAR(255),
                     LAST_NAME VARCHAR(255),
                     EMAIL VARCHAR(255),
                     PHONE_NUMBER VARCHAR(255)
);


DROP TABLE IF EXISTS genre;
CREATE TABLE genre(
                    ID BIGINT NOT NULL auto_increment PRIMARY KEY,
                    GENRE_NAME VARCHAR(255)
);

DROP TABLE IF EXISTS book;
CREATE TABLE book(
                   ISBN BIGINT NOT NULL auto_increment PRIMARY KEY,
                   NAME VARCHAR(255),
                   PUBLISHING_YEAR INTEGER,
                   AUTHOR_ID BIGINT,
                   GENRE_ID BIGINT,
                   foreign key (AUTHOR_ID) references author(ID),
                   foreign key (GENRE_ID) references genre(ID)
);

DROP TABLE IF EXISTS comment;
CREATE TABLE comment(
                      ID BIGINT NOT NULL auto_increment PRIMARY KEY,
                      COMMENT VARCHAR(1000),
                      BOOK_ID BIGINT,
                      foreign key (BOOK_ID) references book(ISBN)
);

DROP TABLE IF EXISTS user;
CREATE TABLE user(ID BIGINT NOT NULL auto_increment PRIMARY KEY,
                  LOGIN VARCHAR(255),
                  PASSWORD VARCHAR(255)
);
