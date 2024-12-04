package service.orders;

import model.Book;
import reports.generator.ReportData;
import repository.orders.OrdersRepository;

import java.util.Map;

public class OrdersServiceImpl implements OrdersService {

    private final OrdersRepository ordersRepository;

    public OrdersServiceImpl(OrdersRepository ordersRepository){
        this.ordersRepository = ordersRepository;
    }

    @Override
    public boolean save(Book book, Integer employee_id) {
        return ordersRepository.save(book,employee_id);
    }

    @Override
    public Map<String, ReportData> getSalesReport(){
        return ordersRepository.getSalesReport();
    }
}
