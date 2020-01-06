package com.otus.avalieva.library.dao.impl;

import com.otus.avalieva.library.dao.GenreDao;
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
public class GenreDaoJdbc implements GenreDao {
    private final NamedParameterJdbcOperations jdbcOperations;

    public GenreDaoJdbc(NamedParameterJdbcOperations namedParameterJdbcOperations) {
        this.jdbcOperations = namedParameterJdbcOperations;
    }


    @Override
    public List<Genre> allGenres() {
        return jdbcOperations.query("select * from genre", new GenreMapper());
    }

    @Override
    public void addGenre(Genre genre) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("genre", genre.getGenreName());
        jdbcOperations.update("insert into genre (genre_name) values (:genre)", paramMap);
    }

    @Override
    public Genre findGenreById(long id) {
        Map<String, Object> params = Collections.singletonMap("id", (int) id);
        return jdbcOperations.queryForObject(
                "select * from genre where id = :id", params, new GenreMapper()
        );
    }

    @Override
    public void deleteGenre(long id) {
        Map<String, Object> params = Collections.singletonMap("id", id);
        jdbcOperations.update(
                "delete from genre where id = :id", params
        );
    }

    @Override
    public Genre findGenreByName(String name) {
        Map<String, Object> params = Collections.singletonMap("genre_name", name);
        return jdbcOperations.queryForObject(
                "select * from genre where genre_name = :genre_name", params, new GenreMapper()
        );
    }

    private static class GenreMapper implements RowMapper<Genre> {

        @Override
        public Genre mapRow(ResultSet resultSet, int i) throws SQLException {
            long id = resultSet.getLong("id");
            String genreValue = resultSet.getString("genre_name");
            return new Genre(id, genreValue);
        }
    }
}
