package projektTrzy;


import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;


public class App extends Application {

    public static TimeSetter runnable = null;
    public static Thread thread = null;
    private static Scene scene;
    private static ObservableList<Alarm> alarmy;

    static void dodajBudzik(Alarm alarm)
    {
        alarmy.add(alarm);
    }

    static ObservableList<Alarm> getListOfAlarmy()
    {
        return alarmy;
    }

    @Override
    public void start(Stage stage) throws IOException {
        alarmy =  FXCollections.observableArrayList();
        scene = new Scene(loadFXML("primary").load(), 630, 400);
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml).load());
    }


    private static FXMLLoader loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader;
    }

    @Override
    public void stop() throws IOException {

        if (thread != null)
        {
            runnable.terminate();
            if(thread != null)
            {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }


    public static void main(String[] args) {
        launch();
    }

}