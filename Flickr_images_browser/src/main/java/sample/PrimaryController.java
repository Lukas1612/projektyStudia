package sample;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;


import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.json.JSONObject;
import netscape.javascript.JSObject;
import javafx.concurrent.Worker.State;


public class PrimaryController implements Initializable {

    @FXML
    Button searchButton;
    @FXML
    Button loadButton;
    @FXML
    ProgressIndicator progressIndicator;
    @FXML
    Tab browseTab;
    @FXML
    Tab browseTab2;
    @FXML
    CheckBox squareCheckBox;
    @FXML
    CheckBox thumbnailCheckBox;
    @FXML
    CheckBox smallCheckBox;
    @FXML
    CheckBox mediumCheckBox;
    @FXML
    CheckBox largeCheckBox;
    @FXML
    CheckBox x_largeCheckBox;
    @FXML
    public TabPane tabPane;
    @FXML
    TextField searchTextField;
    @FXML
    public WebView webView;
    public WebEngine browser;

    public WebView webView2;
    public WebEngine browser2;

    Worker<Void> worker;
    Worker<Void> worker2;

    private String simpleGaleryScriptHtml;

    boolean searchStarted = false;


    private Thread searchThread = null;
    private SearchRunnable searchRunnable = null;

    //ArrayList<String> chosenPhotosUrlAdreses;
    private JavaConnector javaConnector;
   // private JSObject javascriptConnector;



    @FXML
    private void switchToSecondary() throws IOException {
       // App.setRoot("secondary");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        browser = webView.getEngine();
        worker = browser.getLoadWorker();

        browser2 = webView2.getEngine();
        worker2 = browser2.getLoadWorker();
        browseTab.setDisable(true);


        worker.stateProperty().addListener(new ChangeListener<State>() {


            @Override
            public void changed(ObservableValue<? extends State> observable, State oldValue, State newValue) {


                if(newValue == State.RUNNING)
                {
                    progressIndicator.progressProperty().bind(worker.progressProperty());
                }

                if(newValue == Worker.State.SUCCEEDED)
                {
                    browseTab.setDisable(false);
                    searchButton.setDisable(false);
                    loadButton.setDisable(false);
                    searchStarted = false;
                    progressIndicator.progressProperty().unbind();
                }


            }
        });


        browseTab2.setDisable(true);

        worker2.stateProperty().addListener(new ChangeListener<State>() {


            @Override
            public void changed(ObservableValue<? extends State> observable, State oldValue, State newValue) {


                if(newValue == State.RUNNING)
                {
                    browseTab2.setDisable(true);
                }

                if(newValue == Worker.State.SUCCEEDED)
                {
                    browseTab2.setDisable(false);
                }


            }
        });




        progressIndicator.setProgress(0.0);

         //chosenPhotosUrlAdreses = new ArrayList<String>();
        javaConnector = new JavaConnector();

       browser.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
           if (Worker.State.SUCCEEDED == newValue) {
               // set an interface object named 'javaConnector' in the web engine's page

               //************
               JSObject window = (JSObject) browser.executeScript("window");
               //webEngine.executeScript("window") would in fact return the JavaScript Window object.
               //************
               //************
               window.setMember("javaConnector", javaConnector);
               //Równoważna do "this.name = value" w JavaScript.
               //JSObject.setMember(String name,Object value)
               //************

               //javascriptConnector = (JSObject) browser.executeScript("getJsConnector()");
               //wykonanie skrypu w javascript
               //************

           }
       });

        browser2.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (Worker.State.SUCCEEDED == newValue) {
                // set an interface object named 'javaConnector' in the web engine's page

                //************
                JSObject window = (JSObject) browser2.executeScript("window");
                //webEngine.executeScript("window") would in fact return the JavaScript Window object.
                //************
                //************
                window.setMember("javaConnector", javaConnector);
                //Równoważna do "this.name = value" w JavaScript.
                //JSObject.setMember(String name,Object value)
                //************

                //javascriptConnector = (JSObject) browser.executeScript("getJsConnector()");
                //wykonanie skrypu w javascript
                //************

            }
        });

        StringScriptBuilder stringScriptBuilder =  new StringScriptBuilder();
        App.loadPhotosFromDatebase();
        browser2.loadContent(stringScriptBuilder.simpleGaleryHtmlFromArrayListOfUrls_2(App.chosenPhotosUrlAdreses));
    }


    public void search(ActionEvent actionEvent){

        if(!searchStarted)
        {
            if(!searchTextField.getText().isEmpty())
            {
                searchStarted = true;
                searchButton.setDisable(true);
                
                progressIndicator.setProgress(0.0);
                String searchText = searchTextField.getText() ;
                searchText = searchText.replaceAll(" ", "+");

                searchRunnable = new SearchRunnable(progressIndicator, searchText, squareCheckBox.isSelected(), thumbnailCheckBox.isSelected(), smallCheckBox.isSelected(), mediumCheckBox.isSelected(), largeCheckBox.isSelected());
                searchThread = new Thread(searchRunnable);

                searchThread.start();
            }
        }

    }


    @FXML
    public void event(Event event){
        if(!searchStarted)
        {
            progressIndicator.setProgress(0);
        }

    }

    @FXML
    public void event2(Event event) {
    }

    public void load(ActionEvent actionEvent) {

        if(searchRunnable != null && progressIndicator.getProgress() == 1)
        {
            browseTab.setDisable(true);
            loadButton.setDisable(true);
            progressIndicator.setProgress(0.0);
            simpleGaleryScriptHtml = searchRunnable.getResult();
            browser.loadContent(simpleGaleryScriptHtml);
        }
    }

    public void logout(ActionEvent actionEvent) throws IOException {
        App.saveData();

        if(App.admin)
        {
            App.setRoot("adminWindow");
        }else
        {
            App.setRoot("logginWindow");
        }

    }

    public class JavaConnector {
        /**
         * called when the JS side wants a String to be converted.
         */
        StringScriptBuilder stringScriptBuilder = new StringScriptBuilder();
        int i;

        public void takeUrlAdresOfPhoto(String value){
            if(null != value && !isURLAlreadyInArray(value))
            {
                App.chosenPhotosUrlAdreses.add(value);
                browser2.loadContent(stringScriptBuilder.simpleGaleryHtmlFromArrayListOfUrls_2(App.chosenPhotosUrlAdreses));

            }
        }
        public void deletePhotoFromArrays(String n){
            if(n != null)
            {
                i = Integer.valueOf(n);
                if(i>=0)
                {
                    App.chosenPhotosUrlAdreses.remove(i);
                    browser2.loadContent(stringScriptBuilder.simpleGaleryHtmlFromArrayListOfUrls_2(App.chosenPhotosUrlAdreses));
                }else
                {
                    System.out.println(n);
                    System.out.println("photos number not found");

                }
            }

        }
    }

    boolean isURLAlreadyInArray(String url)
    {
        if(!App.chosenPhotosUrlAdreses.isEmpty())
        {
            for(int i=0; i< App.chosenPhotosUrlAdreses.size(); ++i)
            {
                if( App.chosenPhotosUrlAdreses.get(i).equals(url))
                {
                    return true;
                }
            }
        }

       return false;
    }



    void getUrl()
    {
        System.out.println(App.chosenPhotosUrlAdreses);
    }

}
