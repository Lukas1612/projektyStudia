package org.acme;

import io.vertx.mutiny.sqlclient.Row;

public class Tips {

    Integer id;
    String text;
    Integer passage_id;

    public Tips(Integer id, String text, Integer passage_id) {
        this.id = id;
        this.text = text;
        this.passage_id = passage_id;
    }

    public Integer getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public Integer getPassage_id() {
        return passage_id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setPassage_id(Integer passage_id) {
        this.passage_id = passage_id;
    }

    static Tips from(Row row) {
        return new Tips(row.getInteger("id"), row.getString("text"), row.getInteger("passage_id"));
    }

    @Override
    public String toString() {
        return "Tips{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", passage_id=" + passage_id +
                '}';
    }
}
