package sample;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

public class SecondaryController implements Initializable {

    @FXML private TextField textField;

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }

    @FXML
    protected void clearField(ActionEvent event) {
        textField.setText("");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        textField.setText("Wpisz tekst");
    }
}