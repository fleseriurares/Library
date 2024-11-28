package service.orders;

import model.Book;
import repository.orders.OrdersRepository;

public class OrdersServiceImpl implements OrdersService {

    private final OrdersRepository ordersRepository;

    public OrdersServiceImpl(OrdersRepository ordersRepository){
        this.ordersRepository = ordersRepository;
    }

    @Override
    public boolean save(Book book, Integer employee_id) {
        return ordersRepository.save(book,employee_id);
    }
}
