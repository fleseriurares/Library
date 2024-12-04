package service.user;
import model.Role;
import model.User;
import model.builder.UserBuilder;

import model.validator.Notification;
import model.validator.UserValidator;
import repository.security.RightsRolesRepository;
import repository.user.UserRepository;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Collections;
import java.util.List;

import static database.Constants.Roles.*;

public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final RightsRolesRepository rightsRolesRepository;

    public AuthenticationServiceImpl(UserRepository userRepository, RightsRolesRepository rightsRolesRepository) {
        this.userRepository = userRepository;
        this.rightsRolesRepository = rightsRolesRepository;
    }




    @Override
    public Notification<String> register(String username, String password, Integer id_role) {

        Role newRole = getRoleById(id_role);

        User user = new UserBuilder()
                .setUsername(username)
                .setPassword(password)
                .setRoles(Collections.singletonList(newRole))
                .build();

        UserValidator userValidator = new UserValidator(user);

        boolean userValid = userValidator.validate();
        boolean alreadyExists = userRepository.existsByUsername(username);

        Notification<String> userRegisterNotification = new Notification<>();

        if(!userValid){

            userValidator.getErrors().forEach(userRegisterNotification::addError);
            userRegisterNotification.setResult("-1");
        } else if(alreadyExists) {

            userRegisterNotification.addError("Username already taken.");
            userRegisterNotification.setResult("-1");
        }else{
                String hashedPassword = hashPassword(password);
                user.setPassword(hashedPassword);
                if(!userRepository.save(user)){
                    userRegisterNotification.setResult("-1");
                }else{
                    userRegisterNotification.setResult(hashedPassword);
                }
        }
        return userRegisterNotification;
    }

    public boolean isNewRole(User user, Role role){
        List<Role> userRoles = user.getRoles();
        for (Role ro: userRoles){
            if (ro.equals(role)){
                return false;
            }
        }
        return true;
    }

    @Override
    public Notification<User> login(String username, String password) {

        return userRepository.findByUsernameAndPassword(username, hashPassword(password));
    }

    public Role getRoleById(Integer id_role){

        if(id_role == 1){
            return rightsRolesRepository.findRoleByTitle(ADMINISTRATOR);
        }
        else if (id_role == 2){
            return rightsRolesRepository.findRoleByTitle(EMPLOYEE);
        }
        else{
            return rightsRolesRepository.findRoleByTitle(CUSTOMER);
        }

    }



    @Override
    public Notification<Boolean> updateRole(String username, String password, Integer id_role) {

        Role newRole = getRoleById(id_role);

        User user = new UserBuilder()
                .setUsername(username)
                .setPassword(password)
                .setRoles(Collections.singletonList(newRole))
                .build();

        UserValidator userValidator = new UserValidator(user);
        boolean userValid = userValidator.validate();
        boolean alreadyExists = userRepository.existsByUsername(username);
        Notification<Boolean> userUpdateNotification = new Notification<>();
        if (!userValid) {
            userValidator.getErrors().forEach(userUpdateNotification::addError);
            userUpdateNotification.setResult(Boolean.FALSE);
        }

        else if (alreadyExists) {
            Notification<User> existingUser = userRepository.findByUsernameAndPassword(username, hashPassword(password));
            if (isNewRole(existingUser.getResult(), newRole)) {
                rightsRolesRepository.addRolesToUser(existingUser.getResult(), Collections.singletonList(newRole));
            } else {
                userUpdateNotification.addError("This user already has this role.");
                userUpdateNotification.setResult(Boolean.FALSE);
            }
        } else {
            userUpdateNotification.addError("Invalid username or password!");
            userUpdateNotification.setResult(Boolean.FALSE);
        }


        return userUpdateNotification;
    }

    @Override
    public Notification<User> getCurrentUser(String username, String password) {

        return userRepository.findByUsernameAndPassword(username, password);
    }

    @Override
    public List<User> findAll(){
       return userRepository.findAll();
    }

    @Override
    public boolean logout(User user) {
        return false;
    }

    private String hashPassword(String password) {
        try {
            // Sercured Hash Algorithm - 256
            // 1 byte = 8 biți
            // 1 byte = 1 char
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}