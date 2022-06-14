package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class customerSelectorWidnowConroller  implements Initializable {

    @FXML
    ListView listView;

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

    public void selectCustomer(ActionEvent actionEvent) throws IOException {
        if(curSelectedItem != -1)
        {
            App.customerSelectedByWorker = customers.get(curSelectedItem).getID();
            System.out.println("App.adminWorkerCustomer " + App.adminWorkerCustomer + " App.customerSelectedByWorker " + App.customerSelectedByWorker);
            App.setRoot("borrowReturnBookWindow");
        }

    }


    public void back(ActionEvent actionEvent) throws IOException {
        App.setRoot("workerFirstWindow");
    }
}
