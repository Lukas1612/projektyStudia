package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class newAccountController implements Initializable {

    @FXML
    Label statementLabel;
    @FXML
    TextField logginField;
    @FXML
    TextField passwordField;
    @FXML
    TextField passwordField2;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void createAccount(ActionEvent actionEvent) throws IOException {

        if(passwordField.getText().equals(passwordField2.getText()) && !passwordField.getText().isEmpty() && !logginField.getText().isEmpty())
        {
            if(!App.checkIfLogginAlreadyExists(logginField.getText()))
            {
                App.insertNewUser(logginField.getText(),passwordField.getText());

                App.setCurrentUsserName(logginField.getText());
                App.createNewTable(App.getCurrentUsserName());

                App.admin = false;
                App.setRoot("primary");
            }else
            {
                statementLabel.setText("login " + "\"" + logginField.getText() +  "\"" +  " already exist");
            }

        }else
            if (passwordField2.getText().isEmpty() || passwordField.getText().isEmpty() || logginField.getText().isEmpty())
              {
                  statementLabel.setText("fill all the fields");
              } else
                  if(!passwordField2.getText().equals(passwordField.getText()))
                  {
                      statementLabel.setText("passwords are not the same");
                  }





    }

    public void comeBack(ActionEvent actionEvent) throws IOException {

        App.setRoot("logginWindow");
    }
}
