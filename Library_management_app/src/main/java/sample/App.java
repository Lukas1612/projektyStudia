package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.*;

import java.io.IOException;
import java.util.ArrayList;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    //who is logged now
    //admin=0 worker=1 customer=2
    protected static int adminWorkerCustomer;
    protected static int id_adminWorkerCustomer;
    protected static int customerSelectedByWorker;

    @Override
    public void start(Stage stage) throws IOException {

        //App.deleteTableWypozyczenia();
        initializeTables();

        //insertAdmin("admin", "12345");

        System.out.println("levi " + levenshteinDistance("my book is large", "my book is"));

        scene = new Scene(loadFXML("logginWindow"), 640, 400);
        stage.setScene(scene);
        stage.show();
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
        String url = "jdbc:sqlite:C://Users/laseraptor/Desktop/javaFX _tmp_2_1_11/PrzykladFX2/DB/biblioteka.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public static int levenshteinDistance (CharSequence lhs, CharSequence rhs) {
        int len0 = lhs.length() + 1;
        int len1 = rhs.length() + 1;

        // the array of distances
        int[] cost = new int[len0];
        int[] newcost = new int[len0];

        // initial cost of skipping prefix in String s0
        for (int i = 0; i < len0; i++) cost[i] = i;

        // dynamically computing the array of distances

        // transformation cost for each letter in s1
        for (int j = 1; j < len1; j++) {
            // initial cost of skipping prefix in String s1
            newcost[0] = j;

            // transformation cost for each letter in s0
            for(int i = 1; i < len0; i++) {
                // matching current letters in both strings
                int match = (lhs.charAt(i - 1) == rhs.charAt(j - 1)) ? 0 : 1;

                // computing cost for each transformation
                int cost_replace = cost[i - 1] + match;
                int cost_insert  = cost[i] + 1;
                int cost_delete  = newcost[i - 1] + 1;

                // keep minimum cost
                newcost[i] = Math.min(Math.min(cost_insert, cost_delete), cost_replace);
            }

            // swap cost/newcost arrays
            int[] swap = cost; cost = newcost; newcost = swap;
        }

        // the distance is the cost for transforming all letters in both strings
        return cost[len0 - 1];
    }

    public static void createNewTable() {
        // SQLite connection string
        String url = "jdbc:sqlite:C://Users/laseraptor/Desktop/javaFX _tmp_2_1_11/PrzykladFX2/DB/biblioteka.db";

        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS warehouses (\n"
                + "	id integer PRIMARY KEY,\n"
                + "	name text NOT NULL,\n"
                + "	capacity real\n"
                + ");";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insert(String name, double capacity) {
        String sql = "INSERT INTO warehouses(name,capacity) VALUES(?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setDouble(2, capacity);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

   /* String sql = "CREATE TABLE IF NOT EXISTS wypozyczenia (\n"
            + "	id_wypozycenia integer PRIMARY KEY,\n"
            + "	id_czytelnik integer,\n"
            + "	id_ksiazka integer,\n"
            + "	data_wypozyczenia text,\n"
            + "	id_pracownik_wypozyczenie integer,\n"
            + "	data_oddania text,\n"
            + "	id_pracownik_oddanie integer,\n"
            + "unique (id_czytelnik, id_ksiazka)\n"
            + ");";*/

    public static void selectAll(){

        String sql = "SELECT * FROM wypozyczenia";

        int i=0;

        try (Connection conn = App.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            System.out.println("search:    ");
            while (rs.next()) {
                System.out.println(i + " id_wypozycenia " +  rs.getInt("id_wypozycenia") + " id_czytelnik  " + rs.getInt("id_czytelnik") + " id_ksiazka " + rs.getInt("id_ksiazka") + " data_wypozyczenia " + rs.getString("data_wypozyczenia") + "	id_pracownik_wypozyczenie " + rs.getInt("id_pracownik_wypozyczenie") + " data_oddania " + rs.getString("data_oddania") + " id_pracownik_oddanie " + rs.getInt("id_pracownik_oddanie"));
                ++i;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void deleteAll(){

        String sql = "DELETE FROM zamowienia";


        System.out.println("deleting:   ");

        try (Connection conn = App.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){


        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    //loggin window match loggin and password
    public static boolean tryLogginPassword(String login, String haslo, int adminWorkerCustomer){

        String sql;
        if(adminWorkerCustomer==1)
        {
            sql = "SELECT login, haslo, id_admin FROM admin";
        }else if(adminWorkerCustomer==2)
        {
            sql = "SELECT login, haslo, id_pracownik FROM pracownicy";
        }else
        {
            sql = "SELECT login, haslo, id_czytelnik FROM czytelnicy";
        }


        try (Connection conn = App.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){


            // loop through the result set
            while (rs.next()) {

                if(login.equals( rs.getString("login")) && haslo.equals(rs.getString("haslo")))
                {
                    if(adminWorkerCustomer==1)
                    {
                        id_adminWorkerCustomer=rs.getInt("id_admin");
                        System.out.println("you are logged as admin");
                    }
                    if(adminWorkerCustomer==2)
                    {
                        id_adminWorkerCustomer=rs.getInt("id_pracownik");
                        System.out.println("you are logged as worker");
                    }

                    if(adminWorkerCustomer==3)
                    {
                        id_adminWorkerCustomer=rs.getInt("id_czytelnik");
                        System.out.println("you are logged as customer");
                    }
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return false;
    }

    public static void changePassword(int id, String haslo, int adminWorkerCustomer){

        String sql;
        if(adminWorkerCustomer==1)
        {
            sql = "UPDATE admin SET haslo = ? "
                    + "WHERE id_admin = ?";

        }else if(adminWorkerCustomer==2)
        {
            sql = "UPDATE pracownicy SET haslo = ? "
                    + "WHERE id_pracownik = ?";
        }else
        {
            sql = "UPDATE czytelnicy SET haslo = ? "
                    + "WHERE id_czytelnik = ?";
        }


        try (Connection conn = App.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, haslo);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public static boolean tryIfPasswordIsCorrect(int id, String haslo, int adminWorkerCustomer){

        boolean result = false;
        String sql;
        if(adminWorkerCustomer==1)
        {
            sql = "SELECT login, haslo, id_admin FROM admin WHERE id_admin = ? ";
        }else if(adminWorkerCustomer==2)
        {
            sql = "SELECT login, haslo, id_pracownik FROM pracownicy WHERE id_pracownik = ? ";
        }else
        {
            sql = "SELECT login, haslo, id_czytelnik FROM czytelnicy WHERE id_czytelnik = ? ";
        }

        try (Connection conn = App.connect();
             PreparedStatement pstmt  = conn.prepareStatement(sql)){

            // set the value
            pstmt.setInt(1,id);

            //
            ResultSet rs  = pstmt.executeQuery();

            // loop through the result set
            while (rs.next()) {

                if(rs.getString("haslo").equals(haslo))
                {
                    System.out.println("login:  " + rs.getString("login") + "  haslo: " + rs.getString("haslo"));
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return result;
    }

    public static ObservableList<Worker> getListOfWorkers()
    {
        ObservableList<Worker> workers = FXCollections.observableArrayList();
        Worker worker;

        String sql = "SELECT id_pracownik, login, haslo FROM pracownicy";

        try (Connection conn = App.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            while (rs.next()) {
                worker =new Worker(rs.getInt("id_pracownik"), rs.getString("login"), rs.getString("haslo"));
                workers.add(worker);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return workers;
    }



    public void insertAdmin(String login, String haslo) {
        String sql = "INSERT INTO admin(login,haslo) VALUES(?,?)";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, login);
            pstmt.setString(2, haslo);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void addNewWorker(String login, String haslo)
    {
        String sql = "INSERT INTO pracownicy(login,haslo) VALUES(?,?)";

        try (Connection conn = App.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, login);
            pstmt.setString(2, haslo);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public static void deleteWorker(String login)
    {
        String sql = "DELETE FROM pracownicy WHERE login = ?";

        try (Connection conn = App.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setString(1, login);
            // execute the delete statement
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public static boolean tryIfLogginAlreadyExist(String login, int adminWorkerCustomer){

        String sql;
        if(adminWorkerCustomer==1)
        {
            sql = "SELECT login FROM admin";
        }else if(adminWorkerCustomer==2)
        {
            sql = "SELECT login FROM pracownicy";
        }else
        {
            sql = "SELECT login  FROM czytelnicy";
        }


        try (Connection conn = App.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            while (rs.next()) {

                if(login.equals( rs.getString("login")))
                {
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return false;
    }


    public static void addNewBook(String isbn, int id_kategoria, String tytul, String opis, int id_autor, int id_wydawnictwo)
    {
        String sql = "INSERT INTO ksiazki(isbn, id_kategoria, tytul, opis, id_autor, id_wydawnictwo) VALUES(?,?,?,?,?,?)";

        try (Connection conn = App.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, isbn);
            pstmt.setInt(2, id_kategoria);
            pstmt.setString(3, tytul);
            pstmt.setString(4, opis);
            pstmt.setInt(5, id_autor);
            pstmt.setInt(6, id_wydawnictwo);
            pstmt.executeUpdate();

            System.out.println("book has been added");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public static void deleteBookById(int id) {
        String sql = "DELETE FROM ksiazki WHERE id_ksiazka = ?";

        try (Connection conn = App.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setInt(1, id);
            // execute the delete statement
            pstmt.executeUpdate();

        } catch (SQLException e) {

            System.out.println("deleteBookById   " + e.getMessage());
        }
    }

    public static ObservableList<Book> getListOfBooks()
    {
        ObservableList<Book> books = FXCollections.observableArrayList();
        Book book;

        String sql = "SELECT * FROM ksiazki";

        try (Connection conn = App.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            while (rs.next()) {
                //(int id_ksiazka, String isbn, int id_kategoria, String tytul, String opis, int id_autor, int id_wydawnictwo)
                book =new Book(rs.getInt("id_ksiazka"), rs.getString("isbn"), rs.getInt("id_kategoria"), rs.getString("tytul"), rs.getString("opis"), rs.getInt("id_autor"), rs.getInt("id_wydawnictwo"));
                books.add(book);
               // System.out.println("book: " + book);

                //System.out.println(book);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return books;
    }

    public static ObservableList<Book> getListOfBooksThatAreNotOrderedByThisCustomer(int id_czytelnik)
    {
        ObservableList<Book> books = FXCollections.observableArrayList();
        Book book;

        String sql = "SELECT DISTINCT id_ksiazka, isbn, id_kategoria, tytul, opis, id_autor, id_wydawnictwo " +
                "FROM ksiazki " +
                "WHERE id_ksiazka NOT IN (SELECT id_ksiazka from zamowienia " +
                "                         WHERE id_czytelnik = ? )";

        try (Connection conn = App.connect();
             PreparedStatement pstmt  = conn.prepareStatement(sql)){

            pstmt.setInt(1,id_czytelnik);


            ResultSet rs  = pstmt.executeQuery();


            // loop through the result set
            System.out.println("nie zamowione   ");
            while (rs.next()) {

                System.out.println("nie zamowione   " + rs.getInt( "id_ksiazka"));

                book =new Book(rs.getInt("id_ksiazka"), rs.getString("isbn"), rs.getInt("id_kategoria"), rs.getString("tytul"), rs.getString("opis"), rs.getInt("id_autor"), rs.getInt("id_wydawnictwo"));
                books.add(book);



            }
        } catch (SQLException e) {
            System.out.println("TUTAJ  books by id zamowienia!!!");
            System.out.println(e.getMessage());
        }
        return books;
    }


    public static void addNewCategorie(String nazwa)
    {
        String sql = "INSERT INTO kategorie(nazwa) VALUES(?)";

        try (Connection conn = App.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nazwa);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static int getIdCategorie(String nazwa)
    {
        int id=-1;
        String sql = "SELECT id_kategoria "
                + "FROM kategorie WHERE nazwa = ?";

        try (Connection conn = App.connect();
             PreparedStatement pstmt  = conn.prepareStatement(sql)){

            // set the value
            pstmt.setString(1,nazwa);
            //
            ResultSet rs  = pstmt.executeQuery();

            // loop through the result set
            while (rs.next()) {
                id = rs.getInt("id_kategoria");
                System.out.println("kategoria nazwa= " + nazwa + " id_kategoria= "+ id);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return id;
    }

    public static void deleteCategoryById(int id) {
        String sql = "DELETE FROM kategorie WHERE id_kategoria = ?";

        try (Connection conn = App.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setInt(1, id);
            // execute the delete statement
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public static boolean areDifferentThanThisBooksInThisCathegoryStillExiste(int id_kategoria, int id_ksiazka)
    {
        boolean value = false;

        String sql = "SELECT id_ksiazka "
                + "FROM ksiazki WHERE id_ksiazka != ? AND id_kategoria = ?";

        try (Connection conn = App.connect();
             PreparedStatement pstmt  = conn.prepareStatement(sql)){

            // set the value
            pstmt.setInt(1, id_ksiazka);
            pstmt.setInt(2, id_kategoria);
            //
            ResultSet rs  = pstmt.executeQuery();

            // loop through the result set

            while (rs.next()) {
                System.out.println("---------------");
                System.out.println(" ******************kategoria wciaz istnieje");
                System.out.println(rs.getInt("id_ksiazka"));
                System.out.println(" ******************kategoria wciaz istnieje");
                System.out.println("----------------------");
                value = true;
            }
        } catch (SQLException e) {
            System.out.println("kat   " + e.getMessage());
        }

        return value;
    }

    public static void addNewPublishingHouse(String nazwa)
    {
        String sql = "INSERT INTO wydawnictwa(nazwa) VALUES(?)";

        try (Connection conn = App.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nazwa);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static boolean areDifferentThanThisBooksFomThisPublishingHouseStillExiste(int id_wydawnictwo, int id_ksiazka)
    {
        boolean value = false;

        String sql = "SELECT w1.id_wydawnictwo " +
                "FROM wydawnictwa w1 " +
                "JOIN ksiazki k1 ON k1.id_wydawnictwo = w1.id_wydawnictwo " +
                "WHERE k1.id_ksiazka != ? AND k1.id_wydawnictwo = ? ";

        try (Connection conn = App.connect();
             PreparedStatement pstmt  = conn.prepareStatement(sql)){

            // set the value
            pstmt.setInt(1, id_ksiazka);
            pstmt.setInt(2, id_wydawnictwo);
            //
            ResultSet rs  = pstmt.executeQuery();

            // loop through the result set

            while (rs.next()) {
                System.out.println("---------------");
                System.out.println(" *****************wydawnictow wciaz istnieje");
                System.out.println(rs.getInt("id_wydawnictwo"));
                System.out.println(" ******************wydawnictwo wciaz istnieje");
                System.out.println("----------------------");
                value = true;
            }
        } catch (SQLException e) {
            System.out.println("wyd   " + e.getMessage());
        }

        return value;
    }

    public static void deletePublishingHouseById(int id) {
        String sql = "DELETE FROM wydawnictwa WHERE id_wydawnictwo = ?";

        try (Connection conn = App.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setInt(1, id);
            // execute the delete statement
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }




    public static int getIdPublishingHouse(String nazwa)
    {
        int id=-1;
        String sql = "SELECT id_wydawnictwo "
                + "FROM wydawnictwa WHERE nazwa = ?";

        try (Connection conn = App.connect();
             PreparedStatement pstmt  = conn.prepareStatement(sql)){

            // set the value
            pstmt.setString(1,nazwa);
            //
            ResultSet rs  = pstmt.executeQuery();

            // loop through the result set
            while (rs.next()) {
                id = rs.getInt("id_wydawnictwo");
              //  System.out.println("id= "+ id);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return id;
    }

    public static void addNewAuthor(String imie, String nazwisko)
    {
        String sql = "INSERT INTO autorzy(imie, nazwisko) VALUES(?,?)";

        try (Connection conn = App.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, imie);
            pstmt.setString(2, nazwisko);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public static boolean areDifferentThanThisBooksFromThisAuthorStillExiste(int id_autor, int id_ksiazka)
    {
        boolean value = false;

        String sql = "SELECT a1.id_autor " +
                "FROM autorzy a1 JOIN ksiazki k1 ON a1.id_autor = k1.id_autor " +
                "WHERE k1.id_ksiazka != ? AND a1.id_autor = ? ";

        try (Connection conn = App.connect();
             PreparedStatement pstmt  = conn.prepareStatement(sql)){

            // set the value
            pstmt.setInt(1, id_ksiazka);
            pstmt.setInt(2, id_autor);
            //
            ResultSet rs  = pstmt.executeQuery();

            // loop through the result set

            while (rs.next()) {
                System.out.println("---------------");
                System.out.println(" ******************autor wciaz istnieje");
                System.out.println(rs.getInt("id_autor"));
                System.out.println(" ******************autor wciaz istnieje");
                System.out.println("----------------------");
                value = true;
            }
        } catch (SQLException e) {
            System.out.println("autor  " + e.getMessage());
        }

        return value;
    }

    public static void deleteAuthorById(int id) {
        String sql = "DELETE FROM autorzy WHERE id_autor = ?";

        try (Connection conn = App.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setInt(1, id);
            // execute the delete statement
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static int getAuthorId(String imie, String nazwisko){
        int id = -1;


        String sql = "SELECT id_autor "
                + "FROM autorzy WHERE imie = ? AND nazwisko = ?";

        try (Connection conn = App.connect();
             PreparedStatement pstmt  = conn.prepareStatement(sql)){

            pstmt.setString(1,imie);
            pstmt.setString(2,nazwisko);

            ResultSet rs  = pstmt.executeQuery();


            // loop through the result set
            while (rs.next()) {

                id = rs.getInt("id_autor");

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return id;
    }

    public static Author getAuthorById(int id_autor){
        Author author = null;


        String sql = "SELECT imie, nazwisko "
                + "FROM autorzy WHERE id_autor = ?";

        try (Connection conn = App.connect();
             PreparedStatement pstmt  = conn.prepareStatement(sql)){

            pstmt.setInt(1,id_autor);


            ResultSet rs  = pstmt.executeQuery();


            // loop through the result set
            while (rs.next()) {

                System.out.println("imie: " + rs.getString("imie") + " nazwisko: " + rs.getString("nazwisko") + "  id_autor: " + id_autor );
                author =  new Author(rs.getString("imie"), rs.getString("nazwisko"), id_autor);

            }
        } catch (SQLException e) {
            System.out.println("TUTAJ!!!");
            System.out.println(e.getMessage());
        }
        return author;
    }



    public static void addNewCustomer(String login, String haslo)
    {
        String sql = "INSERT INTO czytelnicy(login,haslo) VALUES(?,?)";

        try (Connection conn = App.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, login);
            pstmt.setString(2, haslo);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public static ObservableList<Customer> getListOfCustomers()
    {
        ObservableList<Customer> customers = FXCollections.observableArrayList();
        Customer customer;

        String sql = "SELECT id_czytelnik, login, haslo FROM czytelnicy";

        try (Connection conn = App.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            while (rs.next()) {
                customer =new Customer(rs.getInt("id_czytelnik"), rs.getString("login"), rs.getString("haslo"));
                customers.add(customer);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return customers;
    }


    public static void deleteCustomer(String login)
    {
        String sql = "DELETE FROM czytelnicy WHERE login = ?";

        try (Connection conn = App.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setString(1, login);
            // execute the delete statement
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }


    public static void addNewOrder(int id_czytelnik, int id_ksiazka)
    {
        String sql = "INSERT INTO zamowienia(id_czytelnik,id_ksiazka) VALUES(?,?)";


        try (Connection conn = App.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id_czytelnik);
            pstmt.setInt(2, id_ksiazka);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }



    public static ObservableList<Book> getListOfBooksFromOrdersByCustomerId(int id_czytelnik)
    {
        ObservableList<Book> books = FXCollections.observableArrayList();
        Book book;

        String sql = "SELECT k1.id_ksiazka, k1.isbn, k1.id_kategoria, k1.tytul, k1.opis, k1.id_autor, k1.id_wydawnictwo  FROM ksiazki k1 " +
                "JOIN zamowienia z1 ON z1.id_ksiazka = k1.id_ksiazka " +
                "WHERE z1.id_czytelnik = ?";



        try (Connection conn = App.connect();
             PreparedStatement pstmt  = conn.prepareStatement(sql)){

            pstmt.setInt(1,id_czytelnik);


            ResultSet rs  = pstmt.executeQuery();


            // loop through the result set
            while (rs.next()) {

                book =new Book(rs.getInt("id_ksiazka"), rs.getString("isbn"), rs.getInt("id_kategoria"), rs.getString("tytul"), rs.getString("opis"), rs.getInt("id_autor"), rs.getInt("id_wydawnictwo"));
                books.add(book);

            }
        } catch (SQLException e) {
            System.out.println("TUTAJ  books by id zamowienia!!!");
            System.out.println(e.getMessage());
        }

        return books;
    }

    public static void deleteOrderByBookAndCustomerId(int id_ksiazka, int id_czytelnik) {
        String sql = "DELETE FROM zamowienia WHERE id_ksiazka = ? AND id_czytelnik = ?";

        try (Connection conn = App.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setInt(1, id_ksiazka);
            pstmt.setInt(2, id_czytelnik);
            // execute the delete statement
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public static void addNewBorrow(int id_czytelnik, int id_ksiazka, int id_pracownik_wypozyczenie, String data_wypozyczenia)
    {
        String sql = "INSERT INTO wypozyczenia(id_czytelnik, id_ksiazka, id_pracownik_wypozyczenie, data_wypozyczenia) VALUES(?,?,?,?)";


        try (Connection conn = App.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id_czytelnik);
            pstmt.setInt(2, id_ksiazka);
            pstmt.setInt(3, id_pracownik_wypozyczenie);
            pstmt.setString(4, data_wypozyczenia);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public static  ObservableList<Book> allNoReturnedBooksNotBorrowedByThisCustomer(int id_czytelnik)
    {


        ObservableList<Book> books = FXCollections.observableArrayList();
        Book book;

        String sql = "SELECT DISTINCT id_ksiazka, isbn, id_kategoria, tytul, opis, id_autor, id_wydawnictwo " +
                "FROM ksiazki " +
                "WHERE id_ksiazka IN (SELECT id_ksiazka from wypozyczenia " +
                "                         WHERE id_czytelnik = ? AND data_oddania IS NULL )"
                +"  INTERSECT "
                +"SELECT DISTINCT id_ksiazka, isbn, id_kategoria, tytul, opis, id_autor, id_wydawnictwo " +
                "FROM ksiazki " +
                "WHERE id_ksiazka IN (SELECT id_ksiazka from wypozyczenia " +
                "                         WHERE id_czytelnik != ? AND data_oddania IS NULL )";

        try (Connection conn = App.connect();
             PreparedStatement pstmt  = conn.prepareStatement(sql)){

            pstmt.setInt(1,id_czytelnik);
            pstmt.setInt(2,id_czytelnik);


            ResultSet rs  = pstmt.executeQuery();


            // loop through the result set
            System.out.println("nie zamowione   ");
            while (rs.next()) {

                System.out.println("nie zamowione   " + rs.getInt( "id_ksiazka"));

                book =new Book(rs.getInt("id_ksiazka"), rs.getString("isbn"), rs.getInt("id_kategoria"), rs.getString("tytul"), rs.getString("opis"), rs.getInt("id_autor"), rs.getInt("id_wydawnictwo"));
                books.add(book);



            }
        } catch (SQLException e) {
            System.out.println("TUTAJ  books by id zamowienia!!!");
            System.out.println(e.getMessage());
        }
        return books;

    }



    public static ObservableList<Book> getListOfBooksThatAreBorrowedByThisCustomer(int id_czytelnik)
    {
        ObservableList<Book> books = FXCollections.observableArrayList();
        Book book;

        String sql = "SELECT DISTINCT id_ksiazka, isbn, id_kategoria, tytul, opis, id_autor, id_wydawnictwo " +
                "FROM ksiazki " +
                "WHERE id_ksiazka IN (SELECT id_ksiazka from wypozyczenia " +
                "                         WHERE id_czytelnik = ? AND data_oddania IS NULL )";

        try (Connection conn = App.connect();
             PreparedStatement pstmt  = conn.prepareStatement(sql)){

            pstmt.setInt(1,id_czytelnik);


            ResultSet rs  = pstmt.executeQuery();


            // loop through the result set
            System.out.println("wypozyczone   ");
            while (rs.next()) {

                System.out.println("wypozyczone   " + rs.getInt( "id_ksiazka"));

                book =new Book(rs.getInt("id_ksiazka"), rs.getString("isbn"), rs.getInt("id_kategoria"), rs.getString("tytul"), rs.getString("opis"), rs.getInt("id_autor"), rs.getInt("id_wydawnictwo"));
                books.add(book);
            }
        } catch (SQLException e) {
            System.out.println("TUTAJ  books by id wypozyczone1!!!");
            System.out.println(e.getMessage());
        }
        return books;
    }

    public static boolean wasThisCustomerBorrowedThisBookAtFirst(int id_ksiazka, int id_czytelnik)
    {


        int id_czyt = 0;
        int id_ksi = 0;
        Long date = 0L;


        String sql = "SELECT DISTINCT data_wypozyczenia, id_ksiazka, id_czytelnik " +
                "FROM wypozyczenia " +
                "WHERE id_ksiazka = ? AND data_oddania IS NULL";

        try (Connection conn = App.connect();
             PreparedStatement pstmt  = conn.prepareStatement(sql)){

            pstmt.setInt(1,id_ksiazka);

            ResultSet rs  = pstmt.executeQuery();


            // loop through the result set
            Long date_tmp;
            System.out.println("wypozyczone   ");
            while (rs.next()) {

               if(id_czyt == 0 && id_ksi == 0 && date == 0L)
               {
                   id_czyt = rs.getInt("id_czytelnik");
                   id_ksi = rs.getInt("id_ksiazka");
                   date = Long.valueOf(rs.getString("data_wypozyczenia"));
               }else
               {
                   date_tmp = Long.valueOf(rs.getString("data_wypozyczenia"));
                   if(date_tmp < date)
                   {
                       id_czyt = rs.getInt("id_czytelnik");
                       id_ksi = rs.getInt("id_ksiazka");
                       date = date_tmp;
                   }
               }



            }
        } catch (SQLException e) {
            System.out.println("TUTAJ  books by id wypozyczone1!!!");
            System.out.println(e.getMessage());
        }

        if(id_czyt == id_czytelnik)
        {
            return true;
        }else {
            return false;
        }
    }


    public static void returnBorrowedBook(int id_ksiazka, int id_pracownik_oddanie, String data_oddania, int id_czytelnik)
    {
        String sql = "UPDATE wypozyczenia SET id_pracownik_oddanie = ? , "
                + "data_oddania = ? "
                + "WHERE id_ksiazka = ? AND id_czytelnik = ?";

        try (Connection conn = App.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id_pracownik_oddanie);
            pstmt.setString(2, data_oddania);
            pstmt.setInt(3, id_ksiazka);
            pstmt.setInt(4, id_czytelnik);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }


    public static void deleteTableWypozyczenia() {
        // SQLite connection string
        String url = "jdbc:sqlite:C://Users/laseraptor/Desktop/javaFX _tmp_2_1_11/PrzykladFX2/DB/biblioteka.db";

        // SQL statement for creating a new table
        String sql = "DROP TABLE IF EXISTS wypozyczenia";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    //**********************************************
    public static void createNewTableWypozyczenia() {
        // SQLite connection string
        String url = "jdbc:sqlite:C://Users/laseraptor/Desktop/javaFX _tmp_2_1_11/PrzykladFX2/DB/biblioteka.db";

        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS wypozyczenia (\n"
                + "	id_wypozycenia integer PRIMARY KEY,\n"
                + "	id_czytelnik integer,\n"
                + "	id_ksiazka integer,\n"
                + "	data_wypozyczenia text,\n"
                + "	id_pracownik_wypozyczenie integer,\n"
                + "	data_oddania text,\n"
                + "	id_pracownik_oddanie integer\n"
                + ");";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void createNewTableKsiazki() {
        // SQLite connection string
        String url = "jdbc:sqlite:C://Users/laseraptor/Desktop/javaFX _tmp_2_1_11/PrzykladFX2/DB/biblioteka.db";

        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS ksiazki (\n"
                + "	id_ksiazka integer PRIMARY KEY,\n"
                + "	isbn text,\n"
                + "	id_kategoria integer,\n"
                + "	tytul text,\n"
                + "	opis text,\n"
                + "	id_autor integer,\n"
                + "	id_wydawnictwo integer\n"
                + ");";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void createNewTablePracownicy() {
        // SQLite connection string
        String url = "jdbc:sqlite:C://Users/laseraptor/Desktop/javaFX _tmp_2_1_11/PrzykladFX2/DB/biblioteka.db";

        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS pracownicy (\n"
                + "	id_pracownik integer PRIMARY KEY,\n"
                + "	login text,\n"
                + "	haslo text\n"
                + ");";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void createNewTableCzytelnicy() {
        // SQLite connection string
        String url = "jdbc:sqlite:C://Users/laseraptor/Desktop/javaFX _tmp_2_1_11/PrzykladFX2/DB/biblioteka.db";

        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS czytelnicy (\n"
                + "	id_czytelnik integer PRIMARY KEY,\n"
                + "	login text,\n"
                + "	haslo text\n"
                + ");";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void createNewTableKategorie() {
        // SQLite connection string
        String url = "jdbc:sqlite:C://Users/laseraptor/Desktop/javaFX _tmp_2_1_11/PrzykladFX2/DB/biblioteka.db";

        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS kategorie (\n"
                + "	id_kategoria integer PRIMARY KEY,\n"
                + "	nazwa text UNIQUE\n"
                + ")";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void createNewTableAutorzy() {
        // SQLite connection string
        String url = "jdbc:sqlite:C://Users/laseraptor/Desktop/javaFX _tmp_2_1_11/PrzykladFX2/DB/biblioteka.db";

        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS autorzy (\n"
                + "	id_autor integer PRIMARY KEY,\n"
                + "	imie text,\n"
                + "	nazwisko text,\n"
                + "unique (imie, nazwisko)\n"
                + ")";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public static void createNewTableWydawnictwa() {
        // SQLite connection string
        String url = "jdbc:sqlite:C://Users/laseraptor/Desktop/javaFX _tmp_2_1_11/PrzykladFX2/DB/biblioteka.db";

        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS wydawnictwa (\n"
                + "	id_wydawnictwo integer PRIMARY KEY,\n"
                + "	nazwa text UNIQUE\n"
                + ")";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public static void createNewTableZamowienia() {
        // SQLite connection string
        String url = "jdbc:sqlite:C://Users/laseraptor/Desktop/javaFX _tmp_2_1_11/PrzykladFX2/DB/biblioteka.db";

        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS zamowienia (\n"
                + "	id_zamowienia integer PRIMARY KEY,\n"
                + "	id_czytelnik integer,\n"
                + "	id_ksiazka integer,\n"
                + " unique (id_czytelnik, id_ksiazka)\n"
                + ")";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void createNewTableAdmin() {
        // SQLite connection string
        String url = "jdbc:sqlite:C://Users/laseraptor/Desktop/javaFX _tmp_2_1_11/PrzykladFX2/DB/biblioteka.db";

        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS admin (\n"
                + "	id_admin integer PRIMARY KEY,\n"
                + "	login text,\n"
                + "	haslo text\n"
                + ");";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    void initializeTables()
    {
        createNewTableAutorzy();
        createNewTableCzytelnicy();
        createNewTableKategorie();
        createNewTableKsiazki();
        createNewTablePracownicy();
        createNewTableWydawnictwa();
        createNewTableWypozyczenia();
        createNewTableZamowienia();
        createNewTableAdmin();
    }
    //***********************************************
    public static void main(String[] args) {
        launch();
    }

}