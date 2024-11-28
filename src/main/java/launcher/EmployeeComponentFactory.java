package launcher;

import controller.BookController;
import database.DatabaseConnectionFactory;
import javafx.stage.Stage;
import mapper.BookMapper;
import model.User;
import repository.book.BookRepository;
import repository.book.BookRepositoryMySQL;
import repository.orders.OrdersRepository;
import repository.orders.OrdersRepositoryMySQL;
import repository.security.RightsRolesRepository;
import repository.security.RightsRolesRepositoryMySQL;
import repository.user.UserRepository;
import repository.user.UserRepositoryMySQL;
import service.book.BookService;
import service.book.BookServiceImpl;
import service.orders.OrdersService;
import service.orders.OrdersServiceImpl;
import service.user.AuthenticationService;
import service.user.AuthenticationServiceImpl;
import view.BookView;
import view.model.BookDTO;

import java.sql.Connection;
import java.util.List;

public class EmployeeComponentFactory {

    private final BookView bookView;
    private final BookController bookController;


    private final BookRepository bookRepository;
    private final BookService bookService;
    private final OrdersRepository ordersRepository;
    private final OrdersService ordersService;

    private static volatile EmployeeComponentFactory instance;
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;
    private final RightsRolesRepository rightsRolesRepository;

    public static EmployeeComponentFactory getInstance(boolean componentForTest, Stage primaryStage, User user){
        if(instance == null)
        {   synchronized (EmployeeComponentFactory.class){
            if(instance == null)
                instance = new EmployeeComponentFactory(componentForTest, primaryStage, user);
        }
        }
        return instance;
    }

    private EmployeeComponentFactory(Boolean componentsForTest, Stage primaryStage, User user){
        Connection connection = DatabaseConnectionFactory.getConnectionWrapper(componentsForTest).getConnection();
        this.bookRepository = new BookRepositoryMySQL(connection);
        this.bookService = new BookServiceImpl(bookRepository);
        this.ordersRepository = new OrdersRepositoryMySQL(connection);
        this.ordersService = new OrdersServiceImpl(ordersRepository);
        this.rightsRolesRepository = new RightsRolesRepositoryMySQL(connection);
        this.userRepository = new UserRepositoryMySQL(connection, rightsRolesRepository);
        this.authenticationService = new AuthenticationServiceImpl(userRepository, rightsRolesRepository);
        List<BookDTO> bookDTOs = BookMapper.convertBookListToBookDTOList(bookService.findAll());


        this.bookView = new BookView(primaryStage, bookDTOs);
        this.bookController = new BookController(bookView, bookService, ordersService, authenticationService, user);

    }

    public BookView getBookView() {
        return bookView;
    }

    public BookController getBookController() {
        return bookController;
    }

    public BookRepository getBookRepository() {
        return bookRepository;
    }

    public BookService getBookService() {
        return bookService;
    }
}
