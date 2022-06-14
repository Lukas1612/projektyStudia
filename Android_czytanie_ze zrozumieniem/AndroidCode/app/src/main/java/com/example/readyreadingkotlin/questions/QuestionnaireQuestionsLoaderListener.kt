package com.example.readyreadingkotlin.questions

import com.example.readyreadingkotlin.learning_unit.LearningUnit

interface QuestionnaireQuestionsLoaderListener {

    fun update(unitsList: MutableList<QuestionnaireQuestions>)
}