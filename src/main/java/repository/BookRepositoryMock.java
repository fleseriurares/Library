package repository;

import model.Book;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookRepositoryMock implements BookRepository{
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
    public boolean save(Book book) {
        return books.add(book);
    }

    @Override
    public boolean delete(Book book) {
        return books.remove(book);
    }

    @Override
    public boolean sellOne(Book book){
        return book.getStock() > 0;
    }

    @Override
    public void removeAll() {
        books.clear();
    }
}
