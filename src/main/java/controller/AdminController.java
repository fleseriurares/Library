package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import mapper.UserMapper;
import model.validator.Notification;
import service.user.AuthenticationService;
import view.AdminView;
import view.model.UserDTO;
import view.model.builder.UserDTOBuilder;

public class AdminController {

    private final AdminView adminView;
    private final AuthenticationService authenticationService;


    public AdminController(AdminView adminView, AuthenticationService authenticationService){
        this.adminView = adminView;
        this.authenticationService = authenticationService;
        this.adminView.addSaveButtonListener(new SaveButtonListener());
    }

    private class SaveButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {


            String username = adminView.getUsername();
            String password = adminView.getPassword();
            String role = adminView.getRole();
            Integer id_role;
            if (role.equals("Administrator")){
                id_role = 1;
            }else if(role.equals("Employee")){
                id_role = 2;
            }else{
                id_role = 3;
            }


            Notification<Boolean> registerNotification = authenticationService.register(username, password,id_role);

            if (registerNotification.hasErrors()) {
                adminView.setActionTargetText((registerNotification.getFormattedErrors()));
            } else {
                adminView.setActionTargetText("Register Successful!");
                UserDTO userDTO = new UserDTOBuilder().setUsername(username).setPassword(password).build();
                adminView.addUserToObservableList(userDTO);
            }
        }
    }

}
