package repository.orders;

import model.Book;

public interface OrdersRepository {

    boolean save(Book book, Integer user_id);



}
