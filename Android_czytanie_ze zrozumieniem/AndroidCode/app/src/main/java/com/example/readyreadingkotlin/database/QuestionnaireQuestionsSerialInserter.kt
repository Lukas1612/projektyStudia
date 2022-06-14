package com.example.readyreadingkotlin.database

class QuestionnaireQuestionsSerialInserter(override var iterator: Iterator<Int>?, var listOFUserCheckBoxChoices: MutableList<Int>?,  var describedPassageId: Int? , var name: String, var password: String):  AbstractSerialDataInserter<Int>(iterator) {


    override fun insertNextSingleData() {
        val id = iterator!!.next()

        val dbHelper = DbHelper()
        dbHelper.addNewUserQuestionnaireQuestions( id,  describedPassageId!!,  name, password, this)
    }
}