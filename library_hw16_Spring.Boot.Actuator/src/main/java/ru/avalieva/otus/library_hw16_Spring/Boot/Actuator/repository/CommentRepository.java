package ru.avalieva.otus.library_hw16_Spring.Boot.Actuator.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.avalieva.otus.library_hw16_Spring.Boot.Actuator.domain.Comment;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@RepositoryRestResource(path = "comment")
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @EntityGraph(value = "CommentWithBook")
    List<Comment> findAll();

    @Query("select c from Comment c JOIN FETCH c.book b where b.isbn = :isbn")
    @EntityGraph(value = "CommentWithBook")
    List<Comment> getCommentsOfBook(@Param("isbn") long isbn);
}