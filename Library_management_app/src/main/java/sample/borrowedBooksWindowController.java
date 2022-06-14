package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class borrowedBooksWindowController implements Initializable {

    @FXML
    ListView  listView;
    ObservableList<Book> books;
    ObservableList<SortableBook> sortableBooks;
    SortableBook sortableBook;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        books = FXCollections.observableArrayList();
        sortableBooks = FXCollections.observableArrayList();

        books = App.getListOfBooksThatAreBorrowedByThisCustomer(App.id_adminWorkerCustomer);

        for(int i=0; i<books.size(); ++i)
        {
            if(App.wasThisCustomerBorrowedThisBookAtFirst(books.get(i).getId_ksiazka(), App.id_adminWorkerCustomer))
            {
                sortableBook = new SortableBook(books.get(i), 0);
                sortableBooks.add(sortableBook);
            }
        }


        listView.setItems(sortableBooks);
    }

    public void back(ActionEvent actionEvent) throws IOException {
        App.setRoot("customerFirstWindow");
    }
}
