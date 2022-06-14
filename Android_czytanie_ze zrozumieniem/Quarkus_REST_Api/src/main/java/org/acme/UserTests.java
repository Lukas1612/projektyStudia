package org.acme;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.Tuple;

import java.time.format.DateTimeFormatter;

public class UserTests {

    Integer id;
    String score;
    Integer test_passage_id;
    Integer user_id;
    String date;

    public UserTests(Integer id, String score, Integer test_passage_id, Integer user_id, String date) {
        this.id = id;
        this.score = score;
        this.test_passage_id = test_passage_id;
        this.user_id = user_id;
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public Integer getTest_passage_id() {
        return test_passage_id;
    }

    public void setTest_passage_id(Integer test_passage_id) {
        this.test_passage_id = test_passage_id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "UserTests{" +
                "id=" + id +
                ", score='" + score + '\'' +
                ", test_passage_id=" + test_passage_id +
                ", user_id=" + user_id +
                ", date='" + date + '\'' +
                '}';
    }

    static UserTests from(Row row) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-LL-dd");
        return new UserTests(row.getInteger("id"), row.getString("score"), row.getInteger("test_passage_id"), row.getInteger("user_id"), row.getLocalDate("date").format(formatter));
    }

    public static Uni<Boolean> delete(PgPool client, String name, String password, Integer passage_id) {
        return client
                .preparedQuery("DELETE FROM user_tests USING users WHERE users.name = $1 AND users.password = $2 and user_tests.user_id = users.id and user_tests.test_passage_id = $3 ")
                .execute(Tuple.of(name, password, passage_id))
                .onItem()
                .transform(m -> m.rowCount() == 1);
    }



    //example of INSERT withe value selected from diffrent table, not for use as actual endpoint
    public static Uni<Long> save(PgPool client, Integer test_passage_id, String score, String date, String name, String password) {

        String sql = "INSERT INTO user_tests (id, test_passage_id, user_id, score, date) VALUES ((select max(id) + 1 from user_tests), $1, (SELECT users.id from users WHERE users.name = $2 and users.password = $3),  $4, '" + date +"') RETURNING id";

        return client
                .preparedQuery(sql)
                .execute(Tuple.of(test_passage_id, name, password, score))
                .onItem()
                .transform(m -> m.iterator().next().getLong("id"));
    }

    public static Multi<UserTests> findAll(PgPool client, String name, String password) {

        return client.preparedQuery("SELECT * FROM user_tests JOIN users ON users.id = user_tests.user_id WHERE users.name=$1 and users.password=$2")
                .execute(Tuple.of(name, password)).onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem().transform(UserTests::from);
    }





}
