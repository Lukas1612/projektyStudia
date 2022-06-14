package org.acme;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Tuple;

import java.util.ArrayList;

public class LearningUnit {

    ArrayList<DescribedPassage> describedPassages = null;
    TestPassage testPassage;
    ArrayList<TestQuestionsWithChoices> testQuestions = null;
    ArrayList<UserTests> userTests = null;

    public LearningUnit(ArrayList<DescribedPassage> describedPassages, TestPassage testPassage, ArrayList<TestQuestionsWithChoices> testQuestions,  ArrayList<UserTests> userTests) {
        this.describedPassages = describedPassages;
        this.testPassage = testPassage;
        this.testQuestions = testQuestions;
        this.userTests = userTests;
    }

    public LearningUnit(TestPassage testPassage) {
        this.testPassage = testPassage;
        this.describedPassages = new  ArrayList<DescribedPassage>();
        this.testQuestions = new  ArrayList<TestQuestionsWithChoices>();
        this.userTests = new ArrayList<UserTests>();
    }

    public ArrayList<DescribedPassage> getDescribedPassages() {
        return describedPassages;
    }

    public void setDescribedPassages(ArrayList<DescribedPassage> describedPassages) {
        this.describedPassages = describedPassages;
    }

    public TestPassage getTestPassage() {
        return testPassage;
    }

    public void setTestPassage(TestPassage testPassage) {
        this.testPassage = testPassage;
    }

    public ArrayList<TestQuestionsWithChoices> getTestQuestions() {
        return testQuestions;
    }

    public void setTestQuestions(ArrayList<TestQuestionsWithChoices> testQuestions) {
        this.testQuestions = testQuestions;
    }

    public  ArrayList<UserTests> getUserTests() {
        return userTests;
    }

    public void setUserTests( ArrayList<UserTests> userTests) {
        this.userTests = userTests;
    }

    @Override
    public String toString() {
        return "LearningUnit{" +
                "describedPassages=" + describedPassages +
                ", testPassage=" + testPassage +
                ", testQuestions=" + testQuestions +
                ", userTests=" + userTests +
                '}';
    }

    public static Multi<LearningUnit> findAll(PgPool client, String language, String name, String password) {


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



        Multi<TestPassage> testPassageMulti = Uni.createFrom().item( client.query("SELECT * FROM test_passages")
                .execute().await().indefinitely())
                .onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem().transform(TestPassage::from);

        Multi<TestQuestions> testQuestionsMulti = Uni.createFrom().item( client.query("SELECT * FROM test_questions")
                .execute().await().indefinitely())
                .onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem().transform(TestQuestions::from);

        Multi<TestQuestionsChoices> testQuestionsChoicesMulti = Uni.createFrom().item( client.query("SELECT * FROM test_question_choices")
                .execute().await().indefinitely())
                .onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem().transform(TestQuestionsChoices::from);

        Multi<UserTestQuestionChoices> userTestQuestionsChoicesMulti = Uni.createFrom().item( client.preparedQuery("SELECT * FROM user_test_question_choices JOIN users ON users.id = user_test_question_choices.user_id WHERE users.name = $1 and users.password = $2" )
                .execute(Tuple.of(name, password)).await().indefinitely())
                .onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem().transform(UserTestQuestionChoices::from);

        Multi<UserTests> userTestsMulti = Uni.createFrom().item( client.preparedQuery("SELECT * FROM user_tests JOIN users ON users.id = user_tests.user_id WHERE users.name = $1 and users.password = $2")
                .execute(Tuple.of(name, password)).await().indefinitely())
                .onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem().transform(UserTests::from);


        Multi<PassagesAnswersTier> answersTierMulti = Uni.createFrom().item( client.preparedQuery("SELECT * FROM passages_answers_tier JOIN Users ON users.name = $1 and users.password = $2 WHERE passages_answers_tier.user_id = Users.id")
                .execute(Tuple.of(name, password)).await().indefinitely())
                .onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem().transform(PassagesAnswersTier::from);


//**************************************************
//****************** creating describedPassageMulti
        ArrayList<DescribedPassage> describedPassage = new  ArrayList<DescribedPassage>();

        passages.onItem().transform(i -> i)
                .subscribe().with(
                item -> describedPassage.add(new DescribedPassage(item))
        );


        Multi<DescribedPassage> describedPassageMulti = Multi.createFrom().iterable(describedPassage);

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

                    answersTierMulti.onItem().transform(i -> i).subscribe().with(
                            answetT -> {
                                if(answetT.passage_id == dPassage.id)
                                {
                                    dPassage.answersTier.add(answetT);
                                }
                            }
                    );
                }
        );


//**************************************************
//****************** creating testQuestionsWithChoicesMulti


        ArrayList<TestQuestionsWithChoices> testQuestionsWithChoices = new  ArrayList<TestQuestionsWithChoices>();

        testQuestionsMulti.onItem().transform(i -> i)
                .subscribe().with(
                item -> testQuestionsWithChoices.add(new TestQuestionsWithChoices(item))
        );

        Multi<TestQuestionsWithChoices> testQuestionsWithChoicesMulti = Multi.createFrom().iterable(testQuestionsWithChoices);


        testQuestionsWithChoicesMulti.onItem().transform(i -> i).subscribe().with(
                question -> {
                    testQuestionsChoicesMulti.onItem().transform(i -> i).subscribe().with(
                            choice -> {
                                if(choice.test_questions_id == question.testQuestions.id)
                                {
                                    question.questionChoices.add(choice);
                                }
                            }
                    );


                    userTestQuestionsChoicesMulti.onItem().transform(i -> i).subscribe().with(
                            uChoice -> {
                                if(uChoice.test_question_id == question.testQuestions.id)
                                {
                                    question.userQuestionChoices.add(uChoice);
                                }
                            }
                    );
                }
        );

//*******************************************************************
//****************************** creating learningUnitsMulti

        ArrayList<LearningUnit> learningUnits = new  ArrayList<LearningUnit>();

        testPassageMulti.onItem().transform(i -> i)
                .subscribe().with(
                item -> learningUnits.add(new LearningUnit(item))
        );


        Multi<LearningUnit> learningUnitsMulti = Multi.createFrom().iterable(learningUnits);

         learningUnitsMulti.onItem().transform(i -> i).subscribe().with(
                 learningUnit -> {
                    describedPassageMulti.onItem().transform(i -> i).subscribe().with(
                            dp -> {
                                if(dp.test_passage_id == learningUnit.testPassage.id)
                                {
                                    learningUnit.describedPassages.add(dp);
                                }
                            }
                    );

                     testQuestionsWithChoicesMulti.onItem().transform(i -> i).subscribe().with(
                            tQuestion -> {
                                if(tQuestion.testQuestions.test_passage_id == learningUnit.testPassage.id)
                                {
                                    learningUnit.testQuestions.add(tQuestion);
                                }
                            }
                    );

                     userTestsMulti.onItem().transform(i -> i).subscribe().with(
                             ut -> {
                                 if(ut.test_passage_id == learningUnit.testPassage.id)
                                 {
                                     learningUnit.userTests.add(ut);
                                 }
                             }
                     );
                }
        );

         return learningUnitsMulti;
    }
}
