package org.acme;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.Tuple;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Meals_schedule {

    Long id;
    Long user_id;

    //time 24h = 1440 min
    //example: 19:30 = (19 + 30/60)*60 = 1170
    String first_meal_name;
    int first_meal_time;
    String second_meal_name;
    int second_meal_time;
    String third_meal_name;
    int third_meal_time;
    String fourth_meal_name;
    int fourth_meal_time;
    String fifth_meal_name;
    int fifth_meal_time;
    String sixth_meal_name;
    int sixth_meal_time;
    String day; //yyyy-mm-dd

    public Meals_schedule(Long id, Long user_id) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);

        ZoneId z = ZoneId.of( "Europe/Berlin");
        ZonedDateTime zdt = ZonedDateTime.now( z );

        this.day = zdt.format(formatter);


        this.id = id;
        this.user_id = user_id;

        this.first_meal_name = "";
        this.first_meal_time = -1;
        this.second_meal_name = "";
        this.second_meal_time = -1;
        this.third_meal_name = "";
        this.third_meal_time = -1;
        this.fourth_meal_name = "";
        this.fourth_meal_time = -1;
        this.fifth_meal_name = "";
        this.fifth_meal_time = -1;
        this.sixth_meal_name = "";
        this.sixth_meal_time = -1;
    }

    public Meals_schedule(Long id, Long user_id, String first_meal_name, int first_meal_time, String second_meal_name, int second_meal_time, String third_meal_name, int third_meal_time, String fourth_meal_name, int fourth_meal_time, String fifth_meal_name, int fifth_meal_time, String sixth_meal_name, int sixth_meal_time, String day) {
        this.id = id;
        this.user_id = user_id;
        this.first_meal_name = first_meal_name;
        this.first_meal_time = first_meal_time;
        this.second_meal_name = second_meal_name;
        this.second_meal_time = second_meal_time;
        this.third_meal_name = third_meal_name;
        this.third_meal_time = third_meal_time;
        this.fourth_meal_name = fourth_meal_name;
        this.fourth_meal_time = fourth_meal_time;
        this.fifth_meal_name = fifth_meal_name;
        this.fifth_meal_time = fifth_meal_time;
        this.sixth_meal_name = sixth_meal_name;
        this.sixth_meal_time = sixth_meal_time;
        this.day = day;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getFirst_meal_name() {
        return first_meal_name;
    }

    public int getFirst_meal_time() {
        return first_meal_time;
    }

    public String getSecond_meal_name() {
        return second_meal_name;
    }

    public int getSecond_meal_time() {
        return second_meal_time;
    }

    public String getThird_meal_name() {
        return third_meal_name;
    }

    public int getThird_meal_time() {
        return third_meal_time;
    }

    public String getFourth_meal_name() {
        return fourth_meal_name;
    }

    public int getFourth_meal_time() {
        return fourth_meal_time;
    }

    public String getFifth_meal_name() {
        return fifth_meal_name;
    }

    public int getFifth_meal_time() {
        return fifth_meal_time;
    }

    public String getSixth_meal_name() {
        return sixth_meal_name;
    }

    public int getSixth_meal_time() {
        return sixth_meal_time;
    }

    public Long getId() {
        return id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public void setFirst_meal_name(String first_meal_name) {
        this.first_meal_name = first_meal_name;
    }

    public void setFirst_meal_time(int first_meal_time) {
        this.first_meal_time = first_meal_time;
    }

    public void setSecond_meal_name(String second_meal_name) {
        this.second_meal_name = second_meal_name;
    }

    public void setSecond_meal_time(int second_meal_time) {
        this.second_meal_time = second_meal_time;
    }

    public void setThird_meal_name(String third_meal_name) {
        this.third_meal_name = third_meal_name;
    }

    public void setThird_meal_time(int third_meal_time) {
        this.third_meal_time = third_meal_time;
    }

    public void setFourth_meal_name(String fourth_meal_name) {
        this.fourth_meal_name = fourth_meal_name;
    }

    public void setFourth_meal_time(int fourth_meal_time) {
        this.fourth_meal_time = fourth_meal_time;
    }

    public void setFifth_meal_name(String fifth_meal_name) {
        this.fifth_meal_name = fifth_meal_name;
    }

    public void setFifth_meal_time(int fifth_meal_time) {
        this.fifth_meal_time = fifth_meal_time;
    }

    public void setSixth_meal_name(String sixth_meal_name) {
        this.sixth_meal_name = sixth_meal_name;
    }

    public void setSixth_meal_time(int sixth_meal_time) {
        this.sixth_meal_time = sixth_meal_time;
    }

    static Meals_schedule from(Row row) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-LL-dd");
        return new Meals_schedule(row.getLong("id"), row.getLong("user_id"), row.getString("first_meal_name"), row.getInteger("first_meal_time"), row.getString("second_meal_name"), row.getInteger("second_meal_time"), row.getString("third_meal_name"), row.getInteger("third_meal_time"), row.getString("fourth_meal_name"), row.getInteger("fourth_meal_time"), row.getString("fifth_meal_name"), row.getInteger("fifth_meal_time"), row.getString("sixth_meal_name"), row.getInteger("sixth_meal_time"), row.getLocalDate("day").format(formatter));

    }

 //test function to remove
    public static Multi<Meals_schedule> findAll(PgPool client) {
        return client
                .query("SELECT * FROM Meals_schedule")
                .execute()
                .onItem()
                .transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem()
                .transform(Meals_schedule::from);
    }

    public static Multi<Meals_schedule> findByNameAndPassword(PgPool client, String name, String password) {
        return client.preparedQuery("SELECT * FROM Meals_schedule JOIN Users ON Users.id = Meals_schedule.user_id WHERE Users.name = $1 AND Users.password = $2")
                .execute(Tuple.of(name, password)) .onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem().transform(Meals_schedule::from);
    }

    public static Multi<Meals_schedule> getLastScheduleNameAndPassword(PgPool client, String name, String password) {

        return client.preparedQuery("SELECT Meals_schedule.id, Meals_schedule.user_id, first_meal_name, first_meal_time, second_meal_name, second_meal_time, third_meal_name, third_meal_time, fourth_meal_name, fourth_meal_time, fifth_meal_name, fifth_meal_time, sixth_meal_name, sixth_meal_time, day FROM Meals_schedule JOIN Users ON Users.id = Meals_schedule.user_id WHERE Users.name = $1 AND Users.password = $2 ORDER BY day DESC FETCH FIRST ROW ONLY")
                .execute(Tuple.of(name, password)).onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem().transform(Meals_schedule::from);

    }

    public static Multi<Meals_schedule> findByDayNameAndPassword(PgPool client, String day, String name, String password) {


        String sql = "SELECT * FROM Meals_schedule JOIN Users ON Users.name = \'" + name + "\' AND Users.password = \'" + password + "\' WHERE Meals_schedule.day = \'" + day + "\' AND Meals_schedule.user_id = Users.id";
        System.out.println(sql);

        return client.preparedQuery(sql)
                .execute().onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem().transform(Meals_schedule::from);

    }


    public static Uni<Boolean> save(PgPool client, String first_meal_name, int first_meal_time, String second_meal_name, int second_meal_time, String third_meal_name, int third_meal_time, String fourth_meal_name, int fourth_meal_time, String fifth_meal_name, int fifth_meal_time, String sixth_meal_name, int sixth_meal_time, String day, String name, String password) {


        String sql="UPDATE Meals_schedule SET " +
                "first_meal_name = \'" + first_meal_name + "\'" +
                ", first_meal_time = " + first_meal_time +
                ", second_meal_name = \'" + second_meal_name + "\'" +
                ", second_meal_time = " + second_meal_time +
                ", third_meal_name = \'" + third_meal_name + "\'" +
                ", third_meal_time = " + third_meal_time +
                ", fourth_meal_name = \'" + fourth_meal_name + "\'" +
                ", fourth_meal_time = " + fourth_meal_time +
                ", fifth_meal_name = \'" + fifth_meal_name + "\'" +
                ", fifth_meal_time = " + fifth_meal_time +
                ", sixth_meal_name = \'" + sixth_meal_name + "\'" +
                ", sixth_meal_time = " + sixth_meal_time +
                " FROM Users WHERE Meals_schedule.user_id = Users.id AND Users.name = " + "\'" + name + "\' " + "AND Meals_schedule.day = " + "\'" + day + "\' " + " AND Users.password = " + "\'" + password + "\' ";

        System.out.println(sql);


        return client
                .preparedQuery(sql)
                .execute()
                .onItem()
                .transform(m -> m.rowCount() == 1);

   }

    public static Uni<Long> saveNew(PgPool client, String first_meal_name, int first_meal_time, String second_meal_name, int second_meal_time, String third_meal_name, int third_meal_time, String fourth_meal_name, int fourth_meal_time, String fifth_meal_name, int fifth_meal_time, String sixth_meal_name, int sixth_meal_time, String day, String name, String password) {

        String sql="INSERT INTO Meals_schedule (user_id, first_meal_name, first_meal_time, second_meal_name, second_meal_time, third_meal_name, third_meal_time, fourth_meal_name, fourth_meal_time, fifth_meal_name, fifth_meal_time, sixth_meal_name, sixth_meal_time, day) VALUES (" +
                "(SELECT id FROM Users WHERE Users.name = \'" + name + "\' AND Users.password = \'" + password + "\'), " +
                "\'" + first_meal_name + "\'" +
                ", " + first_meal_time +
                ", \'" + second_meal_name + "\'" +
                ", " + second_meal_time +
                ", \'" + third_meal_name + "\'" +
                ", " + third_meal_time +
                ", \'" + fourth_meal_name + "\'" +
                ", " + fourth_meal_time +
                ", \'" + fifth_meal_name + "\'" +
                ", " + fifth_meal_time +
                ", \'" + sixth_meal_name + "\'" +
                ", " + sixth_meal_time +
                ", \'" + day + "\') RETURNING id";

        System.out.println(sql);


        return client
                .preparedQuery(sql)
                .execute()
                .onItem()
                .transform(m -> m.iterator().next().getLong("id"));
    }

    public static Uni<Boolean> delete(PgPool client, String name, String password) {
        return client
                .preparedQuery("DELETE FROM Meals_schedule USING Users WHERE Users.id = Meals_schedule.user_id AND Users.name = $1 AND Users.password = $2")
                .execute(Tuple.of(name, password))
                .onItem()
                .transform(m -> m.rowCount() == 1);
    }

    public static Uni<Boolean> checkIfDayScheduleExist(PgPool client, String day, String name, String password)
    {
        String sql = "SELECT * FROM Meals_schedule JOIN Users ON Users.name = \'" + name + "\' AND Users.password = \'" + password + "\' WHERE Meals_schedule.day = \'" + day + "\' AND Meals_schedule.user_id = Users.id";
        System.out.println(sql);
        return client
                .preparedQuery(sql)
                .execute()
                .onItem()
                .transform(m -> m.rowCount() == 1);
    }




}
