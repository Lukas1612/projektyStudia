package projektTrzy;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.FileChooser;


public class SecondaryController implements Initializable {

    @FXML
    TextField ringNameTextField;
    @FXML
    Slider hourSlider;
    @FXML
    Slider minuteSlider;
    @FXML
    Slider volumeSlider;
    @FXML
    Spinner hourSpinner;
    @FXML
    Spinner minuteSpinner;
    @FXML
    Spinner volumeSpinner;
    @FXML
    CheckBox repeatCheckBox;
    @FXML
    Button saveButton;
    @FXML
    DatePicker datePicker;




    private Alarm alarm= null;
    private File plikDzwonka = null;
    private Date date = null;
    private LocalDate localDate = null;
    private int hour;
    private int minute;
    private int glosnoscDzwonka;


    protected void setSliders() {
        hourSlider.setMin(0);
        hourSlider.setMax(24);
        hourSlider.setValue(0);
        hourSlider.setShowTickLabels(false);
        hourSlider.setShowTickMarks(false);
        hourSlider.setMajorTickUnit(1);
        hourSlider.setMinorTickCount(1);
        hourSlider.setBlockIncrement(1);


        hourSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                                Number oldValue, Number newValue) {

                hourSpinner.getValueFactory().setValue(newValue.intValue());
                hour = newValue.intValue();
            }
        });


        minuteSlider.setMin(0);
        minuteSlider.setMax(60);
        minuteSlider.setValue(0);
        minuteSlider.setShowTickLabels(false);
        minuteSlider.setShowTickMarks(false);
        minuteSlider.setMajorTickUnit(1);
        minuteSlider.setMinorTickCount(1);
        minuteSlider.setBlockIncrement(1);


        minuteSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                                Number oldValue, Number newValue) {

                minuteSpinner.getValueFactory().setValue(newValue.intValue());
                minute = newValue.intValue();
            }
        });

        volumeSlider.setMin(10);
        volumeSlider.setMax(100);
        volumeSlider.setValue(50);
        volumeSlider.setShowTickLabels(false);
        volumeSlider.setShowTickMarks(false);
        volumeSlider.setMajorTickUnit(1);
        volumeSlider.setMinorTickCount(1);
        volumeSlider.setBlockIncrement(1);


        volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                                Number oldValue, Number newValue) {

                volumeSpinner.getValueFactory().setValue(newValue.intValue());
            }
        });

    }

    protected void setSpinners() {
        hourSpinner.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                                Number oldValue, Number newValue) {

                hourSlider.setValue(newValue.intValue());
            }
        });

        minuteSpinner.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                                Number oldValue, Number newValue) {

                minuteSlider.setValue(newValue.intValue());
            }
        });



        volumeSpinner.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable,
                                Number oldValue, Number newValue) {

                volumeSlider.setValue(newValue.intValue());
                glosnoscDzwonka = newValue.intValue();
            }
        });
    }


    @FXML
    private void saveAlarm() throws IOException {


        date = new Date(localDate.getYear() - 1900, localDate.getMonthValue(), localDate.getDayOfMonth(), hour, minute);


        if(repeatCheckBox.isSelected())
        {
            alarm = new cyclicAlarm(date, plikDzwonka, glosnoscDzwonka);
        }else
        {
             alarm = new disposableAlarm(date, plikDzwonka, glosnoscDzwonka);
        }

        System.out.println(alarm);
        App.dodajBudzik(alarm);
        App.setRoot("primary");
    }

    @FXML
    private void anuluj() throws IOException
    {
        App.setRoot("primary");
    }

    @FXML
    private void otworzPlikAction() throws IOException
    {
        // Tworzymy kontrolkę (okienko) służącą do wybierania pliku
        FileChooser fileChooser = new FileChooser();
        // Tytuł okienka
        fileChooser.setTitle("Otwórz Plik");
        // Dodajemy filtr rodzaju pliku - tu plików tsv
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Pliki ", "*.mp3", "*.txt")
        );
        // Pokaż okno
        plikDzwonka = fileChooser.showOpenDialog(ringNameTextField.getScene().getWindow());


        if (plikDzwonka != null) {
            saveButton.setDisable(false);
            ringNameTextField.setText(plikDzwonka.getName());
        }
    }

    @FXML
    private void setDate() throws IOException
    {
        localDate = datePicker.getValue();
    }

    @Override
    public void initialize (URL url, ResourceBundle resourceBundle)
    {

        glosnoscDzwonka = 0;
        minute = 0;
        hour = 0;
        localDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        datePicker.getEditor().setText(localDate.format(formatter));


        setSliders();
        setSpinners();
       // setCheckBoxes();

    }

}