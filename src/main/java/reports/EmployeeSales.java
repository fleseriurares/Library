package reports;

public class EmployeeSales {
    private Long userId;
    private String username;
    private Integer totalSales;

    public EmployeeSales(Long userId, String username, Integer totalSales){
        this.userId = userId;
        this.username = username;
        this.totalSales = totalSales;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(Integer totalSales) {
        this.totalSales = totalSales;
    }
}
