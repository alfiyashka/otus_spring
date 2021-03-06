package ru.avalieva.otus.hw14SpringBatch.model.jpa;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
@NamedEntityGraph(name = "CommentWithBook",
        attributeNodes = {
                @NamedAttributeNode(value = "book", subgraph = "bookGraph")
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "bookGraph",
                        attributeNodes = {
                                @NamedAttributeNode(value = "author"),
                                @NamedAttributeNode(value = "genre")
                        }
                )
        })
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "comment")
    private String comment;

    @ManyToOne
    @JoinColumn(name = "book_id", referencedColumnName = "isbn", nullable=false)
    private Book book;
}