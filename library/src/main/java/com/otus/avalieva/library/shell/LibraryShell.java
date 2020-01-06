package com.otus.avalieva.library.shell;
import com.otus.avalieva.library.domain.Author;
import com.otus.avalieva.library.domain.Book;
import com.otus.avalieva.library.domain.Genre;
import com.otus.avalieva.library.service.LibraryService;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;

@ShellComponent
public class LibraryShell {
    private LoginType loginType = LoginType.USER;

    private final LibraryService libraryService;

    public LibraryShell(LibraryService service) {
        this.libraryService = service;
    }

    @ShellMethod(key = "as_root", value = "as root user")
    public void asroot() {
        loginType = LoginType.ROOT;
    }

    @ShellMethod(key = "as_user", value = "as user")
    public void asuser() {
        loginType = LoginType.USER;
    }

    @ShellMethod(key = "genres", value = "genres list")
    public void genresList() {
        libraryService.printAllGenres();
    }

    @ShellMethodAvailability("rootAvailability")
    @ShellMethod(key = "new_g", value = "add new genre")
    public void addNewGenre(@ShellOption({"genrename", "n"}) String genreName) {
        Genre genre = new Genre(null, genreName);
        libraryService.addNewGenre(genre);
    }

    @ShellMethodAvailability("rootAvailability")
    @ShellMethod(key = "delete_g", value = "delete genre")
    public void deleteGenre(@ShellOption({"id", "id"}) long id) {
        libraryService.deleteGenre(id);
    }

    @ShellMethod(key = "books", value = "books list")
    public void booksList() {
        libraryService.printAllBooks();
    }

    @ShellMethod(key = "book_id", value = "find book by isbn")
    public void bookByISBN(@ShellOption({"isbn", "id"}) long isbn) {
        libraryService.findBookByISBNAndPrint(isbn);
    }

    @ShellMethod(key = "book_name", value = "find book by name")
    public void bookByName(@ShellOption({"name", "n"}) String name) {
        libraryService.findBookByNameAndPrint(name);
    }

    @ShellMethod(key = "book_author", value = "find book by author")
    public void bookByAuthor(@ShellOption({"firstname", "fn"}) String firstName,
                             @ShellOption({"lastname", "ln"}) String lastName) {
        libraryService.findBookByAuthorAndPrint(firstName, lastName);
    }

    @ShellMethodAvailability("rootAvailability")
    @ShellMethod(key = "new_b", value = "add new book")
    public void addNewBook(@ShellOption({"isbn", "id"}) Long isbn,
                           @ShellOption({"name", "n"}) String name,
                           @ShellOption({"publish", "p"}) int publishingYear,
                           @ShellOption({"author_id", "ai"}) long authorId,
                           @ShellOption({"genre_id", "gi"}) long genreId) {
        Book book = new Book(isbn, name, publishingYear, authorId, genreId);
        libraryService.addNewBook(book);
    }

    @ShellMethodAvailability("rootAvailability")
    @ShellMethod(key = "delete_b", value = "delete book")
    public void deleteBook(@ShellOption({"isbn", "id"}) int isbn) {
        libraryService.deleteBook(isbn);
    }

    @ShellMethod(key = "authors", value = "authors list")
    public void authorsList() {
        libraryService.printAllAuthors();
    }


    @ShellMethodAvailability("rootAvailability")
    @ShellMethod(key = "new_a", value = "add new author")
    public void addNewAuthor(@ShellOption({"firstname", "fn"}) String firstName,
                             @ShellOption({"lastname", "ln"}) String lastName,
                             @ShellOption({"phone", "ph"}) String phone,
                             @ShellOption({"email", "e"}) String email) {
        Author author = new Author(null, firstName, lastName, phone, email);
        libraryService.addNewAuthor(author);
    }


   @ShellMethodAvailability("rootAvailability")
   @ShellMethod(key = "delete_a", value = "delete author")
   public void deleteAuthor(@ShellOption({"id", "id"}) long id) {
       libraryService.deleteAuthor(id);
   }

    public Availability rootAvailability() {
        return loginType == LoginType.ROOT
                ? Availability.available()
                : Availability.unavailable("you have not permissions");
    }

}
