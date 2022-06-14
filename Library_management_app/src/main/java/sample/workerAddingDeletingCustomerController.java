package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class workerAddingDeletingCustomerController implements Initializable {

    @FXML
    ListView listView;
    @FXML
    TextField loginField;
    @FXML
    TextField passwordField;
    @FXML
    TextField passwordField2;
    @FXML
    Label message;

    int curSelectedItem = 0;
    ObservableList<Customer> customers;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        customers= App.getListOfCustomers();
        listView.setItems(customers);

        listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                curSelectedItem  = listView.getSelectionModel().getSelectedIndex();
            }
        });

    }

    public void add(ActionEvent actionEvent) {

        if(!loginField.getText().isEmpty() && !passwordField.getText().isEmpty() && !passwordField2.getText().isEmpty())
        {
            if(passwordField.getText().equals(passwordField2.getText()))
            {
                if(!App.tryIfLogginAlreadyExist(loginField.getText(), 3))
                {
                    App.addNewCustomer(loginField.getText(), passwordField.getText());
                    message.setText("new worker was added to datebase");
                    customers= App.getListOfCustomers();
                    listView.setItems(customers);
                }else
                {
                    message.setText("login " + loginField.getText() + " jest juz zajety");
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

    public void delate(ActionEvent actionEvent) {
        if(!customers.isEmpty())
        {
            App.deleteCustomer(customers.get(curSelectedItem).getLogin());
            customers.remove(curSelectedItem);
            listView.setItems(customers);
        }

    }

    public void back(ActionEvent actionEvent) throws IOException {
        App.setRoot("workerFirstWindow");
    }
}
