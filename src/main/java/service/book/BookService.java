package service.book;

import model.Book;
import model.User;

import java.util.List;

public interface BookService {
    List<Book> findAll();
    Book findById(Long id);
    int save(Book book);
    boolean sell(Book book, User user);
    boolean delete(Book book);
    int getAgeOfBook(Long id);
}
