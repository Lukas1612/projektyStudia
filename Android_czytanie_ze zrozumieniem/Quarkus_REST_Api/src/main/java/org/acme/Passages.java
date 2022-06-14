package org.acme;

import io.smallrye.mutiny.Multi;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;

public class Passages {

    Integer id;
    String  text;
    String title;
    String translationtext;
    Integer test_passage_id;

    public String getTranslationtext() {
        return translationtext;
    }

    public void setTranslationtext(String translationtext) {
        this.translationtext = translationtext;
    }

    public Integer getTest_passage_id() {
        return test_passage_id;
    }

    public void setTest_passage_id(Integer test_passage_id) {
        this.test_passage_id = test_passage_id;
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

    public Passages(Integer id, String text, String title, String translationtext,  Integer test_passage_id) {
        this.id = id;
        this.text = text;
        this.title = title;
        this.translationtext = translationtext;
        this.test_passage_id = test_passage_id;
    }



    static Passages from(Row row) {
        return new Passages(row.getInteger("id"), row.getString("text"), row.getString("title"), row.getString("translationtext"), row.getInteger("test_passage_id"));
    }



    public static Multi<Passages> findAll(PgPool client) {
        return client
                .query("SELECT passages.id, passages.text, passages.title, translations.translationtext, passages.test_passage_id  FROM passages JOIN translations ON translations.passage_id = passages.id")
                .execute()
                .onItem()
                .transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem()
                .transform(Passages::from);
    }

}
