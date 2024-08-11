package in.praveen.kt.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.praveen.kt.model.Book;
import in.praveen.kt.service.BookService;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BookController {
    private static final Logger log = LoggerFactory.getLogger(BookController.class);
    @Autowired
    private BookService bookService;
@SneakyThrows
    @PostMapping("/addBook")
    public Book addBook(@RequestBody Book book){
        log.info("BOOK CONTROLLER ADD BOOK REQUEST {}",new ObjectMapper().writeValueAsString(book));
        return bookService.addbook(book);
    }
    @SneakyThrows
    @GetMapping ("/getBooks")
    public List<Book> getBooks(){
        List<Book> books = bookService.getAllBooks();
        log.info("BOOK CONTROLLER GET BOOK RESPONSE {}",new ObjectMapper().writeValueAsString(books));
        return books;
    }

}
