package org.acme;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.Tuple;

import java.time.format.DateTimeFormatter;

public class Eaten_products {

    Long id;
    Long product_id;
    Long user_id;
    String date; //yyyy-mm-dd
    String meal;
    int portions;

    public Eaten_products(Long id, Long product_id, Long user_id, String date, String meal, int portions) {
        this.id = id;
        this.product_id = product_id;
        this.user_id = user_id;
        this.date = date;
        this.meal = meal;
        this.portions = portions;
    }

    public int getPortions() {
        return portions;
    }

    public void setPortions(int portions) {
        this.portions = portions;
    }

    public String getMeal() {
        return meal;
    }

    public void setMeal(String meal) {
        this.meal = meal;
    }

    public Long getId() {
        return id;
    }

    public Long getProduct_id() {
        return product_id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setProduct_id(Long product_id) {
        this.product_id = product_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    static Eaten_products from(Row row) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-LL-dd");
        return new Eaten_products(row.getLong("id"), row.getLong("product_id"), row.getLong("user_id"),  row.getLocalDate("date").format(formatter), row.getString("meal"), row.getInteger("portions"));
    }

    public static Multi<Eaten_products> findByNameAndPassword(PgPool client, String name, String password) {
        return client.preparedQuery("SELECT * FROM Eaten_products JOIN Users ON Users.name = $1 AND Users.password = $2 WHERE Eaten_products.user_id = Users.id")
                .execute(Tuple.of(name, password)) .onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem().transform(Eaten_products::from);
    }
/*
    public static Multi<Eaten_products> findByNamePasswordAndDay(PgPool client, String name, String password, String day) {

        String sql = "SELECT * FROM Eaten_products JOIN Users ON Users.name = \'" + name + "\' AND Users.password = \'" + password + "\' JOIN Meals_schedule ON Meals_schedule.day = \'" + day + "\' AND Meals_schedule.user_id = Users.id WHERE Eaten_products.meals_schedule_id = Meals_schedule.id";
        System.out.println(sql);

        return client.preparedQuery(sql)
                .execute().onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem().transform(Eaten_products::from);
    }
*/


    public static Multi<Eaten_products> findByNamePasswordAndDay(PgPool client, String name, String password, String day) {

        String sql = "SELECT * FROM Eaten_products JOIN Users ON Users.name = \'" + name + "\' AND Users.password = \'" + password + "\' WHERE Eaten_products.date = \'" + day + "\' AND Eaten_products.user_id = Users.id" ;
        System.out.println(sql);

        return client.preparedQuery(sql)
                .execute().onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem().transform(Eaten_products::from);
    }

    /*public static Uni<Long> save(PgPool client, Long product_id, Integer meal_number, String day, String name, String password) {

        String meal_name;
        switch(meal_number) {
            case 1:
                meal_name = "first_meal_name";
                break;
            case 2:
                meal_name = "second_meal_name";

                break;
            case 3:
                meal_name = "third_meal_name";
                break;
            case 4:
                meal_name = "fourth_meal_name";
                break;
            case 5:
                meal_name = "fifth_meal_name";
                break;
            case 6:
                meal_name = "sixth_meal_time";
                break;
            default:
                meal_name = "first_meal_name";
        }

        String sql = "INSERT INTO Eaten_products (product_id, user_id, meals_schedule_id, meal_number) VALUES (" + product_id +", " + "(SELECT Users.id FROM Users WHERE Users.name = \'" + name + "\' AND Users.password = \'" + password + "\'), " + "(SELECT Meals_schedule.id FROM Meals_schedule JOIN Users ON Users.name = \'" + name + "\' AND Users.password = \'" + password + "\' WHERE Meals_schedule.day = \'" + day + "\' AND Meals_schedule.user_id = Users.id AND Meals_schedule." + meal_name + " != \'" + "\'), " + meal_number +") RETURNING id";
        System.out.println(sql);
        return client
                .preparedQuery(sql)
                .execute()
                .onItem()
                .transform(m -> m.iterator().next().getLong("id"));
    }*/

    public static Uni<Long> save(PgPool client, Long product_id, String day, String name, String password, String meal, int portions) {

        String sql = "INSERT INTO Eaten_products (product_id, user_id, date, meal, portions) VALUES (" + product_id +", " + "(SELECT Users.id FROM Users WHERE Users.name = \'" + name + "\' AND Users.password = \'" + password + "\'), \'" + day  +"\', \'" + meal + "\', " + portions + ") RETURNING id";
        System.out.println(sql);
        return client
                .preparedQuery(sql)
                .execute()
                .onItem()
                .transform(m -> m.iterator().next().getLong("id"));
    }

    public static Uni<Boolean> delete(PgPool client, String name, String password, int eaten_products_id) {


        /* String sql = "DELETE FROM Eaten_products USING Users WHERE Users.name = \'" + name +  "\' AND Users.password = \'" +  password + "\' AND Users.id = Eaten_products.user_id AND Eaten_products.id = " + eaten_products_id + " AND Eaten_products.date = \'" + date + "\' AND Eaten_products.meal = \'" + meal + "\'" ;
        System.out.println(sql);*/

        String sql = "DELETE FROM Eaten_products USING Users WHERE Users.name = \'" + name +  "\' AND Users.password = \'" +  password + "\' AND Users.id = Eaten_products.user_id AND Eaten_products.id = " + eaten_products_id;
        System.out.println(sql);
        return client
                .preparedQuery(sql)
                .execute()
                .onItem()
                .transform(m -> m.rowCount() == 1);
    }
}
