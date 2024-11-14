package view.model;


import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class BookDTO {
    private StringProperty author; //StringProperty: permite legarea unui obiect de tip String la el. vizuale din UI

    public void setAuthor(String author){
        authorProperty().set(author);
    }

    public String getAuthor(){
        return authorProperty().get();
    }

    public StringProperty authorProperty(){
        if(author == null){
            author = new SimpleStringProperty(this, "author");
        }
        return author;
    }
    private StringProperty title;

    public void setTitle(String title){
        titleProperty().set(title);
    }

    public String getTitle(){
        return titleProperty().get();
    }

    public StringProperty titleProperty(){
        if(title == null)
        {
            title = new SimpleStringProperty(this, "title");
        }
        return title;
    }


    public IntegerProperty price;

    public void setPrice(Integer price){
        priceProperty().set(price);
    }

    public Integer getPrice(){ return priceProperty().get();}

    public IntegerProperty priceProperty(){
        if(price == null)
        {
            price = new SimpleIntegerProperty(price, "price");
        }
        return price;
    }

    public IntegerProperty stock;

    public void setStock(Integer stock){
        stockProperty().set(stock);
    }

    public Integer getStock(){ return stockProperty().get();}

    public IntegerProperty stockProperty(){
        if(stock == null)
        {
            stock = new SimpleIntegerProperty(stock, "stock");
        }
        return stock;
    }

}
