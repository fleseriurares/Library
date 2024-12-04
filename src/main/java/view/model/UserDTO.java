package view.model;

import database.Constants;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import model.Role;

import java.util.List;

public class UserDTO {

    public StringProperty username;
    public void setUsername(String username){
        usernameProperty().set(username);
    }

    public String getUsername(){
        return usernameProperty().get();
    }

    public StringProperty usernameProperty(){
        if(username == null){
            username = new SimpleStringProperty(username, "username");
        }
        return username;
    }

    public StringProperty password;
    public void setPassword(String password){
        passwordProperty().set(password);
    }

    public String getPassword(){
        return passwordProperty().get();
    }

    public StringProperty passwordProperty(){
        if(password == null){
            password = new SimpleStringProperty(password, "password");
        }
        return password;
    }



    private IntegerProperty id;
    public IntegerProperty idProperty(){
        if(id == null){
            id = new SimpleIntegerProperty(this,"id");
        }
        return id;
    }

    public void setId(Integer id) {
        this.idProperty().set(id);
    }

    public Integer getID() {
        return idProperty().get();

    }


    public StringProperty roles;
    public void setRoles(List<Role> roles){
        StringBuilder sb = new StringBuilder();
        for(Role role : roles){
            sb.append(role.getRole()).append(", ");
        }
        rolesProperty().set(sb.toString());
    }

    public String getRoles(){
        return rolesProperty().get();
    }

    public StringProperty rolesProperty(){
        if(roles == null){
            roles = new SimpleStringProperty(roles, "role");
        }
        return roles;
    }

}
