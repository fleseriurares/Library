import database.DatabaseConnectionFactory;
import model.Book;
import model.builder.BookBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.BookRepository;

import repository.BookRepositoryMySQL;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class BookRepositoryMySQLTest {
    private static BookRepository bookRepository;


    @BeforeAll
    public static void setup(){
        Connection connection = DatabaseConnectionFactory.getConnectionWrapper(false).getConnection();
        bookRepository = new BookRepositoryMySQL(connection);
    }

    @BeforeEach
    public void cleanDataBase(){
        bookRepository.removeAll();
        bookRepository.save(new BookBuilder().setTitle("Harry Potter").setAuthor("J. K. Rowling").setPublishedDate(LocalDate.of(1999,12,12)).setPrice(0).setStock(0).build());
        bookRepository.save(new BookBuilder().setTitle("Povestea lui Harap Alb").setAuthor("Ion Creanga").setPublishedDate(LocalDate.of(1926,7,24)).setPrice(0).setStock(0).build());
    }

    @Test
    public void findAll(){
        List<Book> books = bookRepository.findAll();
        assertEquals(2, books.size());
    }

    @Test
    public void findById(){
        final Optional<Book> book = bookRepository.findById(3L);
        assertTrue(book.isEmpty());
    }

    @Test
    public void save(){
        assertTrue(bookRepository.save(new BookBuilder().setTitle("Ion").setAuthor("Liviu Rebreanu)").setPublishedDate(LocalDate.of(1900,10,2)).setPrice(0).setStock(0).build()));
    }

    @Test
    public void delete(){
        Optional<Book> deletedBook = bookRepository.findById(1L);
        assertTrue(deletedBook.isEmpty());
    }


    @Test
    public void removeAll(){
        bookRepository.removeAll();
        assertEquals(0, bookRepository.findAll().size());
    }
}
