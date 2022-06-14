package sample;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class workerFirstWindowController implements Initializable {
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void add(ActionEvent actionEvent) throws IOException {
        App.setRoot("bookAddWindow");
    }

    public void delete(ActionEvent actionEvent) throws IOException {
        App.setRoot("deleteBookWindow");
    }

    public void addCustomer(ActionEvent actionEvent) throws IOException {
        App.setRoot("workerAddingDeletingCustomerPanel");
    }

    public void borrowBook(ActionEvent actionEvent) throws IOException {
        App.setRoot("customerSelectorWindow");
    }

    public void back(ActionEvent actionEvent) throws IOException {
        App.setRoot("logginWindow");
    }

    public void changePassword(ActionEvent actionEvent) throws IOException {
        App.setRoot("changePasswordWindow");
    }
}
