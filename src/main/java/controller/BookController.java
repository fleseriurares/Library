package controller;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import mapper.BookMapper;
import service.BookService;
import view.BookView;
import view.model.BookDTO;
import view.model.builder.BookDTOBuilder;

public class BookController {

    private final BookView bookView;
    private final BookService bookService;
    public BookController(BookView bookView, BookService bookService){
        this.bookView = bookView;
        this.bookService = bookService;

        this.bookView.addSaveButtonListener(new SaveButtonListener());
        this.bookView.addDeleteButtonListener(new DeleteButtonListener());
    }

    private class SaveButtonListener implements EventHandler<ActionEvent>{
        @Override
        public void handle(ActionEvent event){
            String title = bookView.getTitle();
            String author = bookView.getAuthor();

            if(title.isEmpty() || author.isEmpty()){
                bookView.addDisplayAlertMessage("Save error","Problem at Author or Title fields", "Cannot have an empty Title or Author field");
            }
            else{
                BookDTO bookDTO = new BookDTOBuilder().setTitle(title).setAuthor(author).build();
                boolean savedBook = bookService.save(BookMapper.convertBookDTOToBook(bookDTO));

                if(savedBook){
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

}
