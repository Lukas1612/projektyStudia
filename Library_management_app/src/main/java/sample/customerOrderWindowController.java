package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

public class customerOrderWindowController implements Initializable {

    @FXML
    ListView listView;
    @FXML
    ListView orderedField;
    @FXML
    TextField searchField;



    String sortingText;

    int curSelectedItems = 0;
    int curSelectedOrderedItems = 0;

    ObservableList<SortableBook> sortableBooks;
    ObservableList<Book> orderedBooks;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        App.selectAll();
        SortableBook sortableBook;
        ObservableList<Book> books;

        sortableBooks = FXCollections.observableArrayList();

        orderedBooks = App.getListOfBooksFromOrdersByCustomerId(App.id_adminWorkerCustomer);
        orderedField.setItems(orderedBooks);

        books = App.getListOfBooksThatAreNotOrderedByThisCustomer(App.id_adminWorkerCustomer);
       // books = App.getListOfBooks();

        for (int i=0; i<books.size(); ++i)
        {
            sortableBook = new SortableBook(books.get(i), 0);
            sortableBooks.add(sortableBook);
        }
        listView.setItems(sortableBooks);


        listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                curSelectedItems = listView.getSelectionModel().getSelectedIndex();
            }
        });

        orderedField.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                curSelectedOrderedItems = orderedField.getSelectionModel().getSelectedIndex();
            }
        });

    }

    public void search(ActionEvent actionEvent) {

        App.selectAll();

        if(!searchField.getText().isEmpty())
        {
            sortingText = searchField.getText();
            sort();
            listView.setItems(sortableBooks);
        }

    }

    public void sort(){

        int hash, tmp;
        Author author;


        if( (sortingText != null) && !sortingText.isEmpty() && !sortableBooks.isEmpty())
        {
            for(int i=0; i<sortableBooks.size(); ++i)
            {
                author = App.getAuthorById(sortableBooks.get(i).getId_autor());
                System.out.println("books.get(i).getId_autor():  " + sortableBooks.get(i).getId_autor());


                hash = App.levenshteinDistance(sortableBooks.get(i).getTytul(),  sortingText);

                if(author != null)
                {
                    System.out.println(" author:  " +  author.getImie() + " " +  author.getNazwisko());
                    tmp = App.levenshteinDistance(author.getImie(), sortingText);

                    if(tmp<hash)
                    {
                        hash = tmp;
                    }

                    tmp = App.levenshteinDistance(author.getNazwisko(), sortingText);

                    if(tmp<hash)
                    {
                        hash = tmp;
                    }

                }else
                {
                    System.out.println("ERRROR AUTHOR NULL");
                }


                sortableBooks.get(i).setHashValue(hash);
            }

            Comparator<SortableBook> comparator = Comparator.comparingInt(SortableBook::getHashValue);
            FXCollections.sort(sortableBooks, comparator);
            App.selectAll();
        }
    }

    public void back(ActionEvent actionEvent) throws IOException {
        App.setRoot("customerFirstWindow");
    }

    public void add(ActionEvent actionEvent) {

        System.out.println("App.id_adminWorkerCustomer " + App.id_adminWorkerCustomer);
        if(!sortableBooks.isEmpty() && curSelectedItems != -1)
        {
            App.addNewOrder(App.id_adminWorkerCustomer, sortableBooks.get(curSelectedItems).getId_ksiazka());
            sortableBooks.remove(curSelectedItems);
            listView.setItems(sortableBooks);

            orderedBooks = App.getListOfBooksFromOrdersByCustomerId(App.id_adminWorkerCustomer);
            orderedField.setItems(orderedBooks);

            App.selectAll();
        }

    }


    public void remove(ActionEvent actionEvent) {
        System.out.println("curSelec   " + curSelectedOrderedItems + " " + curSelectedItems);
        System.out.println("App.id_adminWorkerCustomer " + App.id_adminWorkerCustomer);
        if(!orderedBooks.isEmpty() && curSelectedOrderedItems != -1)
        {
            ObservableList<Book> books;
            SortableBook sortableBook;

            App.deleteOrderByBookAndCustomerId(orderedBooks.get(curSelectedOrderedItems).getId_ksiazka(), App.id_adminWorkerCustomer);
            orderedBooks.remove(curSelectedOrderedItems);
            orderedField.setItems(orderedBooks);


            books = App.getListOfBooksThatAreNotOrderedByThisCustomer(App.id_adminWorkerCustomer);

            sortableBooks=FXCollections.observableArrayList();

            for (int i=0; i<books.size(); ++i)
            {
                sortableBook = new SortableBook(books.get(i), 0);
                sortableBooks.add(sortableBook);
            }

            if((sortingText != null) && !sortingText.isEmpty())
            {
                sort();
            }
            listView.setItems(sortableBooks);

        }
    }
}
