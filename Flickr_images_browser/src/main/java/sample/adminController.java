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

public class adminController implements Initializable {

    @FXML
    private ListView listView;

    private ObservableList<User> users;
    private User user;
    private int  currentSelectedItem;



    @Override
    public void initialize(URL location, ResourceBundle resources) {

        users = App.selectAllUsers();
        listView.setItems(users);

        listView.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Object>() {
                    @Override
                    public void changed(ObservableValue<?> observableValue, Object o, Object t1) {
                        currentSelectedItem = listView.getSelectionModel().getSelectedIndex();

                    }
                });

    }

    public void adminLoggin(ActionEvent actionEvent) throws IOException {
        App.setRoot("logginWindow");
    }

    public void loggin(ActionEvent actionEvent) throws IOException {

       String loggin, password;
       loggin = users.get(currentSelectedItem).getLoggin();
       password = users.get(currentSelectedItem).getPassword();

        if(App.checkMatchingLogginAndPassword(loggin, password))
        {
            App.currentUsserName=loggin;
            App.setRoot("primary");
        }
    }

    public void delete(ActionEvent actionEvent) {

        String loggin;
        loggin = users.get(currentSelectedItem).getLoggin();
        App.deleteUser(loggin);
        users.remove(currentSelectedItem);
        listView.setItems(users);
    }
}
