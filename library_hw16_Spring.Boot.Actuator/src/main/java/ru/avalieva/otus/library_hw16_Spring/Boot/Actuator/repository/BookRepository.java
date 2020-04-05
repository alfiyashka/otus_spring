package ru.avalieva.otus.library_hw16_Spring.Boot.Actuator.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.avalieva.otus.library_hw16_Spring.Boot.Actuator.domain.Book;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
@RepositoryRestResource(path = "book")
public interface BookRepository extends PagingAndSortingRepository<Book, Long> {

    @EntityGraph(value = "BookWithGenreAndAuthor")
    List<Book> findAll();

    @EntityGraph(value = "BookWithGenreAndAuthor")
    List<Book> findByNameLike(@Param("name") String name);

    @Query("select b from Book b JOIN FETCH b.genre g where g.genreName = :genre")
    @EntityGraph(value = "BookWithGenreAndAuthor")
    List<Book> findByGenre(@Param("genre") String genre);

    @Query("select b from Book b JOIN FETCH b.author a where a.firstName = :first_name and a.lastName = :last_name")
    @EntityGraph(value = "BookWithGenreAndAuthor")
    List<Book> findByAuthor(@Param("first_name") String firstName, @Param("last_name") String lastName);

    @EntityGraph(value = "BookWithGenreAndAuthor")
    Optional<Book> findById(@Param("isbn")long isbn);


}

