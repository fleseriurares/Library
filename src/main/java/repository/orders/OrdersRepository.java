package repository.orders;

import model.Book;
import reports.generator.ReportData;

import java.util.Map;

public interface OrdersRepository {

    public boolean save(Book book, Integer user_id);

    public Map<String, ReportData> getSalesReport();


}
