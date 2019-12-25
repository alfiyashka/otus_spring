package com.otus.avalieva.library.dao.impl;

import com.otus.avalieva.library.domain.Genre;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import java.util.List;

@Import(GenreDaoJdbc.class)
@JdbcTest
public class GenreDaoJdbcTest {
    @Autowired
    private GenreDaoJdbc genreDao;

    @DisplayName("get all genres test")
    @Test
    public void allGenresTest() {
        List<Genre> genres = genreDao.allGenres();
        Assertions.assertEquals(6, genres.size());
    }

    @DisplayName("add and delete genre test")
    @Test
    public void addAndDeleteGenreTest() {
        Genre newGenre = new Genre();
        newGenre.setGenreName("NEW_GENRE");
        genreDao.addGenre(newGenre);
        Assertions.assertEquals(7, genreDao.allGenres().size());

        Genre findByName = genreDao.findGenreByName(newGenre.getGenreName());

        genreDao.deleteGenre(findByName.getId());
        Assertions.assertEquals(6, genreDao.allGenres().size());

    }

    @DisplayName("find genre by id test")
    @Test
    public void findGenreTest() {
        long genreId = 1;
        Genre foundGenre = genreDao.findGenreById(genreId);
        Assertions.assertEquals("DRAMA", foundGenre.getGenreName());
    }


    @DisplayName("try to delete genre, that is used, test")
    @Test
    public void deleteGenreTestFailedCannotDeletedUsed() {
        long genreId = 1;
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> { genreDao.deleteGenre(genreId); });
    }
}
