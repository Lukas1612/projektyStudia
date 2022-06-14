package sample;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class customerFirstWindowController implements Initializable {
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void search(ActionEvent actionEvent) throws IOException {
        App.setRoot("customerOrderWindow");
    }

    public void myBooks(ActionEvent actionEvent) throws IOException {
        App.setRoot("borrowedBooksWindow");
    }

    public void back(ActionEvent actionEvent) throws IOException {
        App.setRoot("logginWindow");
    }

    public void changePassword(ActionEvent actionEvent) throws IOException {
        App.setRoot("changePasswordWindow");
    }
}
