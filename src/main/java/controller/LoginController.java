package controller;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import launcher.AdminComponentFactory;
import launcher.EmployeeComponentFactory;
import model.User;
import model.validator.Notification;
import model.validator.UserValidator;
import service.user.AuthenticationService;
import view.LoginView;
import launcher.LoginComponentFactory;
import java.util.EventListener;
import java.util.List;

public class LoginController {

    private final LoginView loginView;
    private final AuthenticationService authenticationService;


    public LoginController(LoginView loginView, AuthenticationService authenticationService) {
        this.loginView = loginView;
        this.authenticationService = authenticationService;
        this.loginView.addLoginButtonListener(new LoginButtonListener());
        this.loginView.addRegisterButtonListener(new RegisterButtonListener());
    }

    private class LoginButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(javafx.event.ActionEvent event) {

            loginView.setActionTargetText("");
            String username = loginView.getUsername();
            String password = loginView.getPassword();

            Notification<User> loginNotification = authenticationService.login(username, password);
            authenticationService.getCurrentUser(username, password);
            if(loginNotification.hasErrors()){
                loginView.setActionTargetText(loginNotification.getFormattedErrors());
            }else{
                loginView.setActionTargetText("LogIn Successful!");
                if(loginNotification.getResult().getRoles().get(0).getRole().equals("administrator")){
                    AdminComponentFactory componentFactory = AdminComponentFactory.getInstance(LoginComponentFactory.getComponentsForTests(),LoginComponentFactory.getStage());
                }
                else{
                    EmployeeComponentFactory.getInstance(LoginComponentFactory.getComponentsForTests(), LoginComponentFactory.getStage(), loginNotification.getResult());

                }
                }
        }
    }

    private class RegisterButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {

            loginView.setActionTargetText("");
            String username = loginView.getUsername();
            String password = loginView.getPassword();

            Notification<Boolean> registerNotification = authenticationService.register(username, password, 1);

            if (registerNotification.hasErrors()) {
                loginView.setActionTargetText((registerNotification.getFormattedErrors()));
            } else {
                loginView.setActionTargetText("Register Successful!");
            }
        }
    }
}