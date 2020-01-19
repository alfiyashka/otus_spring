package ru.avalieva.otus.library_hw07.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.avalieva.otus.library_hw07.domain.Book;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface BookRepository extends JpaRepository<Book, Long> {
    @EntityGraph(value = "BookWithGenreAndAuthor")
    List<Book> findAll();

    @EntityGraph(value = "BookWithGenreAndAuthor")
    List<Book> findByNameLike(String name);

    @Query("select b from Book b JOIN FETCH b.genre g where g.genreName = :genre")
    @EntityGraph(value = "BookWithGenreAndAuthor")
    List<Book> findByGenre(@Param("genre") String genre);

    @Query("select b from Book b JOIN FETCH b.author a where a.firstName = :first_name and a.lastName = :last_name")
    @EntityGraph(value = "BookWithGenreAndAuthor")
    List<Book> findByAuthor(@Param("first_name") String firstName, @Param("last_name") String lastName);
}
