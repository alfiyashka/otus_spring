package ru.avalieva.otus.library_hw09_mvc.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.avalieva.otus.library_hw09_mvc.service.impl.LibraryException;

import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class LibraryControllerAdvice {

    @ExceptionHandler(LibraryException.class)
    public String handleLibraryException(LibraryException e, Model model, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        model.addAttribute("message", e.getMessage());
        return "error";
    }
}
