package repository.book;

import model.Book;
import model.User;
import model.builder.BookBuilder;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookRepositoryMySQL implements BookRepository {

    private final Connection connection;

    public BookRepositoryMySQL(Connection connection){
        this.connection = connection;
    }

    @Override
    public List<Book> findAll() {
        String sql = "SELECT * FROM book;";

        List<Book> books = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()){
                books.add(getBookFromResultSet(resultSet));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return books;
    }

    @Override
    public Optional<Book> findById(Long id) {
        String sql = "SELECT * FROM book WHERE id=" + id;

        Optional<Book> book = Optional.empty();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {
                book = Optional.of(getBookFromResultSet(resultSet));
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return book;
    }


    @Override
    public int save(Book book) {

//        String newSql = "INSERT INTO book VALUES(null, \'" + book.getAuthor() +"\', \'" + book.getTitle()+"\', \'" + book.getPublishedDate() + "\' );";
        String newSql = "INSERT INTO book VALUES(null, ?, ?, ?, ?, ?);";

        try{
            PreparedStatement preparedStatement = connection.prepareStatement(newSql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, book.getAuthor());
            preparedStatement.setString(2, book.getTitle());
            preparedStatement.setDate(3, java.sql.Date.valueOf(book.getPublishedDate()));

            preparedStatement.setInt(4,book.getPrice());
            preparedStatement.setInt(5,book.getStock());

            //   preparedStatement.setDate(3, java.sql.Date.valueOf(book.getPublishedDate()));
            //    preparedStatement.setDate(3, java.sql.Date.valueOf(LocalDate.now()));


            int rowsInserted = preparedStatement.executeUpdate();

            if (rowsInserted == 1) {

                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }


        } catch (SQLException e){
            e.printStackTrace();
            return -1;
        }
        return -1;
    }

    @Override
    public boolean delete(Book book) {
        String newSql = "DELETE FROM book WHERE author=? and title = ?";


        try{
            PreparedStatement preparedStatement = connection.prepareStatement(newSql);
            preparedStatement.setString(1,book.getAuthor());
            preparedStatement.setString(2,book.getTitle());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }

    }


    public boolean sell(Book book, User user){
        String checkStockSQL = "Select stock from book WHERE author=? AND title=?;";
        String newSql = "UPDATE book SET stock = stock - 1 WHERE author=? AND title=?";
        String saveToOrdersSql = "INSERT INTO orders values(null, ?, ?, ?)";// id, book_id, timestamp, user_id
        try{
            PreparedStatement checkStockStatement = connection.prepareStatement(checkStockSQL);
            checkStockStatement.setString(1, book.getAuthor());
            checkStockStatement.setString(2, book.getTitle());
            System.out.println("ID:" + book.getId());
            ResultSet resultSet = checkStockStatement.executeQuery();
            if(resultSet.next()){
                int stock = resultSet.getInt("stock");
                if(stock <= 0){
                    return false;
                }
            }
            PreparedStatement updateStock = connection.prepareStatement(newSql);
            updateStock.setString(1, book.getAuthor());
            updateStock.setString(2, book.getTitle());
            updateStock.executeUpdate();

            PreparedStatement saveToOrders = connection.prepareStatement(saveToOrdersSql);
            saveToOrders.setInt(1,book.getId());
            saveToOrders.setTimestamp(2, Timestamp.from(Instant.now()));
            saveToOrders.setInt(3,user.getId());
            saveToOrders.executeUpdate();
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public void removeAll() {
        String sql = "DELETE FROM book WHERE id >= 0;";

        try{
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    private Book getBookFromResultSet(ResultSet resultSet) throws SQLException{
        return new BookBuilder()
                .setId(resultSet.getInt("id"))
                .setTitle(resultSet.getString("title"))
                .setAuthor(resultSet.getString("author"))
                .setPublishedDate(new java.sql.Date(resultSet.getDate("publishedDate").getTime()).toLocalDate())
                .setPrice(resultSet.getInt("price"))
                .setStock(resultSet.getInt("stock"))
                .build();

    }
}