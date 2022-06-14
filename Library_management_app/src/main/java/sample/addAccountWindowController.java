package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class addAccountWindowController implements Initializable {

    @FXML
    TextField logginField;
    @FXML
    TextField passwordField;
    @FXML
    TextField password2Field;
    @FXML
    Label message;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void add(ActionEvent actionEvent) {
        if(!logginField.getText().isEmpty() && !password2Field.getText().isEmpty() && !passwordField.getText().isEmpty())
        {
            if(passwordField.getText().equals(password2Field.getText()))
            {
                if(!App.tryIfLogginAlreadyExist(logginField.getText(), 2))
                {
                    App.addNewWorker(logginField.getText(), passwordField.getText());
                    message.setText("new worker was added to datebase");
                }else
                {
                    message.setText("login " + logginField.getText() + " jest juz zajety");
                }

            }else
            {
                message.setText("passwords are not equal");
            }
        }else
        {
            message.setText("fill all fields");
        }
    }

    public void back(ActionEvent actionEvent) throws IOException {
        App.setRoot("adminWindow");
    }

    public void save(ActionEvent actionEvent) {
    }
}
