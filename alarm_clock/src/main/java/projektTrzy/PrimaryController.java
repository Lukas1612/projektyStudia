package projektTrzy;

import java.io.IOException;
import java.net.URL;
import java.util.EventObject;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;






public class PrimaryController implements Initializable {


    @FXML
    private TextArea hoursArea;
    @FXML
    private TextArea minutesArea;
    @FXML
    private TextArea secondsArea;
    @FXML
    private TextArea dateArea;
    @FXML
    private ListView listView;
    @FXML
    private Button closeButton;


    private int currentSelectedItem = -1;


    @FXML
    public void addAlarm() throws IOException  {
        App.setRoot("secondary");
    }

    @FXML
    public void removeAlarm() throws IOException {
        if(currentSelectedItem != -1)
        {
            ObservableList<Alarm> alarmy;
            alarmy = App.getListOfAlarmy();
            alarmy.remove(currentSelectedItem);
            listView.setItems(alarmy);
        }
    }

    @FXML
    public void closeApp(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }


    private void initListView()
    {
        listView.setItems(App.getListOfAlarmy());

        listView.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Object>() {
                    @Override
                    public void changed(ObservableValue<?> observableValue, Object o, Object t1) {
                        currentSelectedItem = listView.getSelectionModel().getSelectedIndex();

                        System.out.println(t1);
                    }
                });
    }


    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {

        initListView();

        if(App.thread == null)
        {
            App.runnable = new TimeSetter(hoursArea, minutesArea, secondsArea, dateArea);
            App.thread = new Thread(App.runnable);
            App.thread.start();
        }else
        {
                App.runnable.terminate();
                if(App.thread != null)
                {
                    try {
                        App.thread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            App.runnable = new TimeSetter(hoursArea, minutesArea, secondsArea, dateArea);
            App.thread = new Thread(App.runnable);
            App.thread.start();
        }

    }


}
