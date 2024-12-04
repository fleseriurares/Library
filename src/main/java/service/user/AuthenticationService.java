package service.user;

import model.User;
import model.validator.Notification;

import java.util.List;

public interface AuthenticationService {

    Notification<String> register(String username, String password, Integer id_role);

    Notification<User> login(String username, String password);
    Notification<Boolean> updateRole(String username, String password,Integer id_role);

    Notification<User> getCurrentUser(String username, String password);
    boolean logout(User user);

    public List<User> findAll();
}
