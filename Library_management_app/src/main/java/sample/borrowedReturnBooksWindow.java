package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

public class borrowedReturnBooksWindow implements Initializable {

    @FXML
    ListView listView1;
    @FXML
    ListView listView2;
    @FXML
    ListView searchView;
    @FXML
    ListView waitView;
    @FXML
    TextField searchField;

    ObservableList<SortableBook> allBooks;
    ObservableList<SortableBook> orderedBooks;
    ObservableList<SortableBook> borrowedBooks;
    ObservableList<SortableBook> waitingBooks;
    int curSelectedItem = -1;
    int curSelectedItem2 = -1;
    int curSelectedWindow = -1;

    boolean isSorted = false;
    String searchText;



    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ObservableList<Book> books;
        books = App.getListOfBooks();
        SortableBook sortableBook;
        Book book;

        allBooks= FXCollections.observableArrayList();


        for(int i=0; i<books.size(); ++i)
        {
            sortableBook = new SortableBook(books.get(i), 0);
            allBooks.add(sortableBook);
        }

        listView1.setItems(allBooks);

        orderedBooks = FXCollections.observableArrayList();
        books = App.getListOfBooksFromOrdersByCustomerId(App.customerSelectedByWorker);
        for(int i=0; i<books.size(); ++i)
        {
            sortableBook = new SortableBook(books.get(i), 0);
            orderedBooks .add(sortableBook);
        }

        listView2.setItems(orderedBooks);

        listView1.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                curSelectedItem = listView1.getSelectionModel().getSelectedIndex();
                curSelectedWindow = 0;
            }
        });

        listView2.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                curSelectedItem = listView2.getSelectionModel().getSelectedIndex();
                curSelectedWindow = 1;
            }
        });

        waitView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                curSelectedItem2 = waitView.getSelectionModel().getSelectedIndex();
                curSelectedWindow = 2;
            }
        });


        searchView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                curSelectedItem2 = searchView.getSelectionModel().getSelectedIndex();
                curSelectedWindow = 3;

            }
        });

        borrowedBooks = FXCollections.observableArrayList();
        books = App.getListOfBooksThatAreBorrowedByThisCustomer(App.customerSelectedByWorker);
        for(int i=0; i<books.size(); ++i)
        {
            if(App.wasThisCustomerBorrowedThisBookAtFirst(books.get(i).getId_ksiazka(), App.customerSelectedByWorker))
            {
                sortableBook = new SortableBook(books.get(i), 0);
                borrowedBooks.add(sortableBook);
            }
        }

        searchView.setItems(borrowedBooks);

        waitingBooks = FXCollections.observableArrayList();
        books = App.getListOfBooksThatAreBorrowedByThisCustomer(App.customerSelectedByWorker);
        for(int i=0; i<books.size(); ++i)
        {
            if(!App.wasThisCustomerBorrowedThisBookAtFirst(books.get(i).getId_ksiazka(), App.customerSelectedByWorker))
            {
                sortableBook = new SortableBook(books.get(i), 0);
                waitingBooks.add(sortableBook);
            }
        }

        waitView.setItems(waitingBooks);




    }

    public void back(ActionEvent actionEvent) throws IOException {
        App.setRoot("customerSelectorWindow");
    }

    public void search(ActionEvent actionEvent) {
        isSorted = true;
        if(!searchField.getText().isEmpty())
        {
            searchText = searchField.getText();


            sort();
        }
    }

    public void remove(ActionEvent actionEvent) {

        System.out.println("curSelectedItem2 =  " +  curSelectedItem2 );
        if(curSelectedItem2 != -1)
        {
            long time = System.currentTimeMillis();

            if(curSelectedWindow == 2)
            {
                App.returnBorrowedBook(waitingBooks.get(curSelectedItem2).getId_ksiazka(), App.id_adminWorkerCustomer, String.valueOf(time), App.customerSelectedByWorker);

            }

            if(curSelectedWindow == 3)
            {
                App.returnBorrowedBook(borrowedBooks.get(curSelectedItem2).getId_ksiazka(), App.id_adminWorkerCustomer, String.valueOf(time), App.customerSelectedByWorker);

            }

            SortableBook sortableBook;
            ObservableList<Book> books;
            books = App.getListOfBooksThatAreBorrowedByThisCustomer(App.customerSelectedByWorker);
            borrowedBooks = FXCollections.observableArrayList();
            for(int i=0; i<books.size(); ++i)
            {
                if(App.wasThisCustomerBorrowedThisBookAtFirst(books.get(i).getId_ksiazka(), App.customerSelectedByWorker))
                {
                    sortableBook = new SortableBook(books.get(i), 0);
                    borrowedBooks.add(sortableBook);
                }
            }
            searchView.setItems(borrowedBooks);


            waitingBooks = FXCollections.observableArrayList();
            books = App.getListOfBooksThatAreBorrowedByThisCustomer(App.customerSelectedByWorker);
            for(int i=0; i<books.size(); ++i)
            {
                if(!App.wasThisCustomerBorrowedThisBookAtFirst(books.get(i).getId_ksiazka(), App.customerSelectedByWorker))
                {
                    sortableBook = new SortableBook(books.get(i), 0);
                    waitingBooks.add(sortableBook);
                }
            }

            waitView.setItems(waitingBooks);
        }

    }

    public void add(ActionEvent actionEvent) {

        long yourmilliseconds = System.currentTimeMillis();
        if(curSelectedWindow == 0)
        {
            App.addNewBorrow(App.customerSelectedByWorker, allBooks.get(curSelectedItem).getId_ksiazka(), App.id_adminWorkerCustomer, String.valueOf(yourmilliseconds));
        }else if(curSelectedWindow == 1)
        {
            App.addNewBorrow(App.customerSelectedByWorker, orderedBooks.get(curSelectedItem).getId_ksiazka(), App.id_adminWorkerCustomer, String.valueOf(yourmilliseconds));
        }else
        {
            System.out.println(" ERROR curSelectedWindow = " + curSelectedWindow);
        }

        if(curSelectedWindow != -1)
        {
            SortableBook sortableBook;
            ObservableList<Book> books;
            books = App.getListOfBooksThatAreBorrowedByThisCustomer(App.customerSelectedByWorker);
            borrowedBooks = FXCollections.observableArrayList();
            for(int i=0; i<books.size(); ++i)
            {
                if(App.wasThisCustomerBorrowedThisBookAtFirst(books.get(i).getId_ksiazka(), App.customerSelectedByWorker))
                {
                    sortableBook = new SortableBook(books.get(i), 0);
                    borrowedBooks.add(sortableBook);
                }

            }
            searchView.setItems(borrowedBooks);


            waitingBooks = FXCollections.observableArrayList();
            books = App.getListOfBooksThatAreBorrowedByThisCustomer(App.customerSelectedByWorker);
            for(int i=0; i<books.size(); ++i)
            {
                if(!App.wasThisCustomerBorrowedThisBookAtFirst(books.get(i).getId_ksiazka(), App.customerSelectedByWorker))
                {
                    sortableBook = new SortableBook(books.get(i), 0);
                    waitingBooks.add(sortableBook);
                }
            }

            waitView.setItems(waitingBooks);

        }

        App.selectAll();

    }


    public void sort(){

        int hash, tmp;
        Author author;


        if( (searchText != null) && !searchText.isEmpty() && ! orderedBooks.isEmpty() && !allBooks.isEmpty())
        {
            for(int i=0; i<orderedBooks.size(); ++i)
            {
                author = App.getAuthorById(orderedBooks.get(i).getId_autor());



                hash = App.levenshteinDistance(orderedBooks.get(i).getTytul(),  searchText);

                if(author != null)
                {
                    System.out.println(" author:  " +  author.getImie() + " " +  author.getNazwisko());
                    tmp = App.levenshteinDistance(author.getImie(), searchText);

                    if(tmp<hash)
                    {
                        hash = tmp;
                    }

                    tmp = App.levenshteinDistance(author.getNazwisko(), searchText);

                    if(tmp<hash)
                    {
                        hash = tmp;
                    }

                }else
                {
                    System.out.println("ERRROR AUTHOR NULL");
                }


                orderedBooks.get(i).setHashValue(hash);
            }



            Comparator<SortableBook> comparator = Comparator.comparingInt(SortableBook::getHashValue);
            FXCollections.sort(orderedBooks, comparator);
            listView2.setItems(orderedBooks);

            for(int i=0; i<allBooks.size(); ++i)
            {
                author = App.getAuthorById(allBooks.get(i).getId_autor());



                hash = App.levenshteinDistance(allBooks.get(i).getTytul(),  searchText);

                if(author != null)
                {
                    System.out.println(" author:  " +  author.getImie() + " " +  author.getNazwisko());
                    tmp = App.levenshteinDistance(author.getImie(), searchText);

                    if(tmp<hash)
                    {
                        hash = tmp;
                    }

                    tmp = App.levenshteinDistance(author.getNazwisko(), searchText);

                    if(tmp<hash)
                    {
                        hash = tmp;
                    }

                }else
                {
                    System.out.println("ERRROR AUTHOR NULL");
                }


                allBooks.get(i).setHashValue(hash);
            }

             comparator = Comparator.comparingInt(SortableBook::getHashValue);
            FXCollections.sort(allBooks, comparator);

            listView1.setItems(allBooks);

        }
    }
}
