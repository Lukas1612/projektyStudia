package org.acme;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import java.util.ArrayList;

public class DescribedPassage {

    Integer id;
    String  text;
    String title;
    ArrayList<Tips> tips = null;
    ArrayList<Questions> questions = null;
    ArrayList<PassagesAnswersTier> answersTier = null;
    String translationtext;
    Integer test_passage_id;


    public void setTIps(Multi<Tips> tips) {

        tips.onItem().transform(i -> i)
                .subscribe().with(
                item -> this.tips.add(item));
    }

    public DescribedPassage(Integer id, String text, String title, ArrayList<Tips> tips, ArrayList<Questions> questions, ArrayList<PassagesAnswersTier> answersTier, String translationtext, Integer test_passage_id) {
        this.id = id;
        this.text = text;
        this.title = title;
        this.tips = tips;
        this.questions = questions;
        this.answersTier = answersTier;
        this.translationtext = translationtext;
        this.test_passage_id = test_passage_id;
    }

    public ArrayList<PassagesAnswersTier> getAnswersTier() {
        return answersTier;
    }

    public void setAnswersTier(ArrayList<PassagesAnswersTier> answersTier) {
        this.answersTier = answersTier;
    }

    public String getTranslationtext() {
        return translationtext;
    }

    public void setTranslationtext(String translationText) {
        this.translationtext = translationText;
    }

    public ArrayList<Questions> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Questions> questions) {
        this.questions = questions;
    }

    public DescribedPassage(Passages passages) {

        this.id = passages.getId();
        this.text = passages.getText();
        this.title = passages.getTitle();
        this.tips = new  ArrayList<Tips>();
        this.questions = new  ArrayList<Questions>();
        this.answersTier = new  ArrayList<PassagesAnswersTier>();
        this.translationtext = passages.translationtext;
        this.test_passage_id = passages.test_passage_id;

    }

    public Integer getTest_passage_id() {
        return test_passage_id;
    }


    public void setTest_passage_id(Integer test_passage_id) {
        this.test_passage_id = test_passage_id;
    }

    public ArrayList<Tips> getTips() {
        return tips;
    }


    public void addTip(Tips tip)
    {
        this.tips.add(tip);
    }

    public void addQuestion(Questions question)
    {
        this.questions.add(question);
    }

    public Integer getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getTitle() {
        return title;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTips(ArrayList<Tips> tips) {
        this.tips = tips;
    }

    @Override
    public String toString() {
        return "DescribedPassage{" +
                "id=" + id +
                ", text='" + text + '\'' +
                ", title='" + title + '\'' +
                ", tips=" + tips +
                ", questions=" + questions +
                ", answersTier=" + answersTier +
                ", translationtext='" + translationtext + '\'' +
                ", test_passage_id=" + test_passage_id +
                '}';
    }

    public static Multi<DescribedPassage> findAll(PgPool client, String language) {


        if(language.matches(" "))
        {
            language = "ERROR";
        }

        Multi<Passages> passages = Uni.createFrom().item( client.query("SELECT  passages.id, passages.text, passages.title, translations.translationtext, passages.test_passage_id FROM passages FULL JOIN translations ON translations.passage_id = passages.id FULL JOIN languages ON translations.language_id = languages.id WHERE languages.name = '" + language + "'")
                .execute().await().indefinitely())
                .onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem().transform(Passages::from);

        Multi<Tips> tipsMulti = Uni.createFrom().item( client.query("SELECT * FROM tips")
                .execute().await().indefinitely())
                .onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem().transform(Tips::from);



        Multi<Questions> questionsMulti = Uni.createFrom().item( client.query("SELECT * FROM questions")
                .execute().await().indefinitely())
                .onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem().transform(Questions::from);



        ArrayList<DescribedPassage> describedPassage = new  ArrayList<DescribedPassage>();

        passages.onItem().transform(i -> i)
                .subscribe().with(
                item -> describedPassage.add(new DescribedPassage(item))
        );


        Multi<DescribedPassage> describedPassageMulti = Multi.createFrom().iterable(describedPassage);
/*
        tipsMulti.onItem().transform(i -> i)
                .subscribe().with(
                tip-> { describedPassageMulti.onItem().transform(i -> i).subscribe().with(
                        dPassage -> {
                            if(tip.passage_id == dPassage.id)
                            {
                                try {
                                    dPassage.addTip(tip);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                );

                });
*/

        describedPassageMulti.onItem().transform(i -> i).subscribe().with(
                dPassage -> {
                    tipsMulti.onItem().transform(i -> i).subscribe().with(
                            tip -> {
                                if(tip.passage_id == dPassage.id)
                                {
                                    dPassage.addTip(tip);
                                }
                            }
                    );

                    questionsMulti.onItem().transform(i -> i).subscribe().with(
                            question -> {
                                if(question.passage_id == dPassage.id)
                                {
                                    dPassage.addQuestion(question);
                                }
                            }
                    );
                }
        );


        return describedPassageMulti;

    }



}
