package org.acme;

import io.vertx.mutiny.sqlclient.Row;

public class Flashcard_groups {
    Integer id;
    Integer user_id;
    String title;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Flashcard_groups{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", title='" + title + '\'' +
                '}';
    }

    public Flashcard_groups(Integer id, Integer user_id, String title) {
        this.id = id;
        this.user_id = user_id;
        this.title = title;
    }

    static Flashcard_groups from(Row row) {
        return new Flashcard_groups(row.getInteger("id"), row.getInteger("user_id"), row.getString("title"));
    }
}
