package ru.otus.avalieva.library.orm.jpa.repository.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import ru.otus.avalieva.library.orm.jpa.domain.Genre;

import javax.persistence.PersistenceException;
import java.util.List;

@Import(GenreRepositoryJpa.class)
@DataJpaTest
public class GenreRepositoryJpaTest {
    @Autowired
    private GenreRepositoryJpa genreRepository;

    @DisplayName("get all genres test")
    @Test
    public void allGenresTest() {
        List<Genre> genres = genreRepository.getAll();
        final int EXPECTED_GENRES_COUNT = 6;
        Assertions.assertEquals(EXPECTED_GENRES_COUNT, genres.size());
    }

    @DisplayName("add and delete genre test")
    @Test
    public void addAndDeleteGenreTest() {
        Genre newGenre = new Genre();
        newGenre.setGenreName("NEW_GENRE");
        genreRepository.add(newGenre);
        final int EXPECTED_GENRES_COUNT_AFTER_ADD = 7;
        Assertions.assertEquals(EXPECTED_GENRES_COUNT_AFTER_ADD, genreRepository.getAll().size());

        Genre findByName = genreRepository.findByName(newGenre.getGenreName())
                .orElseThrow(() -> new AssertionFailedError("empty"));

        genreRepository.delete(findByName.getId());
        final int EXPECTED_GENRES_COUNT_AFTER_DELETE = 6;
        Assertions.assertEquals(EXPECTED_GENRES_COUNT_AFTER_DELETE, genreRepository.getAll().size());

    }

    @DisplayName("find genre by id test")
    @Test
    public void findGenreTest() {
        long genreId = 1;
        Genre foundGenre = genreRepository.findById(genreId)
                .orElseThrow(() -> new AssertionFailedError("empty"));
        final String EXPECTED_GENRES_NAME = "DRAMA";
        Assertions.assertEquals(EXPECTED_GENRES_NAME, foundGenre.getGenreName());
    }


    @DisplayName("try to delete genre, that is used, test")
    @Test
    public void deleteGenreTestFailedCannotDeletedUsed() {
        long genreId = 1;
        Assertions.assertThrows(PersistenceException.class, () -> { genreRepository.delete(genreId); });
    }

}
