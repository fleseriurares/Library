package repository.book;

import model.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    List<Book> findAll(); //returneaza lista de carti
    Optional<Book> findById(Long id);
    boolean save(Book book);
    boolean delete(Book book);
    void removeAll(); //flush


}
