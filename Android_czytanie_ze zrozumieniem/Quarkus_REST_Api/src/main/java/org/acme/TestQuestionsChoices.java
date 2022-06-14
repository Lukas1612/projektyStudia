package org.acme;

import io.vertx.mutiny.sqlclient.Row;

public class TestQuestionsChoices {
    Integer id;
    String text;
    String correct_or_false;
    Integer test_questions_id;

    public TestQuestionsChoices(Integer id, String text, String correctOrFalse, Integer test_questions_id) {
        this.id = id;
        this.text = text;
        this.correct_or_false = correctOrFalse;
        this.test_questions_id = test_questions_id;
    }

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

    public String getCorrect_or_false() {
        return correct_or_false;
    }

    public void setCorrect_or_false(String correct_or_false) {
        this.correct_or_false = correct_or_false;
    }

    public Integer getTest_questions_id() {
        return test_questions_id;
    }

    public void setTest_questions_id(Integer test_questions_id) {
        this.test_questions_id = test_questions_id;
    }

    @Override
    public String toString() {
        return "TestQuestionsChoices{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", correctOrFalse='" + correct_or_false + '\'' +
                ", test_questions_id=" + test_questions_id +
                '}';
    }

    static TestQuestionsChoices from(Row row) {
        return new TestQuestionsChoices(row.getInteger("id"), row.getString("text"), row.getString("correct_or_false"), row.getInteger("test_questions_id"));
    }
}
