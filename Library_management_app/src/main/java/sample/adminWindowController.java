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

public class adminWindowController implements Initializable {


    @FXML
    ListView listView;
    protected ObservableList<Worker> workers;

    int currentSelectedItem=0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        workers = App.getListOfWorkers();
        listView.setItems(workers);

        listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                currentSelectedItem = listView.getSelectionModel().getSelectedIndex();
            }
        });

    }

    public void add(ActionEvent actionEvent) throws IOException {
        App.setRoot("addAccountWindow");
    }

    public void delete(ActionEvent actionEvent) {

        if(!workers.isEmpty())
        {
            App.deleteWorker(workers.get( currentSelectedItem ).getLogin());
            workers.remove(currentSelectedItem);
            listView.setItems(workers);

        }

    }

    public void back(ActionEvent actionEvent) throws IOException {
        App.setRoot("logginWindow");
    }

    public void changePassword(ActionEvent actionEvent) throws IOException {
        App.setRoot("changePasswordWindow");
    }
}
