package org.acme;

import io.smallrye.mutiny.Multi;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;

public class QuestionnaireQuestions {

    Integer id;
    String text;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public QuestionnaireQuestions(Integer id, String text) {
        this.id = id;
        this.text = text;
    }

    @Override
    public String toString() {
        return "QuestionnaireQuestions{" +
                "id=" + id +
                ", text='" + text + '\'' +
                '}';
    }

    static QuestionnaireQuestions from(Row row) {
        return new QuestionnaireQuestions(row.getInteger("id"), row.getString("text"));
    }

    public static Multi<QuestionnaireQuestions> findAll(PgPool client) {
        return client
                .query("SELECT * FROM questionnaire_questions")
                .execute()
                .onItem()
                .transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem()
                .transform(QuestionnaireQuestions::from);
    }

}
