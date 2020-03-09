package ru.avalieva.otus.library_hw13_security.controller;


import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import ru.avalieva.otus.library_hw13_security.domain.User;
import ru.avalieva.otus.library_hw13_security.security.LibraryUserDetails;
import ru.avalieva.otus.library_hw13_security.security.UserDetailsServiceImpl;
import ru.avalieva.otus.library_hw13_security.security.UserRole;
import ru.avalieva.otus.library_hw13_security.service.MessageService;
import ru.avalieva.otus.library_hw13_security.service.UserService;

import java.util.stream.Stream;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith({SpringExtension.class})
@WebMvcTest({LibraryPageController.class, UserDetailsServiceImpl.class})
public class LibraryControllerPageTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private MessageService messageService;

    private static Stream<Arguments> getArguments() {
        return Stream.of(
                Arguments.of("/books",
                        new LibraryUserDetails(new User(0,"admin", "password"),
                                UserRole.ADMIN)),
                Arguments.of("/books",
                        new LibraryUserDetails(new User(0,"user1", "password"),
                                UserRole.USER)),
                Arguments.of("/authors",
                        new LibraryUserDetails(new User(0,"admin", "password"),
                                UserRole.ADMIN)),
                Arguments.of("/authors",
                        new LibraryUserDetails(new User(0,"user1", "password"),
                                UserRole.USER)),
                Arguments.of("/genres",
                        new LibraryUserDetails(new User(0,"admin", "password"),
                                UserRole.ADMIN)),
                Arguments.of("/genres",
                        new LibraryUserDetails(new User(0,"user1", "password"),
                                UserRole.USER)),
                Arguments.of("/book",
                        new LibraryUserDetails(new User(0,"admin", "password"),
                                UserRole.ADMIN)),
                Arguments.of("/book",
                        new LibraryUserDetails(new User(0,"user", "password"),
                                UserRole.USER)),
                Arguments.of("/book/new",
                        new LibraryUserDetails(new User(0,"admin", "password"),
                                UserRole.ADMIN)),
                Arguments.of("/comments/book/1",
                        new LibraryUserDetails(new User(0,"admin", "password"),
                                UserRole.ADMIN)),
                Arguments.of("/comments/book/1",
                        new LibraryUserDetails(new User(0,"user", "password"),
                                UserRole.USER)),
                Arguments.of("/add/comment/book/1",
                        new LibraryUserDetails(new User(0,"admin", "password"),
                                UserRole.ADMIN)),
                Arguments.of("/add/comment/book/1",
                        new LibraryUserDetails(new User(0,"user", "password"),
                                UserRole.USER)),
                Arguments.of("/book/edit/1",
                        new LibraryUserDetails(new User(0,"admin", "password"),
                                UserRole.ADMIN)),
                Arguments.of("/",null),
                Arguments.of("/newuser", null)
        );
    }

    @ParameterizedTest
    @MethodSource("getArguments")
    public void pageTestSuccess (String url, UserDetails user) throws Exception {
        MockHttpServletRequestBuilder getRequest = get(url);
        if (user != null) {
            getRequest.with(user(user));
        }
        this.mvc.perform(getRequest)
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @ValueSource(strings = {"/books", "/authors", "/genres", "/book", "/book/new",
            "/comments/book/1", "/add/comment/book/1", "/book/edit/1"})
    public void pageTestFailNotAllowed (String url) throws Exception {
        this.mvc.perform(get(url))
                .andExpect(status().isFound());
    }


    @ParameterizedTest
    @ValueSource(strings = {"/book/new", "/book/edit/1"})
    public void pageTestNotAllowedForUser (String url) throws Exception {
        LibraryUserDetails user = new LibraryUserDetails(new User(0,"user", "password"),
                UserRole.USER);
        this.mvc.perform(get(url).with(user(user)))
                .andExpect(status().isForbidden());
    }

}
