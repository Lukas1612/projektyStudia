package org.acme;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.Tuple;

public class Flashcard {
    Integer id;
    Integer group_id;
    Integer user_id;
    String word;
    String translation;
    String example_sentence;
    String translated_sentence;

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

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public String getExample_sentence() {
        return example_sentence;
    }

    public void setExample_sentence(String example_sentence) {
        this.example_sentence = example_sentence;
    }

    public String getTranslated_sentence() {
        return translated_sentence;
    }

    public void setTranslated_sentence(String translated_sentence) {
        this.translated_sentence = translated_sentence;
    }

    public Integer getGroup_id() {
        return group_id;
    }

    public void setGroup_id(Integer group_id) {
        this.group_id = group_id;
    }

    public Flashcard(Integer id, Integer user_id, String word, String translation, String example_sentence, String translated_sentence, Integer group_id) {
        this.id = id;
        this.user_id = user_id;
        this.word = word;
        this.translation = translation;
        this.example_sentence = example_sentence;
        this.translated_sentence = translated_sentence;
        this.group_id = group_id;
    }

    static Flashcard from(Row row) {
        return new Flashcard(row.getInteger("id"), row.getInteger("user_id"), row.getString("word"), row.getString("translation"),  row.getString("example_sentence"),  row.getString("translated_sentence"), row.getInteger("group_id"));
    }

    public static Multi<Flashcard> findByNameAndPassword(PgPool client, String name, String password) {
        return client.preparedQuery("SELECT * FROM flashcards JOIN Users ON Users.name = $1 AND Users.password = $2 WHERE Users.id = flashcards.user_id")
                .execute(Tuple.of(name, password)) .onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem().transform(Flashcard::from);
    }



    public static Uni<Long> save(PgPool client, String name, String password, String word, String translation, String example_sentence, String translated_sentence, Integer group_id) {

        String sql = "INSERT INTO flashcards (id, word, translation, example_sentence, translated_sentence, user_id, group_id) VALUES ((select max(id) + 1 from flashcards), " +"\'" + word +"\', "  +"\'" + translation +"\', \'" + example_sentence + "\', \'" + translated_sentence +"\', " + "(SELECT id from Users WHERE name = \'" + name +"\' AND password = \'" + password +"\'), " + group_id + ") RETURNING id";

        System.out.println(sql);
        return client
                .preparedQuery(sql)
                .execute()
                .onItem()
                .transform(m -> m.iterator().next().getLong("id"));
    }


    @Override
    public String toString() {
        return "Flashcard{" +
                "id=" + id +
                ", group_id=" + group_id +
                ", user_id=" + user_id +
                ", word='" + word + '\'' +
                ", translation='" + translation + '\'' +
                ", example_sentence='" + example_sentence + '\'' +
                ", translated_sentence='" + translated_sentence + '\'' +
                '}';

    }

    public static Uni<Boolean> delete(PgPool client, String name, String password, Integer id) {
        return client
                .preparedQuery("DELETE FROM flashcards USING users WHERE  users.name = $1 AND users.password = $2 AND flashcards.user_id = users.id AND flashcards.id = $3 ")
                .execute(Tuple.of(name, password, id))
                .onItem()
                .transform(m -> m.rowCount() == 1);
    }

}
