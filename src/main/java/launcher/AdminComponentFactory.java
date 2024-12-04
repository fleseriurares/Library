package launcher;

import controller.AdminController;
import database.DatabaseConnectionFactory;
import javafx.stage.Stage;
import mapper.UserMapper;
import model.User;
import repository.security.RightsRolesRepository;
import repository.security.RightsRolesRepositoryMySQL;
import repository.user.UserRepository;
import repository.user.UserRepositoryMySQL;
import service.user.AuthenticationService;
import service.user.AuthenticationServiceImpl;
import view.AdminView;
import view.model.UserDTO;

import java.sql.Connection;
import java.util.List;

public class AdminComponentFactory {

    private final AdminView adminView;
    private final AdminController adminController;
    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;

    private static volatile AdminComponentFactory instance;
    private final RightsRolesRepository rightsRolesRepository;

    public static AdminComponentFactory getInstance(boolean componentForTest, Stage primaryStage){
        if(instance == null)
        {   synchronized (AdminComponentFactory.class){
            if(instance == null)
                instance = new AdminComponentFactory(componentForTest, primaryStage);
        }
        }
        return instance;
    }

    private AdminComponentFactory(Boolean componentsForTest, Stage primaryStage){
        Connection connection = DatabaseConnectionFactory.getConnectionWrapper(componentsForTest).getConnection();

        this.rightsRolesRepository = new RightsRolesRepositoryMySQL(connection);
        this.userRepository = new UserRepositoryMySQL(connection, rightsRolesRepository);
        this.authenticationService = new AuthenticationServiceImpl(userRepository, rightsRolesRepository);

        List<UserDTO> userDTOs = UserMapper.convertUserListToUserDTOList(authenticationService.findAll());

        System.out.println(userDTOs);
        this.adminView = new AdminView(primaryStage, userDTOs);
        this.adminController = new AdminController(adminView, authenticationService);

    }


}
