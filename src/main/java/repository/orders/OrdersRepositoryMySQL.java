package repository.orders;

import model.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;

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
}
