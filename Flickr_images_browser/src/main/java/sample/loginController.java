package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class loginController implements Initializable {

    @FXML
    TextField logginField;
    @FXML
    TextField passwordField;
    @FXML
    Label statementLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void loggin(ActionEvent actionEvent) throws IOException {
        if(App.checkMatchingLogginAndPassword(logginField.getText(), passwordField.getText()))
        {
            App.admin = false;
            App.currentUsserName=logginField.getText();
            App.setRoot("primary");
        }else
        {
            statementLabel.setText("invalid login or password");
        }
    }

    public void createAccount(ActionEvent actionEvent) throws IOException {

        App.setRoot("newAccountWindow");
    }

    public void deleteAccount(ActionEvent actionEvent) {
        if(App.checkMatchingLogginAndPassword(logginField.getText(), passwordField.getText()))
        {
            App.deleteUser(logginField.getText());
        }else
        {
            statementLabel.setText("invalid login or password");
        }

    }

    public void adminLoggin(ActionEvent actionEvent) throws IOException {

        if(logginField.getText().equals("admin") && passwordField.getText().equals("12345"))
        {
            App.admin = true;
            App.setRoot("adminWindow");
        }

    }
}
