package reports.generator;

public class ReportData {

    private final int booksSold;
    private final int totalPrice;

    public ReportData(int booksSold, int totalPrice){
        this.booksSold = booksSold;
        this.totalPrice = totalPrice;
    }

    public int getBooksSold(){
        return booksSold;
    }

    public int getTotalPrice(){
        return totalPrice;
    }
}
