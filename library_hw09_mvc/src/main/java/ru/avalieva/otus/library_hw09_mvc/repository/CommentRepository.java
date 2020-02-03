package ru.avalieva.otus.library_hw09_mvc.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.avalieva.otus.library_hw09_mvc.domain.Comment;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @EntityGraph(value = "CommentWithBook")
    List<Comment> findAll();

    @Query("select c from Comment c JOIN FETCH c.book b where b.isbn = :id")
    @EntityGraph(value = "CommentWithBook")
    List<Comment> getCommentsOfBook(@Param("id") long isbn);
}
