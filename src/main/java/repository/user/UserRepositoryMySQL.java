package repository.user;

import model.Book;
import model.Role;
import model.User;
import model.builder.BookBuilder;
import model.builder.UserBuilder;
import model.validator.Notification;
import repository.security.RightsRolesRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static database.Constants.Roles.EMPLOYEE;
import static database.Constants.Tables.USER;

public class UserRepositoryMySQL implements UserRepository{
    private final Connection connection;
    private final RightsRolesRepository rightsRolesRepository;

    public UserRepositoryMySQL(Connection connection, RightsRolesRepository rightsRolesRepository) {
        this.connection = connection;
        this.rightsRolesRepository = rightsRolesRepository;
    }

    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM user WHERE id IN ( SELECT user_id FROM user_role);";

        List<User> users = new ArrayList<>();

        try{
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()){
                users.add(getUserFromResultSet(resultSet));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    @Override
    public Notification<User> findByUsernameAndPassword(String username, String password) {

        Notification<User> findByUsernameAndPasswordNotification = new Notification<>();
        try{
             String fetchUserSql =
                     "Select * from `" + USER + "` WHERE `username` = ? AND `password` = ?";
             PreparedStatement statement1 = connection.prepareStatement(fetchUserSql);
            statement1.setString(1, username);
            statement1.setString(2,password);

            ResultSet userResultSet = statement1.executeQuery();
            if(userResultSet.next()){

                User user = new UserBuilder().setId(userResultSet.getInt("id")).setUsername(userResultSet.getString("username"))
                        .setPassword(userResultSet.getString("password"))
                        .setRoles(rightsRolesRepository.findRolesForUser(userResultSet.getLong("id")))
                        .build();
                findByUsernameAndPasswordNotification.setResult(user);
            }
            else{
                System.out.println(username);
                System.out.println(password);
                findByUsernameAndPasswordNotification.addError("Invalid username or password!");
                return findByUsernameAndPasswordNotification;
            }

         } catch (SQLException e) {
             findByUsernameAndPasswordNotification.addError("Something is wrong with the Database!");
            throw new RuntimeException(e);
         }
         return findByUsernameAndPasswordNotification;
    }

    @Override
    public boolean save(User user) {
        try {
            PreparedStatement insertUserStatement = connection
                    .prepareStatement("INSERT INTO user values (null, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            insertUserStatement.setString(1, user.getUsername());
            insertUserStatement.setString(2, user.getPassword());
            insertUserStatement.executeUpdate();

            ResultSet rs = insertUserStatement.getGeneratedKeys();
            rs.next();
            Integer userId = rs.getInt(1);
            user.setId(userId);

            rightsRolesRepository.addRolesToUser(user, user.getRoles());

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public void removeAll() {
        try {
            Statement statement = connection.createStatement();
            String sql = "DELETE from user where id >= 0";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean existsByUsername(String email) {
        try {
            String fetchUserSql =
                    "Select * from `" + USER + "` where `username`= ? ";
            PreparedStatement statement1 = connection.prepareStatement(fetchUserSql);
            statement1.setString(1, email);
            ResultSet userResultSet = statement1.executeQuery();
            return userResultSet.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }



//    private String getRolesFromUser(User user){
//        Integer id = user.getId();
//        String sqlQuery = "SELECT role FROM role WHERE id IN (SELECT id_role FROM user_role WHERE id_user = ?);";
//
//        StringBuilder sb = new StringBuilder();
//        try{
//            PreparedStatement pstmt = connection.prepareStatement(sqlQuery);
//            pstmt.setInt(1, id);
//            ResultSet resultSet = pstmt.executeQuery();
//            while (resultSet.next()){
//                sb.append(resultSet.getString("role"));
//                sb.append(", ");
//            }
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//        return sb.toString();
//    }

    private User getUserFromResultSet(ResultSet resultSet) throws SQLException{


        return new UserBuilder()
                .setId(resultSet.getInt("id"))
                .setUsername(resultSet.getString("username"))
                .setPassword(resultSet.getString("password"))
                .setRoles(rightsRolesRepository.findRolesForUser(resultSet.getLong("id")))
                .build();
    }



}
