package org.acme;

import io.vertx.mutiny.sqlclient.Row;

public class Questions {

    Integer id;
    Integer passage_id;
    String question;
    String answer;

    public Questions(Integer id, Integer passage_id, String question, String answer) {
        this.id = id;
        this.passage_id = passage_id;
        this.question = question;
        this.answer = answer;
    }

    public Integer getId() {
        return id;
    }

    public Integer getPassage_id() {
        return passage_id;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setPassage_id(Integer passage_id) {
        this.passage_id = passage_id;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        return "Questions{" +
                "id=" + id +
                ", passage_id=" + passage_id +
                ", question='" + question + '\'' +
                ", answer='" + answer + '\'' +
                '}';
    }


    static Questions from(Row row) {
        return new Questions(row.getInteger("id"), row.getInteger("passage_id"), row.getString("question"),  row.getString("answer"));
    }
}
