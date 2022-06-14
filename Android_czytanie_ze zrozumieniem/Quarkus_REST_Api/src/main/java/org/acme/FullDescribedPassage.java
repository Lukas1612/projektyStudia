package org.acme;

import io.smallrye.mutiny.Multi;

import java.util.ArrayList;
import java.util.List;

public class FullDescribedPassage {

    Integer passage_id;
    String passage_text;
    String passage_title;

    Integer language_id;

    String bingTr;
    String googleTr;
    String deepL;
    String proofreadedTr;

    ArrayList<String> questions = null;
    ArrayList<String> answers = null;

    ArrayList<String> tips = null;

    public Integer getPassage_id() {
        return passage_id;
    }

    public void setPassage_id(Integer passage_id) {
        this.passage_id = passage_id;
    }

    public String getPassage_text() {
        return passage_text;
    }

    public void setPassage_text(String passage_text) {
        this.passage_text = passage_text;
    }

    public String getPassage_title() {
        return passage_title;
    }

    public void setPassage_title(String passage_title) {
        this.passage_title = passage_title;
    }

    public Integer getLanguage_id() {
        return language_id;
    }

    public void setLanguage_id(Integer language_id) {
        this.language_id = language_id;
    }

    public String getBingTr() {
        return bingTr;
    }

    public void setBingTr(String bingTr) {
        this.bingTr = bingTr;
    }

    public String getGoogleTr() {
        return googleTr;
    }

    public void setGoogleTr(String googleTr) {
        this.googleTr = googleTr;
    }

    public String getDeepL() {
        return deepL;
    }

    public void setDeepL(String deepL) {
        this.deepL = deepL;
    }

    public String getProofreadedTr() {
        return proofreadedTr;
    }

    public void setProofreadedTr(String proofreadedTr) {
        this.proofreadedTr = proofreadedTr;
    }

    public String[] getQuestions() {

        int n = this.questions.size();
        String[] q = new String[n];

        for(int i=0; i<n; ++i)
        {
            q[i]=this.questions.get(i);
        }
        return q;
    }

    public void setQuestions(Multi<String> questions) {
        questions.onItem().transform(i -> i)
                .subscribe().with(
                item -> this.questions.add(item));
    }

    public String[] getAnswers() {
        int n = this.answers.size();
        String[] a = new String[n];

        for(int i=0; i<n; ++i)
        {
            a[i]=this.answers.get(i);
        }
        return a;
    }

    public void setAnswers(Multi<String> answers) {
        answers.onItem().transform(i -> i)
                .subscribe().with(
                item -> this.answers.add(item));
    }

    public String[] getTips() {
        int n = this.tips.size();
        String[] t = new String[n];

        for(int i=0; i<n; ++i)
        {
            t[i]=this.tips.get(i);
        }
        return t;
    }

    public void setTips(Multi<String> tips) {
        tips.onItem().transform(i -> i)
                .subscribe().with(
                item -> this.tips.add(item));
    }
}
