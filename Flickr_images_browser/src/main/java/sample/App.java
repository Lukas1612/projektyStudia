package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.Stage;

import java.sql.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    //login and choosen photos by user
    protected static String currentUsserName;
    protected static ArrayList<String> chosenPhotosUrlAdreses;

    protected static Boolean admin;


    public static void setCurrentUsserName(String currentUsserName) {
        App.currentUsserName = currentUsserName;
    }

    public static String getCurrentUsserName() {
        return currentUsserName;
    }

    @Override
    public void start(Stage stage) throws IOException {
        admin = false;
        chosenPhotosUrlAdreses = new ArrayList<String>();
        initializeAccountsTable();
        scene = new Scene(loadFXML("logginWindow"), 650, 400);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop(){
        saveData();
    }

    //delate data from datebase and write data from current sesion
    public static void saveData()
    {
        deleteAll();

        for(int i=0; i<chosenPhotosUrlAdreses.size(); ++i)
        {
            insert(chosenPhotosUrlAdreses.get(i));
        }
    }


    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    private static Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:C://Users//laseraptor//Desktop//PrzykladFX//database//users_photos_datbase.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    private static Connection connectUsers() {
        // SQLite connection string
        String url = "jdbc:sqlite:C://Users//laseraptor//Desktop//PrzykladFX//database//users_data_datebase.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public static void insert(String url) {
        String sql = "INSERT INTO " + currentUsserName + "(url) VALUES(?)";

        try (Connection conn = App.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, url);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void createNewTable(String userLoggin) {
        // SQLite connection string
        String url = "jdbc:sqlite:C://Users//laseraptor//Desktop//PrzykladFX//database//users_photos_datbase.db";

        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS " + userLoggin + " (\n"
                + " url text PRIMARY KEY\n"
                + ");";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void loadPhotosFromDatebase(){

        if(!chosenPhotosUrlAdreses.isEmpty())
        {
            chosenPhotosUrlAdreses.clear();
        }

        String sql = "SELECT url FROM " + currentUsserName;

        try (Connection conn = App.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            while (rs.next()) {
                chosenPhotosUrlAdreses.add(rs.getString("url"));
            }
        } catch (SQLException e) {
            System.out.println("tutaj");
        }
    }


    //delate current data from database
    public static void deleteAll() {
        String sql = "DELETE FROM " + currentUsserName;

        try (Connection conn = App.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void initializeAccountsTable() {
        // SQLite connection string
        String url = "jdbc:sqlite:C://Users//laseraptor//Desktop//PrzykladFX//database//users_data_datebase.db";

        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS Users (\n"
                + " loggin text,\n"
                + " password text\n"
                + ");";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static boolean checkMatchingLogginAndPassword(String loggin, String Password){
        String sql = "SELECT loggin, password FROM  Users";

        try (Connection conn = App.connectUsers();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            while (rs.next()) {
                if(rs.getString("loggin").equals(loggin)  && rs.getString("password").equals(Password))
                {
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println("tutaj");
        }

        return false;
    }

    public static boolean checkIfLogginAlreadyExists(String loggin){
        String sql = "SELECT loggin FROM  Users";

        try (Connection conn = App.connectUsers();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            while (rs.next()) {
                if(rs.getString("loggin").equals(loggin))
                {
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println("tutaj");
        }

        return false;
    }

    public static void insertNewUser(String loggin, String password) {
        String sql = "INSERT INTO Users(loggin,password) VALUES(?,?)";

        try (Connection conn = App.connectUsers();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, loggin);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public static void deleteUser(String loggin) {
        String sql = "DELETE FROM Users WHERE loggin = ?";


        try (Connection conn = App.connectUsers();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, loggin);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        sql = "DROP TABLE " + loggin;


        try (Connection conn = App.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static ObservableList<User> selectAllUsers(){
        ObservableList<User> users =  FXCollections.observableArrayList();

        User user;

        String sql = "SELECT loggin, password  FROM Users";

        try (Connection conn = App.connectUsers();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            while (rs.next()) {
                user = new User(rs.getString("loggin"),  rs.getString("password"));
                users.add(user);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return users;
    }

    public static void main(String[] args) {
        launch();
    }

}