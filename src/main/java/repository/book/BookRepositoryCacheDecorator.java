package repository.book;

import model.Book;
import model.User;

import java.util.List;
import java.util.Optional;

import static java.util.Locale.filter;

public class BookRepositoryCacheDecorator extends BookRepositoryDecorator {

    private Cache<Book> cache;

    public BookRepositoryCacheDecorator(BookRepository bookRepository, Cache<Book>  cache){
        super(bookRepository);
        this.cache = cache;
    }

    @Override
    public List<Book> findAll() {
        if(cache.hasResult()){
            return cache.load();
        }

        List<Book> books = decoratedBookRepository.findAll();
        cache.save(books);
        return books;
    }

    @Override
    public Optional<Book> findById(Long id) {

        if(cache.hasResult()){
            return cache.load().stream()
                    .filter(it -> it.getId().equals(id))
                    .findFirst();
        }
        return decoratedBookRepository.findById(id);
    }

    @Override
    public int save(Book book) {
        cache.invalidateCache();
        return decoratedBookRepository.save(book);
    }

    @Override
    public boolean delete(Book book) {
        cache.invalidateCache();
        return decoratedBookRepository.delete(book);
    }

    @Override
    public boolean sell(Book book, User user) {
        cache.invalidateCache();
        return decoratedBookRepository.sell(book, user);

    }

    @Override
    public void removeAll() {
        cache.invalidateCache();
        decoratedBookRepository.removeAll();
    }
}