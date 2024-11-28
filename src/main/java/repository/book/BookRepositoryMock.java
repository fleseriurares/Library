package repository.book;

import model.Book;
import model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookRepositoryMock implements BookRepository {
    private final List<Book> books;

    public BookRepositoryMock()
    {
        books = new ArrayList<>();
    }

    @Override
    public List<Book> findAll() {
        return books;
    }

    @Override
    public Optional<Book> findById(Long id) {
        return books.parallelStream() // pentru mai multe core-uri se crreeaza tot atatea steam-uri de date
                .filter(it -> it.getId().equals(id))
                .findFirst();
    }

    @Override
    public int save(Book book) {
       if(books.add(book)){
           return 1;
       }
       return -1;
    }

    @Override
    public boolean delete(Book book) {
        return books.remove(book);
    }

    @Override
    public boolean sell(Book book, User user) {
        return true;
    }



    @Override
    public void removeAll() {
        books.clear();
    }
}
