package ru.otus.avalieva.library.orm.jpa.repository.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.avalieva.library.orm.jpa.domain.Genre;

import javax.persistence.PersistenceException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Import(GenreRepositoryJpa.class)
@DataJpaTest
public class GenreRepositoryJpaTest {
    @Autowired
    private GenreRepositoryJpa genreRepository;

    @Autowired
    private TestEntityManager em;

    @DisplayName("get all genres test")
    @Test
    public void allGenresTest() {
        List<Genre> genres = genreRepository.getAll();
        final int EXPECTED_GENRES_COUNT = 6;
        assertThat(genres).isNotNull().hasSize(EXPECTED_GENRES_COUNT);
    }

    @DisplayName("add and delete genre test")
    @Test
    public void addAndDeleteGenreTest() {
        Genre newGenre = new Genre();
        newGenre.setGenreName("NEW_GENRE");
        genreRepository.add(newGenre);
        assertThat(newGenre.getId()).isGreaterThan(0);

        Genre actualGenre = em.find(Genre.class, newGenre.getId());
        assertThat(actualGenre).isNotNull()
                .matches(g -> g.getGenreName() != null && g.getGenreName().equals(newGenre.getGenreName()));
        em.detach(actualGenre);

        genreRepository.delete(newGenre.getId());
        Genre deletedGenre = em.find(Genre.class, newGenre.getId());
        assertThat(deletedGenre).isNull();
    }

    @DisplayName("find genre by id test")
    @Test
    public void findGenreTest() {
        long genreId = 1;
        Genre foundGenre = genreRepository.findById(genreId)
                .orElseThrow(() -> new AssertionFailedError("empty"));
        final String EXPECTED_GENRES_NAME = "DRAMA";

        assertThat(foundGenre).isNotNull()
                .matches(g -> g.getGenreName().equals(EXPECTED_GENRES_NAME));
    }


    @DisplayName("try to delete genre, that is used, test")
    @Test
    public void deleteGenreTestFailedCannotDeletedUsed() {
        long genreId = 1;
        Assertions.assertThrows(PersistenceException.class, () -> { genreRepository.delete(genreId); });
    }

}
