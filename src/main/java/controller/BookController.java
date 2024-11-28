package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import mapper.BookMapper;
import model.Book;
import model.User;
import service.book.BookService;
import service.orders.OrdersService;
import service.user.AuthenticationService;
import view.BookView;
import view.model.BookDTO;
import view.model.builder.BookDTOBuilder;

public class BookController {

    private final BookView bookView;
    private final BookService bookService;
    private final OrdersService ordersService;
    private final AuthenticationService authenticationService;
    private final User currentUser;
    public BookController(BookView bookView, BookService bookService, OrdersService ordersService, AuthenticationService authenticationService, User user){
        this.bookView = bookView;
        this.bookService = bookService;
        this.ordersService = ordersService;
        this.authenticationService = authenticationService;
        this.currentUser = user;

        this.bookView.addSaveButtonListener(new SaveButtonListener());
        this.bookView.addDeleteButtonListener(new DeleteButtonListener());
        this.bookView.addSellButtonListener(new SellButtonListener());
    }

    private class SaveButtonListener implements EventHandler<ActionEvent>{
        @Override
        public void handle(ActionEvent event){
            String title = bookView.getTitle();
            String author = bookView.getAuthor();

            Integer price = Integer.valueOf(bookView.getPrice());
            Integer stock = Integer.valueOf(bookView.getStock());

            if(title.isEmpty() || author.isEmpty()){
                bookView.addDisplayAlertMessage("Save error","Problem at Author or Title fields", "Cannot have an empty Title or Author field");
            }
            else{
                BookDTO bookDTO = new BookDTOBuilder().setTitle(title).setAuthor(author).setPrice(price).setStock(stock).build();
                Book book = BookMapper.convertBookDTOToBook(bookDTO);
                int savedBookId = bookService.save(book);

                bookDTO = new BookDTOBuilder().setId(book.getId()).setTitle(title).setAuthor(author).setPrice(price).setStock(stock).build();

                if(savedBookId != -1){
                    bookDTO = new BookDTOBuilder().setId(savedBookId).setTitle(title).setAuthor(author).setPrice(price).setStock(stock).build();
                    bookView.addDisplayAlertMessage("Save Successful","Book Added", "Book was successfully added to the database."); //update la baza de date
                    bookView.addBookToObservableList(bookDTO); //update la interfata
                }
                else{
                    bookView.addDisplayAlertMessage("Save error","Problem at adding Book", "There was a problem at adding the book to the database. Please try again!");

                }
            }
        }
    }

    private class DeleteButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            BookDTO bookDTO = (BookDTO) bookView.getBookTableView().getSelectionModel().getSelectedItem();
            if(bookDTO != null){
                boolean deletionSuccessful = bookService.delete(BookMapper.convertBookDTOToBook(bookDTO));

                if(deletionSuccessful)
                {
                    bookView.addDisplayAlertMessage("Deletion Successful","Book Deleted", "Book was successfully deleted to the database.");
                    bookView.removeBookFromObservableList(bookDTO);
                }else{
                    bookView.addDisplayAlertMessage("Deletion error","Problem at deleting Book", "There was a problem at deleting the book from the database. Please try again!");

                }

            } else{
                bookView.addDisplayAlertMessage("Delete error","Problem at deleting books", "You must select a book before pressing delete button");
            }

        }
    }

    private class SellButtonListener implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            BookDTO bookDTO = (BookDTO) bookView.getBookTableView().getSelectionModel().getSelectedItem();

            if(bookDTO != null){
              //  Notification<User> userNotification = authenticationService.getCurrentUser();
                boolean soldSuccessful = bookService.sell(BookMapper.convertBookDTOToBook(bookDTO), currentUser);
                if(soldSuccessful)
                {

                    bookView.addDisplayAlertMessage("Sold Successful","Book Sold", "You sold a book.");
                    //bookView.updateSellBookToObservableList(bookDTO);
                    bookDTO.setStock(bookDTO.getStock() - 1);
                    bookView.updateSellBookFromObservableList(bookDTO);
                }else{
                    bookView.addDisplayAlertMessage("No more in stock","Problem at selling the book", "There was a problem at selling a book . Please try again!");
                }
            } else{
                bookView.addDisplayAlertMessage("Selling error","Problem at selling books", "You must select a book before pressing sell button");
            }
        }
    }


}
