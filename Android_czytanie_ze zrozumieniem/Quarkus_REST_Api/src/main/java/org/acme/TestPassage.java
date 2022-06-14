package org.acme;

import io.vertx.mutiny.sqlclient.Row;

public class TestPassage {

    Integer id;
    String  text;
    String title;

    public TestPassage(Integer id, String text, String title) {
        this.id = id;
        this.text = text;
        this.title = title;
    }

    static TestPassage from(Row row) {
        return new TestPassage(row.getInteger("id"), row.getString("text"), row.getString("title"));
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "TestPassage{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
