package org.acme;

import io.vertx.mutiny.sqlclient.Row;

import java.util.ArrayList;

public class TestQuestions {
    Integer id;
    String  text;
    Integer test_passage_id;

    public TestQuestions(Integer id, String text, Integer test_passage_id) {
        this.id = id;
        this.text = text;
        this.test_passage_id = test_passage_id;
    }

    public TestQuestions(TestQuestions testQuestions) {
        this.id = testQuestions.id;
        this.text = testQuestions.text;
        this.test_passage_id = testQuestions.test_passage_id;
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

    public Integer getTest_passage_id() {
        return test_passage_id;
    }

    public void setTest_passage_id(Integer test_passage_id) {
        this.test_passage_id = test_passage_id;
    }

    @Override
    public String toString() {
        return "TestQuestions{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", test_passage_id=" + test_passage_id +
                '}';
    }

    static TestQuestions from(Row row) {
        return new TestQuestions(row.getInteger("id"), row.getString("text"), row.getInteger("test_passage_id"));
    }

}
