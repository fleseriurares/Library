package repository;

public abstract class BookRepositoryDecorator implements BookRepository{

    protected BookRepository decoratedBookRepository; //cache

    public BookRepositoryDecorator(BookRepository bookRepository){
        decoratedBookRepository = bookRepository;
    }

}
