package ru.avalieva.otus.hw14SpringBatch.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.avalieva.otus.hw14SpringBatch.model.jpa.Author;
import ru.avalieva.otus.hw14SpringBatch.model.jpa.Book;
import ru.avalieva.otus.hw14SpringBatch.model.jpa.Comment;
import ru.avalieva.otus.hw14SpringBatch.model.jpa.Genre;
import ru.avalieva.otus.hw14SpringBatch.model.mongo.AuthorMongo;
import ru.avalieva.otus.hw14SpringBatch.model.mongo.BookMongo;
import ru.avalieva.otus.hw14SpringBatch.model.mongo.CommentMongo;
import ru.avalieva.otus.hw14SpringBatch.model.mongo.GenreMongo;
import ru.avalieva.otus.hw14SpringBatch.service.LibraryConverterService;

import javax.persistence.EntityManagerFactory;

@Configuration
@Import(MongoDBConfig.class)
public class JobConfig {
    private static final int CHUNK_SIZE = 5;
    private final Logger logger = LoggerFactory.getLogger("Batch");

    public static final String IMPORT_LIBRARY_JOB_NAME = "importLibraryJob";

    private final EntityManagerFactory entityManagerFactory;

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    private final MongoTemplate mongoTemplate;

    private final LibraryConverterService libraryConverterService;


    JobConfig(EntityManagerFactory entityManagerFactory,
              JobBuilderFactory jobBuilderFactory,
              StepBuilderFactory stepBuilderFactory,
              MongoTemplate mongoTemplate,
              LibraryConverterService libraryConverterService) {
        this.entityManagerFactory = entityManagerFactory;
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.mongoTemplate = mongoTemplate;
        this.libraryConverterService = libraryConverterService;
    }

    @StepScope
    @Bean
    public JpaPagingItemReader<Book> bookReader() throws Exception{
        String bookQuery = "SELECT b from Book b";
        var bookReader = new JpaPagingItemReader<Book>();
        bookReader.setEntityManagerFactory(entityManagerFactory);
        bookReader.setQueryString(bookQuery);
        bookReader.setPageSize(5);
        bookReader.afterPropertiesSet();
        return bookReader;
     }

    @StepScope
    @Bean
    public JpaPagingItemReader<Author> authorReader() throws Exception{
        String authorQuery = "SELECT a from Author a";
        var authorReader = new JpaPagingItemReader<Author>();
        authorReader.setEntityManagerFactory(entityManagerFactory);
        authorReader.setQueryString(authorQuery);
        authorReader.setPageSize(5);
        authorReader.afterPropertiesSet();
        return authorReader;
    }

    @StepScope
    @Bean
    public JpaPagingItemReader<Genre> genreReader() throws Exception{
        String genreQuery = "SELECT g from Genre g";
        var genreReader = new JpaPagingItemReader<Genre>();
        genreReader.setEntityManagerFactory(entityManagerFactory);
        genreReader.setQueryString(genreQuery);
        genreReader.setPageSize(5);
        genreReader.afterPropertiesSet();
        return genreReader;
    }

    @StepScope
    @Bean
    public JpaPagingItemReader<Comment> commentReader() throws Exception{
        String commentQuery = "SELECT c from Comment c";
        var commentReader = new JpaPagingItemReader<Comment>();
        commentReader.setEntityManagerFactory(entityManagerFactory);
        commentReader.setQueryString(commentQuery);
        commentReader.setPageSize(5);
        commentReader.afterPropertiesSet();
        return commentReader;
    }

    @Bean
    public ItemProcessor<Author, AuthorMongo> authorProcessor() {
        return libraryConverterService::convertToMongoAuthor;
    }

    @Bean
    public ItemProcessor<Genre, GenreMongo> genreProcessor() {
        return libraryConverterService::convertToMongoGenre;
    }

    @Bean
    public ItemProcessor<Book, BookMongo> bookProcessor() {
        return libraryConverterService::convertToMongoBook;
    }

    @Bean
    public ItemProcessor<Comment, CommentMongo> commentProcessor() {
        return libraryConverterService::convertToMongoComment;
    }

    @StepScope
    @Bean
    public MongoItemWriter<BookMongo> bookWriter() {
        return new MongoItemWriterBuilder<BookMongo>()
                .collection("books")
                .template(mongoTemplate)
                .build();
    }

    @StepScope
    @Bean
    public MongoItemWriter<AuthorMongo> authorWriter() {
        return new MongoItemWriterBuilder<AuthorMongo>()
                .collection("authors")
                .template(mongoTemplate)
                .build();
    }

    @StepScope
    @Bean
    public MongoItemWriter<GenreMongo> genreWriter() {
        return new MongoItemWriterBuilder<GenreMongo>()
                .collection("genres")
                .template(mongoTemplate)
                .build();
    }


    @StepScope
    @Bean
    public MongoItemWriter<CommentMongo> commentWriter() {
        return new MongoItemWriterBuilder<CommentMongo>()
                .collection("comments")
                .template(mongoTemplate)
                .build();
    }

    @Bean
    public Step stepAuthorMigration() throws Exception {
        return stepBuilderFactory.get("stepAuthor")
                .<Author, AuthorMongo>chunk(CHUNK_SIZE)
                .reader(authorReader())
                .processor(authorProcessor())
                .writer(authorWriter())
                .build();
    }

    @Bean
    public Step stepGenreMigration() throws Exception {
        return stepBuilderFactory.get("stepGenre")
                .<Genre, GenreMongo>chunk(CHUNK_SIZE)
                .reader(genreReader())
                .processor(genreProcessor())
                .writer(genreWriter())
                .build();
    }

    @Bean
    public Step stepBookMigration() throws Exception {
        return stepBuilderFactory.get("stepBook")
                .<Book, BookMongo>chunk(CHUNK_SIZE)
                .reader(bookReader())
                .processor(bookProcessor())
                .writer(bookWriter())
                .build();
    }

    @Bean
    public Step stepCommentMigration() throws Exception {
        return stepBuilderFactory.get("stepComment")
                .<Comment, CommentMongo>chunk(CHUNK_SIZE)
                .reader(commentReader())
                .processor(commentProcessor())
                .writer(commentWriter())
                .build();
    }

    @Bean
    public Job importLibraryJob() throws Exception {
        return jobBuilderFactory.get("importLibraryJob")
                .incrementer(new RunIdIncrementer())
                .start(stepGenreMigration())
                .next(stepAuthorMigration())
                .next(stepBookMigration())
                .next(stepCommentMigration())
                .listener(new JobExecutionListener() {
                    @Override
                    public void beforeJob(JobExecution jobExecution) {
                        logger.info("Начало job");
                    }

                    @Override
                    public void afterJob(JobExecution jobExecution) {
                        logger.info("Конец job");
                    }
                })
                .build();
    }
}

