package repository.orders;

import javafx.scene.control.PasswordField;
import model.Book;
import reports.generator.ReportData;

import java.sql.*;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class OrdersRepositoryMySQL implements OrdersRepository{

    private final Connection connection;

    public OrdersRepositoryMySQL(Connection connection){
        this.connection = connection;
    }

    @Override
    public boolean save(Book book, Integer employeeId) {

//        String newSql = "INSERT INTO book VALUES(null, \'" + book.getAuthor() +"\', \'" + book.getTitle()+"\', \'" + book.getPublishedDate() + "\' );";
        String newSql = "INSERT INTO book VALUES(null, ?, ?, ?);";

        try{
            PreparedStatement preparedStatement = connection.prepareStatement(newSql);
            preparedStatement.setString(1, book.getTitle());
            preparedStatement.setTimestamp(2, Timestamp.from(Instant.now()));
            preparedStatement.setInt(3, employeeId);

            //   preparedStatement.setDate(3, java.sql.Date.valueOf(book.getPublishedDate()));
            //    preparedStatement.setDate(3, java.sql.Date.valueOf(LocalDate.now()));


            int rowsInserted = preparedStatement.executeUpdate();

            return (rowsInserted != 1) ? false : true;

        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Map<String, ReportData> getSalesReport() {
        String sql = "SELECT u.username, " +
                "       COUNT(o.book_id) AS books_sold, " +
                "       SUM(b.price) AS total_price " +
                " FROM orders o" +
                " JOIN user u ON o.user_id = u.id" +
                " JOIN book b ON o.book_id = b.id" +
                " JOIN user_role ur ON u.id = ur.user_id" +
                " WHERE ur.role_id = 2" +
                " GROUP BY u.username;";
        Map<String, ReportData> reportData = new HashMap<>();

        try{
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while(resultSet.next()){
                String username = resultSet.getString("username");
                int booksSold = resultSet.getInt("books_sold");
                int totalPrice = resultSet.getInt("total_price");
                reportData.put(username, new ReportData(booksSold, totalPrice));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return reportData;
    }


}
