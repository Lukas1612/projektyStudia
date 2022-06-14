package org.acme;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.Tuple;

public class UserTestQuestionChoices {

    Integer id;
    Integer test_question_id;
    Integer user_id;
    Integer question_choice_id;


    public UserTestQuestionChoices(Integer id, Integer test_question_id, Integer user_id, Integer question_choice_id) {
        this.id = id;
        this.test_question_id = test_question_id;
        this.user_id = user_id;
        this.question_choice_id = question_choice_id;
    }

    static UserTestQuestionChoices from(Row row) {
        return new UserTestQuestionChoices(row.getInteger("id"), row.getInteger("test_question_id"), row.getInteger("user_id"), row.getInteger("question_choice_id"));
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTest_question_id() {
        return test_question_id;
    }

    public void setTest_question_id(Integer test_question_id) {
        this.test_question_id = test_question_id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getQuestion_choice_id() {
        return question_choice_id;
    }

    public void setQuestion_choice_id(Integer question_choice_id) {
        this.question_choice_id = question_choice_id;
    }

    @Override
    public String toString() {
        return "UserTestQuestionChoices{" +
                "id=" + id +
                ", test_question_id=" + test_question_id +
                ", user_id=" + user_id +
                ", question_choice_id=" + question_choice_id +
                '}';
    }


    public static Uni<Long> save(PgPool client, Integer test_question_id, Integer question_choice_id, String name, String password) {


        String sql = "INSERT INTO user_test_question_choices (id, test_question_id, question_choice_id, user_id) VALUES ((select max(id) + 1 from user_test_question_choices), $1, $2, (SELECT id from Users WHERE name = $3 AND password = $4)) RETURNING id";
        return client
                .preparedQuery(sql)
                .execute(Tuple.of(test_question_id, question_choice_id, name, password))
                .onItem()
                .transform(m -> m.iterator().next().getLong("id"));
    }


}
