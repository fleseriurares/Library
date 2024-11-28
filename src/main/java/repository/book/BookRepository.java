package repository.book;

import model.Book;
import model.User;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    List<Book> findAll(); //returneaza lista de carti
    Optional<Book> findById(Long id);
    int save(Book book);
    boolean delete(Book book);
    boolean sell(Book book, User user);
    void removeAll(); //flush


}
