package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class changePassowrdWindowController implements Initializable {

    @FXML
    TextField oldPasswordField;
    @FXML
    TextField newPasswordField;
    @FXML
    TextField newPasswordField2;
    @FXML
    Label message;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void back(ActionEvent actionEvent) throws IOException {
        if(App.adminWorkerCustomer == 1)
        {
            App.setRoot("adminWindow");
        }

        if(App.adminWorkerCustomer == 2)
        {
            App.setRoot("workerFirstWindow");
        }

        if(App.adminWorkerCustomer == 3)
        {
            App.setRoot("customerFirstWindow");
        }
    }

    public void change(ActionEvent actionEvent) {

        if(!oldPasswordField.getText().isEmpty() && !newPasswordField.getText().isEmpty() && !newPasswordField2.getText().isEmpty())
        {
            if(newPasswordField2.getText().equals(newPasswordField.getText()))
            {
                if(App.tryIfPasswordIsCorrect(App.id_adminWorkerCustomer, oldPasswordField.getText(), App.adminWorkerCustomer))
                {
                    App.changePassword(App.id_adminWorkerCustomer, newPasswordField.getText(), App.adminWorkerCustomer);
                }else
                {
                    message.setText("old password is incorrect");
                }

            }else
            {
                message.setText("passwords are not equal");
            }
        }else
        {
            message.setText("fill up all fields");
        }
    }

}
