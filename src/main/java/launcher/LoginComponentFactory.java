package launcher;

import controller.BookController;
import controller.LoginController;
import database.DatabaseConnectionFactory;
import javafx.stage.Stage;
import mapper.BookMapper;
import repository.book.BookRepository;
import repository.book.BookRepositoryMySQL;
import repository.security.RightsRolesRepository;
import repository.security.RightsRolesRepositoryMySQL;
import repository.user.UserRepository;
import repository.user.UserRepositoryMySQL;
import service.book.BookService;
import service.book.BookServiceImpl;
import service.user.AuthenticationService;
import service.user.AuthenticationServiceImpl;
import view.BookView;
import view.LoginView;
import view.model.BookDTO;

import java.sql.Connection;
import java.util.List;

public class LoginComponentFactory {
    private final LoginView loginView;
    private final LoginController loginController;
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;
    private final RightsRolesRepository rightsRolesRepository;
    private final BookRepository bookRepository;
    private final BookService bookService;
    private static volatile LoginComponentFactory instance;
    public static LoginComponentFactory getInstance(boolean componentForTest, Stage primaryStage){
        if(instance == null)
        {   synchronized (LoginComponentFactory.class){
            if(instance == null)
                instance = new LoginComponentFactory(componentForTest, primaryStage);
        }
        }
        return instance;
    }

    private LoginComponentFactory(Boolean componentsForTest, Stage primaryStage){
        Connection connection = DatabaseConnectionFactory.getConnectionWrapper(componentsForTest).getConnection();
        this.bookRepository = new BookRepositoryMySQL(connection);
        this.bookService = new BookServiceImpl(bookRepository);

        this.rightsRolesRepository = new RightsRolesRepositoryMySQL(connection);
        this.userRepository = new UserRepositoryMySQL(connection,rightsRolesRepository);
        this.authenticationService = new AuthenticationServiceImpl(userRepository, rightsRolesRepository);
        this.loginView = new LoginView(primaryStage);
        this.loginController = new LoginController(loginView, authenticationService);

    }

    public LoginView getLoginView() {
        return loginView;
    }

    public LoginController getLoginController() {
        return loginController;
    }

    public AuthenticationService getAuthenticationService() {
        return authenticationService;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public RightsRolesRepository getRightsRolesRepository() {
        return rightsRolesRepository;
    }
}
