package ru.avalieva.com.library_hw11_webflux.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

@ExtendWith({SpringExtension.class})
@SpringBootTest
@AutoConfigureWebTestClient
public class LibraryPageControllerTest {

    @Autowired
    private WebTestClient client;

    @Test
    public void getBooksPageTest() {

        client.get()
                .uri("/books/")
                .accept(MediaType.TEXT_HTML)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    public void getAuthorsPageTest() {
        client.get()
                .uri("/authors")
                .accept(MediaType.TEXT_HTML)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    public void geGenresPageTest() {
        client.get()
                .uri("/genres")
                .accept(MediaType.TEXT_HTML)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    public void getSearchBookPageTest() {
        client.get()
                .uri("/book")
                .accept(MediaType.TEXT_HTML)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    public void getAddBookPageTest() throws Exception {
        client.get()
                .uri("/book/new")
                .accept(MediaType.TEXT_HTML)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    public void getCommentsPageTest() {
        client.get()
                .uri("/comments/book/1")
                .accept(MediaType.TEXT_HTML)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    public void getNewCommentForBookPageTest() {
        client.get()
                .uri("/add/comment/book/1")
                .accept(MediaType.TEXT_HTML)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    public void getEditBookPage()  {
        client.get()
                .uri("/book/edit/1")
                .accept(MediaType.TEXT_HTML)
                .exchange()
                .expectStatus()
                .isOk();
    }
}
