package org.acme;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Tuple;
import org.acme.Flashcard_groups;

import java.util.ArrayList;

public class Flashcards_in_groups {

    Flashcard_groups group;
    ArrayList<Flashcard> flashcards = null;

    public Flashcard_groups getGroup() {
        return group;
    }

    public void setGroup(Flashcard_groups group) {
        this.group = group;
    }

    public ArrayList<Flashcard> getFlashcards() {
        return flashcards;
    }

    public void setFlashcards(ArrayList<Flashcard> flashcards) {
        this.flashcards = flashcards;
    }

    @Override
    public String toString() {
        return "Flashcards_in_groups{" +
                "group=" + group +
                ", flashcards=" + flashcards +
                '}';
    }

    public Flashcards_in_groups(Flashcard_groups group) {
        this.group = group;
        this.flashcards = new ArrayList<Flashcard>();
    }

    public Flashcards_in_groups(Flashcard_groups group, ArrayList<Flashcard> flashcards) {
        this.group = group;
        this.flashcards = flashcards;
    }


    public static Multi<Flashcards_in_groups> findAll(PgPool client, String name, String password) {

//"SELECT * FROM flashcards JOIN users ON users.id = flashcards.user_id WHERE users.name=$1 AND users.password=$2"
        Multi<Flashcard> flashcardsMulti = Uni.createFrom().item(client.preparedQuery("SELECT * FROM flashcards JOIN users ON users.name=$1 AND users.password=$2  WHERE users.id = flashcards.user_id OR flashcards.user_id = 0 ")
                .execute(Tuple.of(name, password)).await().indefinitely())
                .onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem().transform(Flashcard::from);

        Multi<Flashcard_groups> flashcardGroupsMulti = Uni.createFrom().item(client.preparedQuery("SELECT * FROM flashcards_groups JOIN users ON users.id = flashcards_groups.user_id WHERE users.name=$1 AND users.password=$2")
                .execute(Tuple.of(name, password)).await().indefinitely())
                .onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem().transform(Flashcard_groups::from);



        ArrayList<Flashcards_in_groups> flashcardsInGroups = new  ArrayList<Flashcards_in_groups>();

        flashcardGroupsMulti.onItem().transform(i -> i)
                .subscribe().with(
                item -> flashcardsInGroups.add(new Flashcards_in_groups(item))
        );



        Multi<Flashcards_in_groups> flashcardsInGroupsMulti = Multi.createFrom().iterable(flashcardsInGroups);


        flashcardsInGroupsMulti.onItem().transform(i -> i).subscribe().with(
                flashcardInGroup -> {
                    flashcardsMulti.onItem().transform(i -> i).subscribe().with(
                            flashcard -> {
                                if(flashcard.group_id == flashcardInGroup.group.id)
                                {
                                    flashcardInGroup.flashcards.add(flashcard);
                                }
                            }
                    );

                }
        );

       return flashcardsInGroupsMulti;

    }
}
