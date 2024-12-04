package service.orders;

import model.Book;
import reports.generator.ReportData;

import java.util.Map;

public interface OrdersService {

    boolean save(Book book, Integer employee_id);
    public Map<String, ReportData> getSalesReport();

    }
