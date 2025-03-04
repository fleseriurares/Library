package model;

import java.util.List;

public class User {
    private Integer id;
    private String username;
    private String password;

    private List<Role> roles;
    public int getId() {
        return id;
    }
    public void setId(Integer id){
        this.id = id;
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public String getRolesToString(){
        StringBuilder sb = new StringBuilder();
        for(Role role: roles){
            sb.append(role.getRole());
        }
        return sb.toString();
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
