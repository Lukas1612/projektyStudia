package org.acme;

import io.smallrye.mutiny.Multi;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;

public class Passages {

    Integer id;
    String  text;
    String title;

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
        return "Passages{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", title='" + title + '\'' +
                '}';
    }

    public Passages(Integer id, String text, String title) {
        this.id = id;
        this.text = text;
        this.title = title;
    }


    static Passages from(Row row) {
        return new Passages(row.getInteger("id"), row.getString("text"), row.getString("title"));
    }

    public static Multi<Passages> findAll(PgPool client) {
        return client
                .query("SELECT * FROM Passages ORDER BY id DESC")
                .execute()
                .onItem()
                .transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem()
                .transform(Passages::from);
    }
}
