package com.example.readyreadingkotlin.learning_unit


import android.os.Parcelable
import com.example.readyreadingkotlin.described_passage.DescribedPassage
import kotlinx.parcelize.Parcelize


@Parcelize
class LearningUnit(var describedPassages:  List<DescribedPassage>?,
                   var testPassage: TestPassage?,
                   var testQuestions:  List<TestQuestionsWithChoices>?,
                   var userTests: List<UserTests>?
                   ) : Parcelable
{
    override fun toString(): String {
        return "LearningUnit(describedPassages=$describedPassages, testPassage=$testPassage, testQuestions=$testQuestions, userTests=$userTests)"
    }
}
