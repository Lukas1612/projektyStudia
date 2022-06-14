package com.example.readyreadingkotlin.questions.qdb;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TestQuestionDao {

    @Insert
    void insertAllQuestions(List<TestQuestionEntity> questions);

    @Query("SELECT * FROM test_questionnaire_questions")
    List<QuestionEntity> getAllQuestions();

    @Query("DELETE FROM test_questionnaire_questions")
    void deleteAllQuestions();
}
