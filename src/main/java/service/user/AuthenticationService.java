package service.user;

import model.Book;
import model.User;
import model.validator.Notification;

import java.util.List;

public interface AuthenticationService {

    Notification<Boolean> register(String username, String password,Integer id_role);

    Notification<User> login(String username, String password);

    Notification<User> getCurrentUser(String username, String password);
    boolean logout(User user);
    public List<User> findAll();
}
