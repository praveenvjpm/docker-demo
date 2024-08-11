package in.praveen.kt.service;

import in.praveen.kt.model.Book;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {
    List<Book> books = new ArrayList<>();
    public Book addbook(Book book){
       books.add(book);
       return book;
    }

    public List<Book> getAllBooks() {
        return books;
    }
}
