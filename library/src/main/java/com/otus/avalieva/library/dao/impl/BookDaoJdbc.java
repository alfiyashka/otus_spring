package com.otus.avalieva.library.dao.impl;

import com.otus.avalieva.library.dao.BookDao;
import com.otus.avalieva.library.domain.Author;
import com.otus.avalieva.library.domain.Book;
import com.otus.avalieva.library.domain.Genre;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class BookDaoJdbc implements BookDao {
    private final NamedParameterJdbcOperations jdbcOperations;

    public BookDaoJdbc(NamedParameterJdbcOperations namedParameterJdbcOperations) {
        this.jdbcOperations = namedParameterJdbcOperations;
    }

    @Override
    public List<Book> allBooks() {
        return jdbcOperations.query(
                "select * from book b join author a on b.author_id = a.id join genre g on b.genre_id = g.id",
                new BookMapper());
    }

    @Override
    public void addBook(Book book) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("isbn", book.getIsbn());
        paramMap.put("name", book.getName());
        paramMap.put("publishYear", book.getPublishingYear());
        paramMap.put("authorId", book.getAuthor().getId());
        paramMap.put("genreId", book.getGenre().getId());
        jdbcOperations.update("insert into book values (:isbn, :name, :publishYear, :authorId, :genreId)", paramMap);
    }

    @Override
    public Book findBookByIsbn(long isbn) {
        Map<String, Object> params = Collections.singletonMap("isbn", isbn);
        return jdbcOperations.queryForObject(
                "select * from book b join author a on b.author_id = a.id join genre g on b.genre_id = g.id where b.isbn = :isbn",
                params, new BookMapper()
        );
    }

    @Override
    public List<Book> findBookByName(String name) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);
        return jdbcOperations.query(
                "select * from book b join author a on b.author_id = a.id join genre g on b.genre_id = g.id where b.name like :name",
                paramMap, new BookMapper()
        );
    }

    @Override
    public List<Book> findBookByGenre(String genre) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("genre", genre);
        return jdbcOperations.query(
                "select * from book b join author a on b.author_id = a.id join genre g on b.genre_id = g.id where g.genre_name = :genre",
                paramMap, new BookMapper()
        );
    }

    @Override
    public List<Book> findBookByAuthor(String firstName, String lastName) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("firstname", firstName);
        paramMap.put("lastname", lastName);
        return jdbcOperations.query(
                "select * from book b join author a on b.author_id = a.id join genre g on b.genre_id = g.id where a.firstname = :firstname and a.lastname = :lastname",
                paramMap, new BookMapper()
        );
    }

    @Override
    public void deleteBookById(long isbn) {
        Map<String, Object> params = Collections.singletonMap("isbn", isbn);
        jdbcOperations.update(
                "delete from book where isbn = :isbn", params
        );
    }



    private static class BookMapper implements RowMapper<Book> {
        @Override
        public Book mapRow(ResultSet resultSet, int i) throws SQLException {
            long isbn = resultSet.getLong("isbn");
            String name = resultSet.getString("name");
            int publishYear = resultSet.getInt("publishing_year");

            long genreId = resultSet.getLong("id");
            String genreValue = resultSet.getString("genre_name");
            Genre genre = new Genre(genreId, genreValue);

            long authorId = resultSet.getLong("id");
            String firstName = resultSet.getString("firstname");
            String lastName = resultSet.getString("lastname");
            String email = resultSet.getString("email");
            String phone = resultSet.getString("phone");

            Author author = new Author(authorId, firstName, lastName, email, phone);
            return new Book(isbn, name, publishYear, author, genre);
        }
    }
}
