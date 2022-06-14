package sample;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class logginWindowController implements Initializable {

    protected int adminWorkerCustomer;

    @FXML
    TextField logginField;
    @FXML
    TextField passwordField;
    @FXML
    MenuButton menuButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        adminWorkerCustomer = 2;

    }

    public void setCustomer(ActionEvent actionEvent) {

        adminWorkerCustomer=3;
        menuButton.setText("customer");
    }

    public void setWorker(ActionEvent actionEvent) {
        adminWorkerCustomer=2;
        menuButton.setText("worker");
    }

    public void setAdmin(ActionEvent actionEvent) {
        adminWorkerCustomer=1;
        menuButton.setText("admin");
    }

    public void loggin(ActionEvent actionEvent) throws IOException {

        if(!logginField.getText().isEmpty() && !passwordField.getText().isEmpty())
        {

            if(App.tryLogginPassword(logginField.getText(), passwordField.getText(), adminWorkerCustomer))
            {
                App.adminWorkerCustomer=this.adminWorkerCustomer;

                if(adminWorkerCustomer==1)
                {
                    App.setRoot("adminWindow");
                    System.out.println("adminWindow " + App.id_adminWorkerCustomer);

                }
                if(adminWorkerCustomer==2)
                {

                    System.out.println("worker " + App.id_adminWorkerCustomer);
                    App.setRoot("workerFirstWindow");
                }
                if(adminWorkerCustomer==3)
                {

                    System.out.println("customer " + App.id_adminWorkerCustomer);
                    App.setRoot("customerFirstWindow");
                }
            }
        }
    }
}
