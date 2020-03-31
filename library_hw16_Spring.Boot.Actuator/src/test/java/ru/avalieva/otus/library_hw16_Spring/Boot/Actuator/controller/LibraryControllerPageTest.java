package ru.avalieva.otus.library_hw16_Spring.Boot.Actuator.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.avalieva.otus.library_hw16_Spring.Boot.Actuator.service.LibraryService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({SpringExtension.class})
@WebMvcTest({LibraryPageController.class})
public class LibraryControllerPageTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private LibraryService libraryService;

    @Test
    public void getBooksPageTest() throws Exception {
        this.mvc.perform(get("/books"))
                .andExpect(status().isOk());
    }

    @Test
    public void getAuthorsPageTest() throws Exception {
        this.mvc.perform(get("/authors"))
                .andExpect(status().isOk());
    }

    @Test
    public void geGgenresPageTest() throws Exception {
        this.mvc.perform(get("/genres"))
                .andExpect(status().isOk());
    }

    @Test
    public void getSearchBookPageTest() throws Exception {
        this.mvc.perform(get("/findbook"))
                .andExpect(status().isOk());
    }

    @Test
    public void getAddBookPageTest() throws Exception {
        this.mvc.perform(get("/book/new"))
                .andExpect(status().isOk());
    }

    @Test
    public void getCommentsPageTest() throws Exception {
        this.mvc.perform(get("/comments/book/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void getNewCommentForBookPageTest() throws Exception {
        this.mvc.perform(get("/add/comment/book/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void getEditBookPage() throws Exception {
        this.mvc.perform(get("/books/edit/1"))
                .andExpect(status().isOk());
    }

}