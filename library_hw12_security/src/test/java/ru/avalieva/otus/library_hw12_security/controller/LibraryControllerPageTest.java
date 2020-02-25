package ru.avalieva.otus.library_hw12_security.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.avalieva.otus.library_hw12_security.security.UserDetailsServiceImpl;
import ru.avalieva.otus.library_hw12_security.service.MessageService;
import ru.avalieva.otus.library_hw12_security.service.UserService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({SpringExtension.class})
@WebMvcTest({LibraryPageController.class})
public class LibraryControllerPageTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private MessageService messageService;

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @Test
    public void getBooksPageTest() throws Exception {
        this.mvc.perform(get("/books"))
                .andExpect(status().isOk());
    }


    @Test
    public void getIndexPageTest() throws Exception {
        this.mvc.perform(get("/"))
                .andExpect(status().isOk());
    }

    @Test
    public void getAddUserPageTest() throws Exception {
        this.mvc.perform(get("/newuser"))
                .andExpect(status().isOk());
    }


    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @Test
    public void getAuthorsPageTest() throws Exception {
        this.mvc.perform(get("/authors"))
                .andExpect(status().isOk());
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @Test
    public void geGgenresPageTest() throws Exception {
        this.mvc.perform(get("/genres"))
                .andExpect(status().isOk());
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @Test
    public void getSearchBookPageTest() throws Exception {
        this.mvc.perform(get("/book"))
                .andExpect(status().isOk());
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @Test
    public void getAddBookPageTest() throws Exception {
        this.mvc.perform(get("/book/new"))
                .andExpect(status().isOk());
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @Test
    public void getCommentsPageTest() throws Exception {
        this.mvc.perform(get("/comments/book/1"))
                .andExpect(status().isOk());
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @Test
    public void getNewCommentForBookPageTest() throws Exception {
        this.mvc.perform(get("/add/comment/book/1"))
                .andExpect(status().isOk());
    }

    @WithMockUser(
            username = "admin",
            authorities = {"ROLE_ADMIN"}
    )
    @Test
    public void getEditBookPage() throws Exception {
        this.mvc.perform(get("/book/edit/1"))
                .andExpect(status().isOk());
    }

}
