package com.otus.avalieva.library.dao.impl;

import com.otus.avalieva.library.dao.AuthorDao;
import com.otus.avalieva.library.domain.Author;
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
public class AuthorDaoJdbc implements AuthorDao {
    private final NamedParameterJdbcOperations jdbcOperations;

    public AuthorDaoJdbc(NamedParameterJdbcOperations namedParameterJdbcOperations) {
        this.jdbcOperations = namedParameterJdbcOperations;
    }

    @Override
    public List<Author> allAuthors() {
        return jdbcOperations.query("select * from author", new AuthorMapper());
    }

    @Override
    public void addAuthor(Author author) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("firstname", author.getFirstName());
        paramMap.put("lastname", author.getLastName());
        paramMap.put("email", author.getEmail());
        paramMap.put("phone", author.getPhoneNumber());
        jdbcOperations.update("insert into author (firstname, lastname, email, phone) values (:firstname, :lastname, :email, :phone)", paramMap);
    }

    @Override
    public Author findAuthorById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        return jdbcOperations.queryForObject(
                "select * from author where id = :id", params, new AuthorMapper()
        );
    }

    @Override
    public void deleteAuthorById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        jdbcOperations.update(
                "delete from author where id = :id", params
        );
    }

    @Override
    public Author findAuthorByName(String firstName, String lastName) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("firstname", firstName);
        paramMap.put("lastname", lastName);
        return jdbcOperations.queryForObject(
                "select * from author where firstname = :firstname and lastname = :lastname", paramMap, new AuthorMapper()
        );
    }

    private static class AuthorMapper implements RowMapper<Author> {

        @Override
        public Author mapRow(ResultSet resultSet, int i) throws SQLException {
            long id = resultSet.getLong("id");
            String firstName = resultSet.getString("firstname");
            String lastName = resultSet.getString("lastname");
            String email = resultSet.getString("email");
            String phone = resultSet.getString("phone");
            return new Author(id, firstName, lastName, email, phone);
        }
    }
}
