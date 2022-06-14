package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class bookAddWindowController implements Initializable {

    @FXML
    TextField descriptionField;
    @FXML
    TextField publishingHouseField;
    @FXML
    TextField secondNameField;
    @FXML
    TextField nameField;
    @FXML
    TextField titleField;
    @FXML
    TextField cathegoryField;
    @FXML
    TextField isbnField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void save(ActionEvent actionEvent) {
        if(!descriptionField.getText().isEmpty() && !publishingHouseField.getText().isEmpty() && !secondNameField.getText().isEmpty() && !nameField.getText().isEmpty() && !titleField.getText().isEmpty() && !cathegoryField.getText().isEmpty() && !isbnField.getText().isEmpty())
        {
            App.addNewCategorie(cathegoryField.getText());
            App.addNewPublishingHouse(publishingHouseField.getText());
            App.addNewAuthor(nameField.getText(), secondNameField.getText());


            System.out.println("book added initialized");
            App.addNewBook(isbnField.getText(), App.getIdCategorie(cathegoryField.getText()), titleField.getText(), descriptionField.getText(), App.getAuthorId(nameField.getText(), secondNameField.getText()), App.getIdPublishingHouse(publishingHouseField.getText()));
            //String isbn, int id_kategoria, String tytul, String opis, int id_autor, int id_wydawnictwo

            System.out.println("3333333333333333333333333");
            App.selectAll();
            System.out.println("**********************");

        }
    }

    public void back(ActionEvent actionEvent) throws IOException {
        App.setRoot("workerFirstWindow");
    }
}
