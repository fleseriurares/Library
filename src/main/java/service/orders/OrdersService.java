package service.orders;

import model.Book;

public interface OrdersService {

    boolean save(Book book, Integer employee_id);

}
