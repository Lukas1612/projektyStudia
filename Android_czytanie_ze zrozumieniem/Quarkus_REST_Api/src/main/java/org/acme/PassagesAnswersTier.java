package org.acme;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.Tuple;

public class PassagesAnswersTier {
    Integer id;
    Integer tier;
    Integer passage_id;
    Integer question_id;
    Integer user_id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTier() {
        return tier;
    }

    public void setTier(Integer tier) {
        this.tier = tier;
    }

    public Integer getPassage_id() {
        return passage_id;
    }

    public void setPassage_id(Integer passage_id) {
        this.passage_id = passage_id;
    }

    public Integer getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(Integer question_id) {
        this.question_id = question_id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "PassagesAnswersTier{" +
                "id=" + id +
                ", tier=" + tier +
                ", passage_id=" + passage_id +
                ", question_id=" + question_id +
                ", user_id=" + user_id +
                '}';
    }

    public PassagesAnswersTier(Integer id, Integer tier, Integer passage_id, Integer question_id, Integer user_id) {
        this.id = id;
        this.tier = tier;
        this.passage_id = passage_id;
        this.question_id = question_id;
        this.user_id = user_id;
    }

    static PassagesAnswersTier from(Row row) {
        return new PassagesAnswersTier(row.getInteger("id"), row.getInteger("tier"), row.getInteger("passage_id"), row.getInteger("question_id"), row.getInteger("user_id"));
    }

    public static Multi<PassagesAnswersTier> findByNameAndPassword(PgPool client, String name, String password) {
        return client.preparedQuery("SELECT * FROM passages_answers_tier JOIN Users ON Users.name = $1 AND Users.password = $2 WHERE passages_answers_tier.user_id = Users.id")
                .execute(Tuple.of(name, password)) .onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem().transform(PassagesAnswersTier::from);
    }


    public static Uni<Boolean> delete(PgPool client, String name, String password, Integer id) {
        return client
                .preparedQuery("DELETE FROM passages_answers_tier USING users WHERE  users.name = $1 AND users.password = $2 AND passages_answers_tier.user_id = users.id AND passages_answers_tier.id = $3 ")
                .execute(Tuple.of(name, password, id))
                .onItem()
                .transform(m -> m.rowCount() == 1);
    }



    public static Uni<Long> save(PgPool client, Integer tier, Integer passage_id, Integer question_id, String name, String password) {

        String sql = "INSERT INTO passages_answers_tier (id, tier, passage_id, question_id, user_id) VALUES ((select max(id) + 1 from passages_answers_tier), $1, $2, $3, (SELECT id from Users WHERE name = $4 AND password = $5)) RETURNING id";

        System.out.println(sql);
        return client
                .preparedQuery(sql)
                .execute(Tuple.of(tier, passage_id, question_id, name, password))
                .onItem()
                .transform(m -> m.iterator().next().getLong("id"));
    }

  

}
