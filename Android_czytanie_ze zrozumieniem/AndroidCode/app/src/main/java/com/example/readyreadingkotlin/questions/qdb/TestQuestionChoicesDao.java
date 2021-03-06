package com.example.readyreadingkotlin.questions.qdb;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;
@Dao
public interface TestQuestionChoicesDao {

    @Insert
    void insertAllChoicesOfQuestion(List<TestQuestionWithChoicesEntity> choices);

    @Query("UPDATE test_answer_choices SET  ans_choice_state = :selectState WHERE question_id = :questionId AND ans_choice_pos =:optionId")
    void updateQuestionWithChoice(String selectState, String questionId, String optionId);

    @Query("SELECT ans_choice_state FROM test_answer_choices WHERE question_id = :questionId AND ans_choice_pos =:optionId")
    String isChecked(String questionId, String optionId);

    @Query("SELECT * FROM test_answer_choices WHERE ans_choice_state =:selected")
    List<QuestionWithChoicesEntity> getAllQuestionsWithChoices(String selected);

    @Query("DELETE FROM test_answer_choices")
    void deleteAllChoicesOfQuestion();
}
