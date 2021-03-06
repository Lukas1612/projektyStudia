package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * JavaFX App
 */
public class App extends Application {

    private  Connection conn;

    private enum SubqueriesNames
    {
        non_corelative_4in,
        non_corelative_3in,
        non_corelative_2in,
        non_corelative_1in,
        join_4join,
        join_3join,
        join_2join,
        join_1join,
        long_corelative,
        corelative_like_non_corelative_1in,
        corelative_short,
        non_corelative_short_from_corelative_short
    }
    SubqueriesNames subqueriesNames;

    int numberOfSubqueries = subqueriesNames.non_corelative_short_from_corelative_short.ordinal() + 1;

    String[] subqueries;
    String[] names;
    Long[] times;
    Long[] times2;

    private final String url = "jdbc:postgresql://localhost:5432/postgres";
    private final String user = "postgres";
    private final String password = "1391248";


    public static void createNewDatabase(){

        String url = "jdbc:sqlite:C://sqlite/db/tests.db";

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public static void createNewTable() throws FileNotFoundException {
        // SQLite connection string
        String url = "jdbc:sqlite:C://sqlite/db/tests.db";

        File file = new File("C:\\Users\\Laseraptor\\Desktop\\Folder testowy\\SQL-Server-Sample-Database\\CreateDatabase.txt");
        Scanner odczyt = new Scanner(file);

        // SQL statement for creating a new table
        String sql = "";
        String tmp = "";

        while (odczyt.hasNextLine())
        {
            tmp = odczyt.nextLine();
            sql = sql + tmp + "\n";

            if(tmp.endsWith (";"))
            {
                System.out.println ( sql );
                try (Connection conn = DriverManager.getConnection(url);
                     Statement stmt = conn.createStatement()) {
                    // create a new table
                    stmt.execute(sql);
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
                sql = "";
                tmp = "";
            }
        }

    }

    @Override
    public void start(Stage stage) throws IOException {

        setupQueries();



        //createNewDatabase();
        //createNewTable();


        //insert();
        Long starttime = System.currentTimeMillis();
        Long endtime = System.currentTimeMillis();
        Long result = endtime - starttime;
        Long resultQueries;
        Long resultSubqueries;
        System.out.println ("***************");
        System.out.println ("***************");
        System.out.println ("***************");
        System.out.println ("***************");
        executeQuery(subqueries[0]);
        executeQueryPSQL(subqueries[0]);
        System.out.println ("time: " + result);
        System.out.println ("***************");
        System.out.println ();
        System.out.println ();



        int quantityOfLoops=30;

        for(int j=0; j<quantityOfLoops; ++j){


            //******time measuring loop***********
            for(int q=0; q<numberOfSubqueries; ++q)
            {
                starttime = System.currentTimeMillis();
                for (int i = 0; i < 10; ++i) {

                    executeQuery (subqueries[q]);
                }
                endtime = System.currentTimeMillis ();
                resultQueries = endtime - starttime;
                times[q]=times[q]+resultQueries;


                starttime = System.currentTimeMillis();
                for (int i = 0; i < 10; ++i) {

                    executeQueryPSQL(subqueries[q]);
                }
                endtime = System.currentTimeMillis ();
                resultQueries = endtime - starttime;
                times2[q]=times2[q]+resultQueries;


            }
            //*************************************


            System.out.println (j);

        }

        for(int q=0; q<numberOfSubqueries; ++q)
        {
            System.out.println ("***************");
            System.out.println(names[q] + " = " + (times[q]/quantityOfLoops) + "    |     "+ (times2[q]/quantityOfLoops));
            System.out.println ("***************");
        }

        clearTimesTable ();
//##############PostgreSQL################################





    }


    private Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:C://sqlite/db/tests.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    private Connection connectPostgreSQL()
    {
        Connection conn = null;
        try{
            conn = DriverManager.getConnection (url, user, password);
            //System.out.println("Connected to the PostgreSQL server successfully.");

        }catch (SQLException e)
        {
            System.out.println (e.getMessage());
        }

        return conn;
    }



    public void executeQuery(String sql){

        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            // while (rs.next()) {
            // System.out.println(rs.getInt("product_id"));
            // }

           // System.out.println ("end");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void executeQueryPSQL(String sql) {

        try (Connection conn = connectPostgreSQL();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }



    public void insertCat(int category_id, String category_name) {

        String sql = "INSERT INTO categories(category_id,category_name) VALUES(?,?)";


        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt (1, category_id);
            pstmt.setString (2, category_name);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public void insertFromFile() throws FileNotFoundException {

        String url = "jdbc:sqlite:C://sqlite/db/tests.db";

        File file = new File("C:\\Users\\Laseraptor\\Desktop\\Folder testowy\\SQL-Server-Sample-Database\\InsertToDatabase.txt");
        Scanner odczyt = new Scanner(file);

        // SQL statement for creating a new table
        String sql = "";
        String tmp = "";

        while (odczyt.hasNextLine())
        {
            tmp = odczyt.nextLine();
            if(!tmp.isEmpty())
            {
                sql = sql + tmp + "\n";
            }


            if(tmp.endsWith (";"))
            {
               // System.out.println( sql );
                try (Connection conn = this.connect();
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {

                    pstmt.executeUpdate();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                    System.out.println (" komunikat sql: " + sql);
                }
                sql = "";
                tmp = "";
            }

        }
    }


    

    void setupQueries()
    {
        subqueries = new String[numberOfSubqueries];
        names = new String[numberOfSubqueries];
        times = new Long[numberOfSubqueries];
        times2 = new Long[numberOfSubqueries];


        clearTimesTable();

        names[subqueriesNames.non_corelative_4in.ordinal()]="non_corelative_4in";
        subqueries[subqueriesNames.non_corelative_4in.ordinal()]="select customer_id\n" +
                "from customers\n" +
                "where customer_id IN\n" +
                "           (select customer_id\n" +
                "\t\t    from orders\n" +
                "\t\t\twhere order_id IN (select order_id\n" +
                "\t\t\t                    from order_items\n" +
                "\t\t\t\t\t\t\t\twhere product_id IN (select product_id\n" +
                "\t\t\t\t\t\t\t\t                      from products\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t  where product_id IN ( select product_id\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t                     from stocks\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t where store_id=2))))";


        names[subqueriesNames.non_corelative_3in.ordinal()]="non_corelative_3in";
        subqueries[subqueriesNames.non_corelative_3in.ordinal()]="select customer_id\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t \n" +
                "from customers\n" +
                "where customer_id IN\n" +
                "           (select customer_id\n" +
                "\t\t    from orders\n" +
                "\t\t\twhere order_id IN (select order_id\n" +
                "\t\t\t                    from order_items\n" +
                "\t\t\t\t\t\t\t\twhere product_id IN (select product_id\n" +
                "\t\t\t\t\t\t\t\t                      from products\n" +
                "\t\t\t\t\t\t\t\t\t\t\t\t\t  where category_id = 2)))";


        names[subqueriesNames.non_corelative_2in.ordinal()]="non_corelative_2in";
        subqueries[subqueriesNames.non_corelative_2in.ordinal()]="select customer_id\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t \n" +
                "from customers\n" +
                "where customer_id IN\n" +
                "           (select customer_id\n" +
                "\t\t    from orders\n" +
                "\t\t\twhere order_id IN (select order_id\n" +
                "\t\t\t                    from order_items\n" +
                "\t\t\t\t\t\t\t\twhere product_id = 2))";


        names[subqueriesNames.non_corelative_1in.ordinal()]="non_corelative_1in";
        subqueries[subqueriesNames.non_corelative_1in.ordinal()]="select customer_id\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t \n" +
                "from customers\n" +
                "where customer_id IN\n" +
                "           (select customer_id\n" +
                "\t\t    from orders\n" +
                "\t\t\twhere order_id = 2)";

        names[subqueriesNames.join_4join.ordinal()]="join_4join";
        subqueries[subqueriesNames.join_4join.ordinal()]="select cus.customer_id\n" +
                "from customers cus\n" +
                "join orders ors on cus.customer_id=ors.customer_id\n" +
                "join order_items oi on oi.order_id = ors.order_id\n" +
                "join products prs on prs.product_id = oi.product_id\n" +
                "join stocks ss on ss.product_id = prs.product_id\n" +
                "where ss.store_id = 2";

        names[subqueriesNames.join_3join.ordinal()]="join_3join";
        subqueries[subqueriesNames.join_3join.ordinal()]="select cus.customer_id\n" +
                "from customers cus\n" +
                "join orders ors on cus.customer_id=ors.customer_id\n" +
                "join order_items oi on oi.order_id = ors.order_id\n" +
                "join products prs on prs.product_id = oi.product_id\n" +
                "where prs.category_id = 2";

        names[subqueriesNames.join_2join.ordinal()]="join_2join";
        subqueries[subqueriesNames.join_2join.ordinal()]="select cus.customer_id\n" +
                "from customers cus\n" +
                "join orders ors on cus.customer_id=ors.customer_id\n" +
                "join order_items oi on oi.order_id = ors.order_id\n" +
                "where oi.product_id = 2";

        names[subqueriesNames.join_1join.ordinal()]="join_1join";
        subqueries[subqueriesNames.join_1join.ordinal()]="select cus.customer_id\n" +
                "from customers cus\n" +
                "join orders ors on cus.customer_id=ors.customer_id\n" +
                "where ors.order_id = 2";

        names[subqueriesNames.corelative_like_non_corelative_1in.ordinal()]="corelative_like_non_corelative_1in";
        subqueries[subqueriesNames.corelative_like_non_corelative_1in.ordinal()]="select distinct (select customer_id\n" +
                "         from customers\n" +
                "\t\t  where customer_id = orders.customer_id\n" +
                "\t\t) as customer_id\n" +
                "from orders\t\n" +
                "where orders.order_id = 2";

        names[subqueriesNames.long_corelative.ordinal()]="long_corelative";
        subqueries[subqueriesNames.long_corelative.ordinal()]="select distinct (select customer_id\n" +
                "         from customers\n" +
                "\t\t  where customer_id = orders.customer_id\n" +
                "\t\t) as customer_id\n" +
                "from orders\t\n" +
                "where orders.order_id IN (select order_id \n" +
                "\t\t\t\t          from order_items\n" +
                "\t\t\t\t\t\t  where product_id IN (select ( select product_id\n" +
                "\t\t\t\t\t\t                                from products \n" +
                "\t\t\t\t\t\t\t\t\t\t\t            where products.product_id = stocks.product_id)\n" +
                "\t\t\t\t\t\t\t\t\t\t\t  from stocks\n" +
                "\t\t\t\t\t\t\t\t\t\t\t  where store_id=2))";

        names[subqueriesNames.corelative_short.ordinal()]="corelative_short";
        subqueries[subqueriesNames.corelative_short.ordinal()]="select (select store_id\n" +
                "        from orders o\n" +
                "\t\twhere o.order_id=oi.order_id)\n" +
                "from order_items oi\n" +
                "where item_id = 2";

        names[subqueriesNames.non_corelative_short_from_corelative_short.ordinal()]="non_corelative_short_from_corelative_short";
        subqueries[subqueriesNames.non_corelative_short_from_corelative_short.ordinal()]="select store_id\n" +
                "from orders o\n" +
                "where o.order_id=2";
    }

    void clearTimesTable()
    {
        for(int i=0; i<numberOfSubqueries; ++i)
        {
            times[i]= Long.valueOf(0);
            times2[i]= Long.valueOf(0);
        }
    }

    public static void main(String[] args) {
        launch();
    }

}