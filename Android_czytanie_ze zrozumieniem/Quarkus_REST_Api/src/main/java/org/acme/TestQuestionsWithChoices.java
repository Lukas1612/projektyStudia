package org.acme;

import io.vertx.mutiny.sqlclient.Row;

import java.util.ArrayList;

public class TestQuestionsWithChoices {

    TestQuestions testQuestions;
    public ArrayList<TestQuestionsChoices> questionChoices = null;
    public ArrayList<UserTestQuestionChoices> userQuestionChoices = null;

    public TestQuestionsWithChoices(Integer id, String text, Integer test_passage_id, ArrayList<TestQuestionsChoices> questionChoices,  ArrayList<UserTestQuestionChoices> userQuestionChoices) {
        testQuestions = new TestQuestions(id, text, test_passage_id);
        this.questionChoices = questionChoices;
        this.userQuestionChoices = userQuestionChoices;
    }

    public TestQuestionsWithChoices(TestQuestions testQuestion) {
        testQuestions = new TestQuestions(testQuestion);
        this.questionChoices = new ArrayList<TestQuestionsChoices>();
        this.userQuestionChoices = new ArrayList<UserTestQuestionChoices>();
    }

    public Integer getId() {
        return testQuestions.id;
    }

    public void setId(Integer id) {
        testQuestions.id = id;
    }

    public String getText() {
        return testQuestions.text;
    }

    public void setText(String text) {
        testQuestions.text = text;
    }

    public Integer getTest_passage_id() {
        return testQuestions.test_passage_id;
    }

    public void setTest_passage_id(Integer test_passage_id) {
        testQuestions.test_passage_id = test_passage_id;
    }

    public ArrayList<TestQuestionsChoices> getQuestionChoices() {
        return questionChoices;
    }

    public void setQuestionChoices(ArrayList<TestQuestionsChoices> questionChoices) {
        this.questionChoices = questionChoices;
    }

    public TestQuestions getTestQuestions() {
        return testQuestions;
    }

    public void setTestQuestions(TestQuestions testQuestions) {
        this.testQuestions = testQuestions;
    }

    public ArrayList<UserTestQuestionChoices> getUserQuestionChoices() {
        return userQuestionChoices;
    }

    public void setUserQuestionChoices(ArrayList<UserTestQuestionChoices> userQuestionChoices) {
        this.userQuestionChoices = userQuestionChoices;
    }

    @Override
    public String toString() {
        return "TestQuestionsWithChoices{" +
                "testQuestions=" + testQuestions +
                ", questionChoices=" + questionChoices +
                ", userQuestionChoices=" + userQuestionChoices +
                '}';
    }
}
