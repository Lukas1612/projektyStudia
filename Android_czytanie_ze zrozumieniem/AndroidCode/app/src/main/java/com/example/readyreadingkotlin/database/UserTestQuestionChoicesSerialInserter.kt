package com.example.readyreadingkotlin.database

import com.example.readyreadingkotlin.learning_unit.UserTestQuestionChoices

class UserTestQuestionChoicesSerialInserter(override var iterator: Iterator<UserTestQuestionChoices>?, var userTestQuestionChoices: MutableList<UserTestQuestionChoices>?, var name: String, var password: String):  AbstractSerialDataInserter<UserTestQuestionChoices>(iterator) {

    override fun insertNextSingleData() {
        val userTestQuestionChoice = iterator!!.next()
        DbHelper().postUserTestQuestionChoices(userTestQuestionChoice!!.test_question_id!!, userTestQuestionChoice!!.question_choice_id!!, name, password, this)
    }
}