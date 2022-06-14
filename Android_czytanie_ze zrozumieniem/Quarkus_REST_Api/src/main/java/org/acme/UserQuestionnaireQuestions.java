package org.acme;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.Tuple;

public class UserQuestionnaireQuestions {

    Integer id;
    Integer questionnaire_questions_id;
    Integer user_id;
    Integer passage_id;

    public Integer getPassage_id() {
        return passage_id;
    }

    public void setPassage_id(Integer passage_id) {
        this.passage_id = passage_id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getQuestionnaire_questions_id() {
        return questionnaire_questions_id;
    }

    public void setQuestionnaire_questions_id(Integer questionnaire_questions_id) {
        this.questionnaire_questions_id = questionnaire_questions_id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "UserQuestionnaireQuestions{" +
                "id=" + id +
                ", questionnaire_questions_id=" + questionnaire_questions_id +
                ", user_id=" + user_id +
                ", passage_id=" + passage_id +
                '}';
    }

    public UserQuestionnaireQuestions(Integer id, Integer questionnaire_questions_id, Integer user_id, Integer passage_id) {
        this.id = id;
        this.questionnaire_questions_id = questionnaire_questions_id;
        this.user_id = user_id;
        this.passage_id = passage_id;
    }

    static UserQuestionnaireQuestions from(Row row) {
        return new UserQuestionnaireQuestions(row.getInteger("id"), row.getInteger("questionnaire_questions_id"), row.getInteger("user_id"),  row.getInteger("passage_id"));
    }

    public static Multi<UserQuestionnaireQuestions> findByNameAndPassword(PgPool client, String name, String password) {
        return client.preparedQuery("SELECT * FROM user_questionnaire_questions JOIN Users ON Users.name = $1 AND Users.password = $2 WHERE Users.id = user_questionnaire_questions.user_id")
                .execute(Tuple.of(name, password)) .onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem().transform(UserQuestionnaireQuestions::from);
    }

    public static Uni<Long> save(PgPool client, Integer questionnaire_questions_id, Integer passage_id, String name, String password) {

        String sql = "INSERT INTO user_questionnaire_questions (id, questionnaire_questions_id, passage_id, user_id) VALUES ((select max(id) + 1 from user_questionnaire_questions), $1, $2, (SELECT id from Users WHERE name = $3 AND password = $4)) RETURNING id";


        System.out.println(sql);
        return client
                .preparedQuery(sql)
                .execute(Tuple.of(questionnaire_questions_id, passage_id, name, password))
                .onItem()
                .transform(m -> m.iterator().next().getLong("id"));
    }


}
