package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

public class deleteBookWindowController implements Initializable {

    @FXML
    ListView listView;
    ObservableList<Book> books;
    int curSelectedItems = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        books = FXCollections.observableArrayList();
        books = App.getListOfBooks();
        listView.setItems(books);

        listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                curSelectedItems = listView.getSelectionModel().getSelectedIndex();
            }
        });
    }

    public void delete(ActionEvent actionEvent) {



        if(!books.isEmpty())
        {
            System.out.println("11111111111111111111111111111");
            App.selectAll();
            System.out.println("**********************");



            if(!App.areDifferentThanThisBooksInThisCathegoryStillExiste(books.get(curSelectedItems).getId_kategoria(), books.get(curSelectedItems).id_ksiazka))
            {
                System.out.println(">>>>>>>>>>>>>kategoria juz nie istnieje");
                App.deleteCategoryById(books.get(curSelectedItems).getId_kategoria());

            }

            if(!App.areDifferentThanThisBooksFomThisPublishingHouseStillExiste(books.get(curSelectedItems).getId_wydawnictwo(), books.get(curSelectedItems).getId_ksiazka()))
            {
                System.out.println(">>>>>>>>>>>>>wydawnictwo juz nie istnieje");
                App.deletePublishingHouseById(books.get(curSelectedItems).getId_wydawnictwo());

            }
            if(!App.areDifferentThanThisBooksFromThisAuthorStillExiste( books.get(curSelectedItems).getId_autor(), books.get(curSelectedItems).getId_ksiazka()))
            {
                System.out.println(">>>>>>>>>>>>>autor juz nie istnieje");
                App.deleteAuthorById(books.get(curSelectedItems).getId_autor());
            }
            App.deleteBookById(books.get(curSelectedItems).getId_ksiazka());
            books.remove(curSelectedItems);
            listView.setItems(books);

            System.out.println("2222222222222222222222222222222");
            App.selectAll();
            System.out.println("**********************");

        }

    }

    public void back(ActionEvent actionEvent) throws IOException {
        App.setRoot("workerFirstWindow");
    }

    public void sort(ActionEvent actionEvent) {

        Comparator<Book> comparator = Comparator.comparingInt(Book::getId_ksiazka);
        comparator = comparator.reversed();
        FXCollections.sort(books, comparator);
        listView.setItems(books);
    }
}
