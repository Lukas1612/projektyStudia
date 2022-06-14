package org.acme;

import io.smallrye.mutiny.Multi;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.Tuple;

public class PassageTips {

    Integer id;
    String text;
    Integer passage_id;

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

    public Integer getPassage_id() {
        return passage_id;
    }

    public void setPassage_id(Integer passage_id) {
        this.passage_id = passage_id;
    }

    public PassageTips(Integer id, String text, Integer passage_id) {
        this.id = id;
        this.text = text;
        this.passage_id = passage_id;
    }

    static PassageTips from(Row row) {
        return new PassageTips(row.getInteger("id"), row.getString("text"), row.getInteger("passage_id"));
    }

    public static Multi<PassageTips> findTipsById(PgPool client, Integer id) {
        return client.preparedQuery("SELECT * FROM  Tips WHERE passage_id = $1")
                .execute(Tuple.of(id)).onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem().transform(PassageTips::from);
    }
}
